/**
 * 
 */
package cn.strong.fastdfs.response;

import static cn.strong.fastdfs.core.Consts.FDFS_GROUP_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_HOST_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_STORAGE_LEN;
import static cn.strong.fastdfs.util.Helper.readString;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import cn.strong.fastdfs.core.FastdfsException;
import cn.strong.fastdfs.model.StorageServerInfo;

/**
 * 存储服务器信息列表解码器
 * 
 * @author liulongbiao
 *
 */
public enum StorageServerInfoListDecoder implements
		ResponseDecoder<List<StorageServerInfo>> {
	INSTANCE;

	@Override
	public List<StorageServerInfo> decode(ByteBuf in) {
		int size = in.readableBytes();
		if (size < FDFS_STORAGE_LEN) {
			throw new FastdfsException("body length : " + size
					+ " is less than required length " + FDFS_STORAGE_LEN);
		}
		if ((size - FDFS_STORAGE_LEN) % FDFS_HOST_LEN != 0) {
			throw new FastdfsException("body length : " + size
					+ " is invalidate. ");
		}

		int count = (size - FDFS_STORAGE_LEN) / FDFS_HOST_LEN + 1;
		List<StorageServerInfo> result = new ArrayList<StorageServerInfo>(count);

		String group = readString(in, FDFS_GROUP_LEN);
		String mainHost = readString(in, FDFS_HOST_LEN);
		int port = (int) in.readLong();
		result.add(new StorageServerInfo(group, mainHost, port));

		for (int i = 1; i < count; i++) {
			String host = readString(in, FDFS_HOST_LEN);
			result.add(new StorageServerInfo(group, host, port));
		}

		return result;
	}

}
