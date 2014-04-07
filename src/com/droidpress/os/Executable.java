package com.droidpress.os;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public abstract class Executable implements Runnable {
	private static Executor sExecutor;
	private static Handler  sHandler;

	static {
		sExecutor = Executors.newCachedThreadPool();
		sHandler = new Handler(Looper.getMainLooper());
	}

	protected static void runOnMainThread(Runnable action) {
		sHandler.post(action);
	}

	protected static void runInBackground(Runnable action) {
		sExecutor.execute(action);
	}

	public void execute() {
		runOnMainThread(this);
	}

	public void executeInBackground() {
		runInBackground(this);
	}
}