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
 * 抽象 Receiver 基类
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractReceiver<T> implements Receiver<T> {

	protected boolean atHead = true;
	protected long length;

	@Override
	public void receive(ByteBuf in, Promise<T> promise) {
		if (atHead) {
			readHead(in);
		}
		readContent(in, promise);
	}

	private void readHead(ByteBuf in) {
		if (in.readableBytes() < Consts.HEAD_LEN) {
			return;
		}
		length = in.readLong();
		byte cmd = in.readByte();
		byte errno = in.readByte();
		if (errno != 0) {
			throw new FastdfsException("Fastdfs responsed with an error, errno is " + errno);
		}
		if (cmd != FDFS_PROTO_CMD_RESP) {
			throw new FastdfsException("Expect response command code error : " + cmd);
		}
		long expectLength = expectLength();
		if (expectLength >= 0) {
			if (length != expectLength) {
				throw new FastdfsException("Expect response length : " + expectLength
						+ " , but receive length : " + length);
			}
		}
		atHead = false;
	}

	protected long expectLength() {
		return -1;
	}
	
	protected abstract void readContent(ByteBuf in, Promise<T> promise);

}
