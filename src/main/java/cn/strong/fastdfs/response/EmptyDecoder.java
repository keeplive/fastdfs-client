/**
 * 
 */
package cn.strong.fastdfs.response;

import io.netty.buffer.ByteBuf;

/**
 * 空响应解码器
 * 
 * @author liulongbiao
 *
 */
public enum EmptyDecoder implements ResponseDecoder<Void> {

	INSTANCE;

	@Override
	public long expectLength() {
		return 0;
	}

	@Override
	public Void decode(ByteBuf buf) {
		return null;
	}

}
