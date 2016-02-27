/**
 * 
 */
package cn.strong.fastdfs.util;

import io.netty.util.concurrent.GenericProgressiveFutureListener;
import io.netty.util.concurrent.ProgressiveFuture;

/**
 * 基于 ProgressiveFuture 的 AsyncAction 实现
 * 
 * @author liulongbiao
 *
 */
public class ProgressiveFutureAsyncAction<T> implements AsyncAction<T> {

	final ProgressiveFuture<T> future;

	public ProgressiveFutureAsyncAction(ProgressiveFuture<T> future) {
		this.future = future;
	}

	@Override
	public void action(Callback<T> callback) {
		this.future.addListener(new ProgressCallbackFutureListener<T>(callback));
	}

	private static class ProgressCallbackFutureListener<T>
			implements GenericProgressiveFutureListener<ProgressiveFuture<T>> {
		final Callback<T> callback;

		public ProgressCallbackFutureListener(Callback<T> callback) {
			this.callback = callback;
		}

		@Override
		public void operationComplete(ProgressiveFuture<T> future) throws Exception {
			if (future.isSuccess()) {
				try {
					callback.call(future.getNow(), null);
				} catch (Exception e) {
					callback.call(null, e);
				}
			} else {
				callback.call(null, future.cause());
			}
		}

		@Override
		public void operationProgressed(ProgressiveFuture<T> future, long progress, long total) throws Exception {
			if (callback instanceof ProgressCallback) {
				((ProgressCallback<T>) callback).onProgress(progress, total);
			}
		}

	}

}
