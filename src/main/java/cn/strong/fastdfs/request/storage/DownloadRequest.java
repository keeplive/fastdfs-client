/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static cn.strong.fastdfs.core.Consts.ERRNO_OK;
import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_LONG_LEN;
import static cn.strong.fastdfs.core.Consts.HEAD_LEN;
import static cn.strong.fastdfs.util.Helper.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;

import java.util.Objects;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Sender;

/**
 * 下载请求
 * 
 * @author liulongbiao
 *
 */
public class DownloadRequest implements Sender {

	public static final int DEFAULT_OFFSET = 0;
	public static final int SIZE_UNLIMIT = 0;

	public final StoragePath spath;
	public final int offset;
	public final int size;

	public DownloadRequest(StoragePath spath, int offset, int size) {
		this.spath = Objects.requireNonNull(spath);
		this.offset = offset;
		this.size = size;
	}

	public DownloadRequest(StoragePath spath) {
		this(spath, DEFAULT_OFFSET, SIZE_UNLIMIT);
	}

	@Override
	public void send(Channel ch) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		int length = 2 * FDFS_LONG_LEN + FDFS_GROUP_LEN + pathBytes.length;
		byte cmd = CommandCodes.STORAGE_PROTO_CMD_DOWNLOAD_FILE;
		ByteBuf buf = ch.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);

		buf.writeLong(offset);
		buf.writeLong(size);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN);
		ByteBufUtil.writeUtf8(buf, spath.path);
		ch.writeAndFlush(buf);
	}

}
