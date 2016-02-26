/**
 * 
 */
package cn.strong.fastdfs.util;

import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.io.Closeable;
import java.io.IOException;

/**
 * 帮助类
 * 
 * @author liulongbiao
 *
 */
public class Helper {

	/**
	 * 异步执行，当 Future 完成时回调 Callback
	 * 
	 * @param future
	 * @param callback
	 */
	public static <T> void execAsync(Future<T> future, Callback<T> callback) {
		future.addListener(new GenericFutureListener<Future<T>>() {

			@Override
			public void operationComplete(Future<T> f) throws Exception {
				if (f.isSuccess()) {
					callback.onComplete(f.getNow(), null);
				} else {
					callback.onComplete(null, f.cause());
				}
			}

		});
	}

	/**
	 * 关闭
	 * 
	 * @param closeable
	 */
	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	/**
	 * 判断字符串为空
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isEmpty(String content) {
		return content == null || content.isEmpty();
	}

	/**
	 * 给 ByteBuf 写入定长字符串
	 * <p>
	 * 若字符串长度大于定长，则截取定长字节；若小于定长，则补零
	 * 
	 * @param buf
	 * @param content
	 * @param length
	 */
	public static void writeFixLength(ByteBuf buf, String content, int length) {
		byte[] bytes = content.getBytes(CharsetUtil.UTF_8);
		int blen = bytes.length;
		int wlen = blen > length ? length : blen;
		buf.writeBytes(bytes, 0, wlen);
		if (wlen < length) {
			buf.writeZero(length - wlen);
		}
	}

	/**
	 * 读取固定长度的字符串(修剪掉补零的字节)
	 * 
	 * @param in
	 * @param length
	 * @return
	 */
	public static String readString(ByteBuf in, int length) {
		return in.readBytes(length).toString(UTF_8).trim();
	}

	/**
	 * 读取字符串(修剪掉补零的字节)
	 * 
	 * @param in
	 * @return
	 */
	public static String readString(ByteBuf in) {
		return in.toString(UTF_8);
	}
}
