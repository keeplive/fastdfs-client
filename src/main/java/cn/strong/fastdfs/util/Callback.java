/**
 * 
 */
package cn.strong.fastdfs.util;



/**
 * 回调接口
 * 
 * @author liulongbiao
 *
 */
public interface Callback<T> {

	void onComplete(T result, Throwable t);
}
