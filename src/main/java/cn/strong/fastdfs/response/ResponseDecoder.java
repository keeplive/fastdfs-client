/**
 * 
 */
package cn.strong.fastdfs.response;

import io.netty.buffer.ByteBuf;

/**
 * 响应解码器
 * 
 * @author liulongbiao
 *
 */
public interface ResponseDecoder<T> {

	/**
	 * 期待的长度值，小于 0 时不验证
	 * 
	 * @return
	 */
	default long expectLength() {
		return -1;
	}

	/**
	 * 解码响应
	 * 
	 * @param buf
	 * @return
	 */
	T decode(ByteBuf buf);
}
