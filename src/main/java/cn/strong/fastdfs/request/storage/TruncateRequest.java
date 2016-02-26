/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.ERRNO_OK;
import static cn.strong.fastdfs.core.Consts.FDFS_PROTO_PKG_LEN_SIZE;
import static cn.strong.fastdfs.core.Consts.HEAD_LEN;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Sender;

/**
 * 截取请求
 * 
 * @author liulongbiao
 *
 */
public class TruncateRequest implements Sender {

	public final StoragePath spath;
	public final int truncatedSize;

	public TruncateRequest(StoragePath spath, int truncatedSize) {
		this.spath = spath;
		this.truncatedSize = truncatedSize;
	}

	@Override
	public void send(Channel ch) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		int length = 2 * FDFS_PROTO_PKG_LEN_SIZE + pathBytes.length;
		byte cmd = CommandCodes.STORAGE_PROTO_CMD_TRUNCATE_FILE;
		ByteBuf buf = ch.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);
		buf.writeLong(pathBytes.length);
		buf.writeLong(truncatedSize);
		buf.writeBytes(pathBytes);
		ch.writeAndFlush(buf);
	}

}
