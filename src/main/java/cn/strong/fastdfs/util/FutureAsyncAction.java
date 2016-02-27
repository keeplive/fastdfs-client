/**
 * 
 */
package cn.strong.fastdfs.util;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

/**
 * 基于 Future 的 AsyncAction 实现
 * 
 * @author liulongbiao
 *
 */
public class FutureAsyncAction<T> implements AsyncAction<T> {
	final Future<T> future;

	public FutureAsyncAction(Future<T> future) {
		this.future = future;
	}

	@Override
	public void action(Callback<T> callback) {
		future.addListener(new CallbackFutureListener<T>(callback));
	}

	static class CallbackFutureListener<T> implements FutureListener<T> {
		final Callback<T> callback;

		public CallbackFutureListener(Callback<T> callback) {
			this.callback = callback;
		}

		@Override
		public void operationComplete(Future<T> future) throws Exception {
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
	}
}
