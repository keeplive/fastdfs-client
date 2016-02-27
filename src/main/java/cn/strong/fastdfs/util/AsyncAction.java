/**
 * 
 */
package cn.strong.fastdfs.util;

import java.util.function.Function;

/**
 * 异步操作
 * 
 * @author liulongbiao
 *
 */
public interface AsyncAction<T> {

	/**
	 * 设置异步执行回调
	 * 
	 * @param callback
	 */
	void action(Callback<T> callback);

	/**
	 * 将异步操作映射
	 * 
	 * @param fn
	 * @return
	 */
	default <V> AsyncAction<V> map(Function<T, V> fn) {
		AsyncAction<T> source = this;
		return callback -> {
			source.action(callback.compose(fn));
		};
	}

	/**
	 * 将异步操作映射展开
	 * 
	 * @param fn
	 * @return
	 */
	default <V> AsyncAction<V> then(Function<T, AsyncAction<V>> fn) {
		AsyncAction<T> source = this;
		return callback -> {
			source.action(prepose(callback, fn));
		};
	}

	/**
	 * 组合一个前置异步操作生成器得到一个组合的回调函数
	 * 
	 * @param callback
	 * @param fn
	 * @return
	 */
	static <T, V> Callback<T> prepose(Callback<V> callback, Function<T, AsyncAction<V>> fn) {
		return (t, ex) -> {
			if (ex != null) {
				callback.call(null, ex);
			} else {
				try {
					fn.apply(t).action(callback);
				} catch (Exception e) {
					callback.call(null, e);
				}
			}
		};
	}

}
