/**
 * 
 */
package cn.strong.fastdfs.util;

/**
 * 进度回调
 * 
 * @author liulongbiao
 *
 */
public interface ProgressCallback<T> extends Callback<T> {

	void onProgress(long progress, long total);
}
