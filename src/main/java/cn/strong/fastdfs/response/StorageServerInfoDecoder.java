/**
 * 
 */
package cn.strong.fastdfs.response;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_HOST_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_STORAGE_STORE_LEN;
import static cn.strong.fastdfs.util.Helper.readString;
import io.netty.buffer.ByteBuf;

import cn.strong.fastdfs.model.StorageServerInfo;

/**
 * 存储服务器信息解码器
 * 
 * @author liulongbiao
 *
 */
public enum StorageServerInfoDecoder implements
		ResponseDecoder<StorageServerInfo> {
	INSTANCE;

	@Override
	public int expectLength() {
		return FDFS_STORAGE_STORE_LEN;
	}

	@Override
	public StorageServerInfo decode(ByteBuf in) {
		String group = readString(in, FDFS_GROUP_LEN);
		String host = readString(in, FDFS_HOST_LEN);
		int port = (int) in.readLong();
		byte idx = in.readByte();
		return new StorageServerInfo(group, host, port, idx);
	}

}
