/**
 * 
 */
package cn.strong.fastdfs.util;

import java.util.function.Function;



/**
 * 回调接口
 * 
 * @author liulongbiao
 *
 */
public interface Callback<T> {

	void onComplete(T result, Throwable t);

	/**
	 * 组合回调和转换函数
	 * 
	 * @param callback
	 * @param fn
	 * @return
	 */
	public static <S, T> Callback<S> compose(Callback<T> callback, Function<S, T> fn) {
		return new Callback<S>() {

			@Override
			public void onComplete(S result, Throwable t) {
				if (t != null) {
					callback.onComplete(null, t);
				} else {
					try {
						callback.onComplete(fn.apply(result), null);
					} catch (Exception e) {
						callback.onComplete(null, e);
					}
				}
			}
		};
	}
}
