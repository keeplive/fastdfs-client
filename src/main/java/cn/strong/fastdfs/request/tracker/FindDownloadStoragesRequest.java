/**
 * 
 */
package cn.strong.fastdfs.request.tracker;

import static cn.strong.fastdfs.core.CommandCodes.TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.request.AbstractStoragePathRequest;

/**
 * 获取可下载的存储服务器列表
 * 
 * @author liulongbiao
 *
 */
public class FindDownloadStoragesRequest extends AbstractStoragePathRequest {

	public FindDownloadStoragesRequest(StoragePath spath) {
		super(spath);
	}

	@Override
	protected byte cmd() {
		return TRACKER_PROTO_CMD_SERVICE_QUERY_FETCH_ALL;
	}

}
