/**
 * 
 */
package cn.strong.fastdfs.response;

import static cn.strong.fastdfs.core.CommandCodes.FDFS_PROTO_CMD_RESP;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Promise;

import cn.strong.fastdfs.core.Consts;
import cn.strong.fastdfs.core.FastdfsException;

/**
 * 默认接收器实现
 * 
 * @author liulongbiao
 *
 */
public class DefaultReciver<T> implements Receiver<T> {

	private ResponseDecoder<T> decoder;

	private boolean atHead = true;
	private int length;

	public DefaultReciver(ResponseDecoder<T> decoder) {
		this.decoder = decoder;
	}

	@Override
	public void receive(ByteBuf in, Promise<T> promise) {
		if (atHead) {
			readHead(in);
		}
		readBody(in, promise);
	}

	private void readHead(ByteBuf in) {
		if (in.readableBytes() < Consts.HEAD_LEN) {
			return;
		}
		length = (int) in.readLong();
		byte cmd = in.readByte();
		byte errno = in.readByte();
		if (errno != 0) {
			throw new FastdfsException("Fastdfs responsed with an error, errno is " + errno);
		}
		if (cmd != FDFS_PROTO_CMD_RESP) {
			throw new FastdfsException("Expect response command code error : " + cmd);
		}
		long expectLength = decoder.expectLength();
		if (expectLength >= 0) {
			if (length != expectLength) {
				throw new FastdfsException("Expect response length : " + expectLength
						+ " , but receive length : " + length);
			}
		}
		atHead = false;
	}

	private void readBody(ByteBuf in, Promise<T> promise) {
		if (in.readableBytes() < length) {
			return;
		}
		ByteBuf buf = in.readSlice(length);
		T result = decoder.decode(buf);
		atHead = true;
		promise.setSuccess(result);
	}
}
