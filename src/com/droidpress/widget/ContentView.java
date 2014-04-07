package com.droidpress.widget;

import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ContentView extends WebView {
	private static final String TAG = "ContentView";

	private static final String BLANK_URL = "about:blank";
	private static final String BASE_URL  = "file:///android_asset/";
	private static final String MIME_TYPE = "text/html";
	private static final String ENCODING  = "UTF-8";

	private static TemplateCache sTemplateCache;

	private ContentViewClient   mViewClient;
	private ContentChromeClient mChromeClient;

	private OnLinkClickListener mLinkClickListener;

	@SuppressLint("SetJavaScriptEnabled")
	public ContentView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (sTemplateCache == null)
			sTemplateCache = new TemplateCache();

		mViewClient   = new ContentViewClient();
		mChromeClient = new ContentChromeClient();

		getSettings().setJavaScriptEnabled(true);
		setWebViewClient(mViewClient);
		setWebChromeClient(mChromeClient);
	}

	public void setOnLinkClickListener(OnLinkClickListener listener) {
		mLinkClickListener = listener;
	}

	public void clear() {
		loadUrl(BLANK_URL);
	}

	public void loadContent(String content) {
		loadDataWithBaseURL(BASE_URL, content, MIME_TYPE, ENCODING, null);
	}

	public void loadContent(String content, Runnable callback) {
		mViewClient.onPageFinishedCallback = callback;
		loadContent(content);
	}

	public void loadTemplate(int resId, Object... args) {
		loadContent(String.format(getTemplate(resId), args));
	}

	public String getTemplate(int resId) {
		return sTemplateCache.getTemplate(resId);
	}

	public interface OnLinkClickListener {
		public boolean onLinkClick(WebView reader, Uri link);
	}

	private class TemplateCache {
		private SparseArray<String> mTemplateMap;

		public TemplateCache() {
			mTemplateMap = new SparseArray<String>();
		}

		public String getTemplate(int resId) {
			if (mTemplateMap.indexOfKey(resId) < 0) {
				InputStreamReader reader = new InputStreamReader(getResources()
						.openRawResource(resId));

				StringBuilder builder = new StringBuilder();

				char[] buffer = new char[256];
				int length;

				try {
					while ((length = reader.read(buffer)) > 0)
						builder.append(new String(buffer, 0, length));

					reader.close();
				} catch (IOException e) {
					Log.e(TAG, e.getLocalizedMessage(), e);
				}

				mTemplateMap.put(resId, builder.toString());
			}

			return mTemplateMap.get(resId);
		}
	}

	private class ContentViewClient extends WebViewClient {
		private Runnable onPageFinishedCallback;

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.w(TAG, "Error " + errorCode + ": " + description + " -- From "
					+ failingUrl);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (mLinkClickListener != null)
				return mLinkClickListener.onLinkClick(ContentView.this,
						Uri.parse(url));

			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			if (onPageFinishedCallback != null) {
				onPageFinishedCallback.run();
				onPageFinishedCallback = null;
			}
		}
	}

	private class ContentChromeClient extends WebChromeClient {
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			Log.i(TAG, "Alert: " + message + " -- From URL " + url);

			return super.onJsAlert(view, url, message, result);
		}

		@Override
		@Deprecated
		public void onConsoleMessage(String message, int lineNumber,
				String sourceID) {
			Log.i(TAG, "Message: " + message + " -- From " + sourceID
					+ ", Line " + lineNumber);
		}
	}
}