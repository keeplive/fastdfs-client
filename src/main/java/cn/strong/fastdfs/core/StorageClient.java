/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.File;
import java.util.Objects;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.request.storage.UploadRequest;
import cn.strong.fastdfs.response.DefaultReciver;
import cn.strong.fastdfs.response.StoragePathDecoder;
import cn.strong.fastdfs.util.Callback;

/**
 * StorageClient
 * 
 * @author liulongbiao
 *
 */
public class StorageClient {
	private FastdfsExecutor executor;

	public StorageClient(FastdfsExecutor executor) {
		this.executor = Objects.requireNonNull(executor, "storage executor is null");
	}

	/**
	 * 上传文件
	 * 
	 * @param storage
	 * @param file
	 * @param callback
	 */
	public void upload(StorageServerInfo storage, File file, Callback<StoragePath> callback) {
		executor.exec(storage.getAddress(), new UploadRequest(file, storage.storePathIndex),
				new DefaultReciver<>(StoragePathDecoder.INSTANCE), callback);
	}

	/**
	 * 上传内容，其中内容字段 content 的支持以下类型：
	 * 
	 * <ul>
	 * <li>{@link java.io.File}</li>
	 * <li>{@link java.io.InputStream}</li>
	 * <li><code>byte[]</code></li>
	 * <li>{@link java.nio.channels.ReadableByteChannel}</li>
	 * <li>{@link io.netty.channel.FileRegion}</li>
	 * <li>{@link io.netty.handler.stream.ChunkedInput}</li>
	 * <li>{@link io.netty.buffer.ByteBuf}</li>
	 * </ul>
	 * 
	 * @param storage
	 *            存储服务器信息
	 * @param content
	 *            上传内容
	 * @param length
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param callback
	 */
	public void upload(StorageServerInfo storage, Object content, long length, String ext,
			Callback<StoragePath> callback) {
		executor.exec(storage.getAddress(), 
				new UploadRequest(content, length, ext, storage.storePathIndex), 
				new DefaultReciver<>(StoragePathDecoder.INSTANCE), 
				callback);
	}

}