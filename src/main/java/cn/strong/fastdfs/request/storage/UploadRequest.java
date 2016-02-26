/**
 * 
 */
package cn.strong.fastdfs.request.storage;
import static cn.strong.fastdfs.core.Consts.ERRNO_OK;
import static cn.strong.fastdfs.core.Consts.FDFS_FILE_EXT_LEN;
import static cn.strong.fastdfs.core.Consts.FDFS_PROTO_PKG_LEN_SIZE;
import static cn.strong.fastdfs.core.Consts.FDFS_STORE_PATH_INDEX_LEN;
import static cn.strong.fastdfs.core.Consts.HEAD_LEN;
import static cn.strong.fastdfs.util.Helper.writeFixLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.handler.stream.ChunkedInput;
import io.netty.handler.stream.ChunkedNioStream;
import io.netty.handler.stream.ChunkedStream;

import java.io.File;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

import cn.strong.fastdfs.core.CommandCodes;
import cn.strong.fastdfs.request.Sender;
import cn.strong.fastdfs.util.Helper;

/**
 * 上传请求
 * 
 * @author liulongbiao
 *
 */
public class UploadRequest implements Sender {

	public final Object content;
	public final long length;
	public final String ext;
	public final byte storePathIndex;

	public UploadRequest(File file, byte storePathIndex) {
		if (!file.exists()) {
			throw new IllegalArgumentException("uploaded file " + file.getName() + " not exist");
		}
		long length = file.length();
		this.content = new DefaultFileRegion(file, 0, length);
		this.length = length;
		this.ext = Helper.getFileExt(file.getName());
		this.storePathIndex = storePathIndex;
	}

	public UploadRequest(Object content, long length, String ext, byte storePathIndex) {
		this.content = toContent(content);
		this.length = length;
		this.ext = ext;
		this.storePathIndex = storePathIndex;
	}

	private static Object toContent(Object content) {
		Objects.requireNonNull(content, "uploaded content shoule not be null");
		if (content instanceof ByteBuf || content instanceof FileRegion
				|| content instanceof ChunkedInput) {
			return content;
		} else if (content instanceof File) {
			File file = (File) content;
			if (!file.exists()) {
				throw new IllegalArgumentException("uploaded file " + file.getName() + " not exist");
			}
			return new DefaultFileRegion(file, 0, file.length());
		} else if (content instanceof ReadableByteChannel) {
			return new ChunkedNioStream((ReadableByteChannel) content);
		} else if (content instanceof InputStream) {
			return new ChunkedStream((InputStream) content);
		} else if (content instanceof byte[]) {
			return Unpooled.wrappedBuffer((byte[]) content);
		} else {
			throw new IllegalArgumentException("unknown content type : "
					+ content.getClass().getName());
		}
	}

	@Override
	public void send(Channel ch) {
		int preLen = FDFS_STORE_PATH_INDEX_LEN + FDFS_PROTO_PKG_LEN_SIZE + FDFS_FILE_EXT_LEN;
		ByteBuf buf = ch.alloc().buffer(HEAD_LEN + preLen);
		buf.writeLong(preLen + length);
		buf.writeByte(cmd());
		buf.writeByte(ERRNO_OK);
		buf.writeByte(storePathIndex);
		buf.writeLong(length);
		writeFixLength(buf, ext, FDFS_FILE_EXT_LEN);
		ch.write(buf);
		ch.writeAndFlush(content);
	}

	protected byte cmd() {
		return CommandCodes.STORAGE_PROTO_CMD_UPLOAD_FILE;
	}

}
