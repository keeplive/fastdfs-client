/**
 * 
 */
package cn.strong.fastdfs.response;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Promise;

/**
 * 默认接收器实现
 * 
 * @author liulongbiao
 *
 */
public class DefaultReciver<T> extends AbstractReceiver<T> {

	private ResponseDecoder<T> decoder;

	public DefaultReciver(ResponseDecoder<T> decoder) {
		this.decoder = decoder;
	}

	@Override
	protected long expectLength() {
		return decoder.expectLength();
	}

	@Override
	protected void readContent(ByteBuf in, Promise<T> promise) {
		if (in.readableBytes() < length) {
			return;
		}
		ByteBuf buf = in.readSlice((int) length);
		T result = decoder.decode(buf);
		atHead = true;
		promise.setSuccess(result);
	}
}
