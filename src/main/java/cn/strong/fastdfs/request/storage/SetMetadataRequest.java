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

import java.util.Objects;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.Sender;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 设置文件属性请求
 * 
 * @author liulongbiao
 *
 */
public class SetMetadataRequest implements Sender {

	public final StoragePath spath;
	public final Metadata metadata;
	public final byte flag;

	public SetMetadataRequest(StoragePath spath, Metadata metadata, byte flag) {
		this.spath = Objects.requireNonNull(spath);
		this.metadata = metadata;
		this.flag = flag;
	}

	@Override
	public void send(Channel ch) {
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		byte[] metadatas = metadata.toBytes(UTF_8);
		int length = 2 * FDFS_LONG_LEN + 1 + FDFS_GROUP_LEN + pathBytes.length + metadatas.length;
		byte cmd = CommandCodes.STORAGE_PROTO_CMD_SET_METADATA;
		ByteBuf buf = ch.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);

		buf.writeLong(pathBytes.length);
		buf.writeLong(metadatas.length);
		buf.writeByte(flag);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN);
		buf.writeBytes(pathBytes);
		buf.writeBytes(metadatas);

		ch.writeAndFlush(buf);
	}

}
