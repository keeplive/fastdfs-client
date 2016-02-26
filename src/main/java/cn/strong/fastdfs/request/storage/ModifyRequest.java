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
 * 修改文件请求
 * 
 * @author liulongbiao
 *
 */
public class ModifyRequest extends AbstractFileRequest {

	public final StoragePath spath;
	public final int offset;

	public ModifyRequest(File file, StoragePath spath, int offset) {
		super(file);
		this.spath = spath;
		this.offset = offset;
	}

	public ModifyRequest(Object content, long size, StoragePath spath, int offset) {
		super(content, size);
		this.spath = spath;
		this.offset = offset;
	}

	@Override
	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_MODIFY_FILE;
	}

	@Override
	protected ByteBuf meta(ByteBufAllocator alloc) {
		int metaLen = 3 * Consts.FDFS_PROTO_PKG_LEN_SIZE + spath.path.getBytes(UTF_8).length;
		ByteBuf buf = alloc.buffer(metaLen);
		byte[] pathBytes = spath.path.getBytes(UTF_8);
		buf.writeLong(pathBytes.length);
		buf.writeLong(offset);
		buf.writeLong(size);
		buf.writeBytes(pathBytes);
		return buf;
	}

}
