/**
 * 
 */
package cn.strong.fastdfs.request.tracker;

import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.AbstractStoragePathRequest;

/**
 * 获取可下载的存储服务器
 * 
 * @author liulongbiao
 *
 */
public class GetUpdateStorageRequest extends AbstractStoragePathRequest {

	public GetUpdateStorageRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	protected byte cmd() {
		return TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE;
	}

}
