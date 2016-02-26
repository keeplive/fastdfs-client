/**
 * 
 */
package cn.strong.fastdfs.request.tracker;

import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE;
import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
import static cn.strong.fastdfs.core.Consts.ERRNO_OK;
import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.HEAD_LEN;
import static cn.strong.fastdfs.util.Helper.isEmpty;
import static cn.strong.fastdfs.util.Helper.writeFixLength;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import cn.strong.fastdfs.request.Sender;

/**
 * 获取可上传的存储服务器
 * 
 * @author liulongbiao
 *
 */
public class GetUploadStorage implements Sender {

	private String group;

	public GetUploadStorage(String group) {
		this.group = group;
	}

	@Override
	public void send(Channel ch) {
		int length = isEmpty(group) ? 0 : FDFS_GROUP_LEN;
		byte cmd = isEmpty(group) ? TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE
				: TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE;
		ByteBuf buf = ch.alloc().buffer(length + HEAD_LEN);
		buf.writeLong(length);
		buf.writeByte(cmd);
		buf.writeByte(ERRNO_OK);
		if (!isEmpty(group)) {
			writeFixLength(buf, group, FDFS_GROUP_LEN);
		}
		ch.writeAndFlush(buf);
	}

}
