/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;

import cn.strong.fastdfs.model.Metadata;
import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.util.AsyncAction;

/**
 * FastDFS 客户端
 * 
 * @author liulongbiao
 *
 */
public class FastdfsClient {

	private TrackerClient trackerClient;
	private StorageClient storageClient;

	public FastdfsClient(FastdfsExecutor executor, List<InetSocketAddress> trackers) {
		this.trackerClient = new TrackerClient(executor, trackers);
		this.storageClient = new StorageClient(executor);
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            文件
	 * @param group
	 *            分组
	 * @return
	 */
	public AsyncAction<StoragePath> upload(File file, String group) {
		return trackerClient.getUploadStorage(group).then(info -> {
			return storageClient.upload(info, file);
		});
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
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param content
	 *            上传内容
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param group
	 *            分组
	 * @return
	 */
	public AsyncAction<StoragePath> upload(Object content, long size, String ext, String group) {
		return trackerClient.getUploadStorage(group).then(info -> {
			return storageClient.upload(info, content, size, ext);
		});
	}

	/**
	 * 上传可追加文件
	 * 
	 * @param file
	 *            文件
	 * @param group
	 *            分组
	 * @return
	 */
	public AsyncAction<StoragePath> uploadAppender(File file, String group) {
		return trackerClient.getUploadStorage(group).then(info -> {
			return storageClient.uploadAppender(info, file);
		});
	}

	/**
	 * 上传可追加文件，其中文件内容字段 content 的支持以下类型：
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
	 *            存储服务器信息，应该由 tracker 查询得到
	 * @param content
	 *            上传内容
	 * @param size
	 *            内容长度
	 * @param ext
	 *            扩展名
	 * @param group
	 *            分组
	 * @return
	 */
	public AsyncAction<StoragePath> uploadAppender(Object content, long size, String ext, String group) {
		return trackerClient.getUploadStorage(group).then(info -> {
			return storageClient.uploadAppender(info, content, size, ext);
		});
	}

	/**
	 * 下载文件，其输出 output 参数支持以下类型
	 * 
	 * <ul>
	 * <li>{@link java.io.OutputStream}</li>
	 * <li>{@link java.nio.channels.GatheringByteChannel}</li>
	 * </ul>
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @param output
	 *            输出流
	 * @return
	 */
	public AsyncAction<Void> download(StoragePath spath, Object out) {
		return trackerClient.getDownloadStorage(spath).then(info -> {
			return storageClient.download(info, spath, out);
		});
	}

	/**
	 * 删除文件
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @return
	 */
	public AsyncAction<Void> delete(StoragePath spath) {
		return trackerClient.getUpdateStorage(spath).then(info -> {
			return storageClient.delete(info, spath);
		});
	}

	/**
	 * 追加文件
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @param bytes
	 *            内容
	 * @return
	 */
	public AsyncAction<Void> append(StoragePath spath, byte[] bytes) {
		return trackerClient.getUpdateStorage(spath).then(info -> {
			return storageClient.append(info, spath, bytes);
		});
	}

	/**
	 * 修改文件
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @param offset
	 *            偏移量
	 * @param bytes
	 *            内容
	 * @return
	 */
	public AsyncAction<Void> modify(StoragePath spath, int offset, byte[] bytes) {
		return trackerClient.getUpdateStorage(spath).then(info -> {
			return storageClient.modify(info, spath, offset, bytes);
		});
	}

	/**
	 * 截取文件
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @return
	 */
	public AsyncAction<Void> truncate(StoragePath spath) {
		return truncate(spath, 0);
	}

	/**
	 * 截取文件
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @param truncatedSize
	 *            截取字节数
	 * @return
	 */
	public AsyncAction<Void> truncate(StoragePath spath, int truncatedSize) {
		return trackerClient.getUpdateStorage(spath).then(info -> {
			return storageClient.truncate(info, spath, truncatedSize);
		});
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 */
	public AsyncAction<Void> setMetadata(StoragePath spath, Metadata metadata) {
		return setMetadata(spath, metadata, Consts.STORAGE_SET_METADATA_FLAG_OVERWRITE);
	}

	/**
	 * 设置文件元数据
	 * 
	 * @param spath
	 *            服务器存储路径
	 * @param metadata
	 *            元数据
	 * @param flag
	 *            设置标识
	 */
	public AsyncAction<Void> setMetadata(StoragePath spath, Metadata metadata, byte flag) {
		return trackerClient.getUpdateStorage(spath).then(info -> {
			return storageClient.setMetadata(info, spath, metadata, flag);
		});
	}

	/**
	 * 获取文件元数据
	 * 
	 * @param spath
	 * @return
	 */
	public AsyncAction<Metadata> getMetadata(StoragePath spath) {
		return trackerClient.getUpdateStorage(spath).then(info -> {
			return storageClient.getMetadata(info, spath);
		});
	}
}
