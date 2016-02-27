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

	/**
	 * called when AsyncAction completed
	 * 
	 * @param result
	 * @param ex
	 */
	void call(T result, Throwable ex);

	default <S> Callback<S> compose(Function<S, T> before) {
		Callback<T> source = this;
		return (s, ex) -> {
			if (ex != null) {
				source.call(null, ex);
			} else {
				try {
					source.call(before.apply(s), ex);
				} catch (Exception e) {
					source.call(null, e);
				}
			}
		};
	}
}
