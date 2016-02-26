/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.request.storage.DownloadRequest.DEFAULT_OFFSET;
import static cn.strong.fastdfs.request.storage.DownloadRequest.SIZE_UNLIMIT;

import java.io.File;
import java.util.Objects;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.request.storage.AppendRequest;
import cn.strong.fastdfs.request.storage.DeleteRequest;
import cn.strong.fastdfs.request.storage.DownloadRequest;
import cn.strong.fastdfs.request.storage.ModifyRequest;
import cn.strong.fastdfs.request.storage.TruncateRequest;
import cn.strong.fastdfs.request.storage.UploadAppenderRequest;
import cn.strong.fastdfs.request.storage.UploadRequest;
import cn.strong.fastdfs.response.EmptyDecoder;
import cn.strong.fastdfs.response.StoragePathDecoder;
import cn.strong.fastdfs.response.StreamReceiver;
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
		executor.exec(storage.getAddress(), 
				new UploadRequest(file, storage.storePathIndex),
				StoragePathDecoder.INSTANCE, callback);
	}

	/**
	 * 上传文件，其中文件内容字段 content 的支持以下类型：
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
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param callback
	 */
	public void upload(StorageServerInfo storage, Object content, long size, String ext,
			Callback<StoragePath> callback) {
		executor.exec(storage.getAddress(),
				new UploadRequest(content, size, ext, storage.storePathIndex),
				StoragePathDecoder.INSTANCE, callback);
	}

	/**
	 * 上传可追加文件内容
	 * 
	 * @param storage
	 * @param file
	 * @param callback
	 */
	public void uploadAppender(StorageServerInfo storage, File file, Callback<StoragePath> callback) {
		executor.exec(storage.getAddress(),
				new UploadAppenderRequest(file, storage.storePathIndex),
				StoragePathDecoder.INSTANCE, callback);
	}

	/**
	 * 上传可追加文件内容，其中文件内容字段 content 的支持以下类型：
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
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param callback
	 */
	public void uploadAppender(StorageServerInfo storage, Object content, long size, String ext,
			Callback<StoragePath> callback) {
		executor.exec(storage.getAddress(), 
				new UploadAppenderRequest(content, size, ext, storage.storePathIndex),
				StoragePathDecoder.INSTANCE, callback);
	}

	/**
	 * 追加文件内容
	 * 
	 * @param storage
	 * @param spath
	 * @param content
	 * @param length
	 * @param callback
	 */
	public void append(StorageServerInfo storage, StoragePath spath, byte[] bytes,
			Callback<Void> callback) {
		executor.exec(storage.getAddress(), new AppendRequest(bytes, bytes.length, spath),
				EmptyDecoder.INSTANCE, callback);
	}

	/**
	 * 追加文件内容
	 * 
	 * @param storage
	 * @param spath
	 * @param content
	 * @param length
	 * @param callback
	 */
	public void modify(StorageServerInfo storage, StoragePath spath, int offset, byte[] bytes,
			Callback<Void> callback) {
		executor.exec(storage.getAddress(), new ModifyRequest(bytes, bytes.length, spath, offset),
				EmptyDecoder.INSTANCE, callback);
	}

	/**
	 * 删除文件
	 * 
	 * @param storage
	 * @param spath
	 * @param callback
	 */
	public void delete(StorageServerInfo storage, StoragePath spath, Callback<Void> callback) {
		executor.exec(storage.getAddress(), new DeleteRequest(spath), EmptyDecoder.INSTANCE,
				callback);
	}

	/**
	 * 截取文件
	 * 
	 * @param storage
	 * @param spath
	 * @param callback
	 */
	public void truncate(StorageServerInfo storage, StoragePath spath, Callback<Void> callback) {
		truncate(storage, spath, 0, callback);
	}

	/**
	 * 截取文件
	 * 
	 * @param storage
	 * @param spath
	 * @param truncatedSize
	 * @param callback
	 */
	public void truncate(StorageServerInfo storage, StoragePath spath, int truncatedSize,
			Callback<Void> callback) {
		executor.exec(storage.getAddress(), new TruncateRequest(spath, truncatedSize),
				EmptyDecoder.INSTANCE, callback);
	}
	
	/**
	 * 下载文件，其输出 output 参数支持以下类型
	 * 
	 * <ul>
	 * <li>{@link java.io.OutputStream}</li>
	 * <li>{@link java.nio.channels.GatheringByteChannel}</li>
	 * </ul>
	 * 
	 * @param storage
	 * @param spath
	 * @param output
	 * @param callback
	 */
	public void download(StorageServerInfo storage, StoragePath spath, Object output,
			Callback<Void> callback) {
		download(storage, spath, DEFAULT_OFFSET, SIZE_UNLIMIT, output, callback);
	}

	/**
	 * 下载文件
	 * 
	 * @param storage
	 * @param spath
	 * @param offset
	 * @param size
	 * @param output
	 * @param callback
	 */
	public void download(StorageServerInfo storage, StoragePath spath, int offset, int size,
			Object output, Callback<Void> callback) {
		executor.exec(storage.getAddress(), new DownloadRequest(spath, offset, size),
				StreamReceiver.newInstance(output), callback);
	}

}
