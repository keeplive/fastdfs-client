/**
 * 
 */
package cn.strong.fastdfs.util;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ProgressiveFuture;

/**
 * AsyncAction 的创建
 * 
 * @author liulongbiao
 *
 */
public class AsyncActions {
	/**
	 * 从 Future 对象创建 AsyncAction
	 * 
	 * @param future
	 * @return
	 */
	public static <T> FutureAsyncAction<T> from(Future<T> future) {
		return new FutureAsyncAction<T>(future);
	}

	/**
	 * @param future
	 * @return
	 */
	public static <T> ProgressiveFutureAsyncAction<T> from(ProgressiveFuture<T> future) {
		return new ProgressiveFutureAsyncAction<T>(future);
	}
}
