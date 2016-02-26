/**
 * 
 */
package cn.strong.fastdfs.request;

import static cn.strong.fastdfs.core.Consts.ERRNO_OK;
import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.HEAD_LEN;
import static cn.strong.fastdfs.util.Helper.writeFixLength;
import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;

import cn.strong.fastdfs.model.StoragePath;

/**
 * 请求内容为存储路径的请求
 * 
 * @author liulongbiao
 *
 */
public abstract class AbstractStoragePathRequest implements Sender {

	private StoragePath spath;

	public AbstractStoragePathRequest(StoragePath spath) {
		this.spath = spath;
	}

	@Override
	public void send(Channel ch) {
		int length = FDFS_GROUP_LEN + spath.path.getBytes(UTF_8).length;
		byte cmd = cmd();
		ByteBuf buf = ch.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);
		writeFixLength(buf, spath.group, FDFS_GROUP_LEN);
		ByteBufUtil.writeUtf8(buf, spath.path);
		ch.writeAndFlush(buf);
	}

	protected abstract byte cmd();

}
