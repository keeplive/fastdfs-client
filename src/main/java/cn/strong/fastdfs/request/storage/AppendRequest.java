/**
 * 
 */
package cn.strong.fastdfs.request.storage;

import static io.netty.util.CharsetUtil.UTF_8;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.File;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.core.Consts;
import cn.strong.fastdfs.model.StoragePath;

/**
 * @author liulongbiao
 *
 */
public class AppendRequest extends AbstractFileRequest {

	public final StoragePath spath;

	public AppendRequest(File file, StoragePath spath) {
		super(file);
		this.spath = spath;
	}

	public AppendRequest(Object content, long size, StoragePath spath) {
		super(content, size);
		this.spath = spath;
	}

	@Override
	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_APPEND_FILE;
	}

	@Override
	protected ByteBuf meta(ByteBufAllocator alloc) {
		int metaLen = 2 * Consts.FDFS_PROTO_PKG_LEN_SIZE + spath.path.getBytes(UTF_8).length;
		ByteBuf buf = alloc.buffer(metaLen);
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		buf.writeLong(pathBytes.length);
		buf.writeLong(size);
		buf.writeBytes(pathBytes);
		return buf;
	}

}
