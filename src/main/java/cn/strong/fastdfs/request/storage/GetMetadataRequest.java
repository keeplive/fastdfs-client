/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.AbstractStoragePathRequest;

/**
 * 获取文件属性请求
 * 
 * @author liulongbiao
 *
 */
public class GetMetadataRequest extends AbstractStoragePathRequest {

	public GetMetadataRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	public byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_GET_METADATA;
	}

}