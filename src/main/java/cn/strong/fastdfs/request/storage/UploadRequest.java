/**
 * 
 */
package cn.strong.fastdfs.request.storage;
import static cn.strong.fastdfs.core.Consts.FDFS_FILE_EXT_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_PROTO_PKG_LEN_SIZE;
import static cn.strong.fastdfs.core.Consts.FDFS_STORE_PATH_INDEX_LEN;
import static cn.strong.fastdfs.util.Helper.writeFixLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.File;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.util.Helper;

/**
 * 上传请求
 * 
 * @author liulongbiao
 *
 */
public class UploadRequest extends AbstractFileRequest {

	public final String ext;
	public final byte storePathIndex;

	public UploadRequest(File file, byte storePathIndex) {
		super(file);
		this.ext = Helper.getFileExt(file.getName());
		this.storePathIndex = storePathIndex;
	}

	public UploadRequest(Object content, long size, String ext, byte storePathIndex) {
		super(content, size);
		this.ext = ext;
		this.storePathIndex = storePathIndex;
	}

	@Override
	protected ByteBuf meta(ByteBufAllocator alloc) {
		int metaLen = FDFS_STORE_PATH_INDEX_LEN + FDFS_PROTO_PKG_LEN_SIZE + FDFS_FILE_EXT_LEN;
		ByteBuf buf = alloc.buffer(metaLen);
		buf.writeByte(storePathIndex);
		buf.writeLong(size);
		writeFixLength(buf, ext, FDFS_FILE_EXT_LEN);
		return buf;
	}

	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_UPLOAD_FILE;
	}

}
