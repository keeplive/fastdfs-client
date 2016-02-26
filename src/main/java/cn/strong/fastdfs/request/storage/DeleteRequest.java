/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.AbstractStoragePathRequest;

/**
 * 删除请求
 * 
 * @author liulongbiao
 *
 */
public class DeleteRequest extends AbstractStoragePathRequest {

	public DeleteRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_DELETE_FILE;
	}

}
