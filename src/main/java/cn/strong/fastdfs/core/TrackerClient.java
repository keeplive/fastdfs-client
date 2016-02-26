/**
 * 
 */
package cn.strong.fastdfs.core;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.request.tracker.FindDownloadStoragesRequest;
import cn.strong.fastdfs.request.tracker.GetDownloadStorageRequest;
import cn.strong.fastdfs.request.tracker.GetUpdateStorageRequest;
import cn.strong.fastdfs.request.tracker.GetUploadStorageRequest;
import cn.strong.fastdfs.response.StorageServerInfoDecoder;
import cn.strong.fastdfs.response.StorageServerInfoListDecoder;
import cn.strong.fastdfs.util.Callback;
import cn.strong.fastdfs.util.Helper;

/**
 * TrackerClient
 * 
 * @author liulongbiao
 *
 */
public class TrackerClient {

	private static Logger LOG = LoggerFactory.getLogger(TrackerClient.class);

	private FastdfsExecutor executor;
	private List<InetSocketAddress> seeds;
	private PickStrategy strategy;

	public TrackerClient(FastdfsExecutor executor, List<InetSocketAddress> seeds) {
		this(executor, seeds, PickStrategy.ROUND_ROBIN);
	}

	public TrackerClient(FastdfsExecutor executor, List<InetSocketAddress> seeds,
			PickStrategy strategy) {
		this.executor = Objects.requireNonNull(executor, "tracker executor is null");
		if (seeds == null || seeds.isEmpty()) {
			throw new IllegalArgumentException("tracker addresses is empty.");
		}
		this.seeds = Collections.unmodifiableList(seeds);
		this.strategy = seeds.size() == 1 ? PickStrategy.FIRST : strategy;
		LOG.info("TrackerClient inited with {} seeds and strategy {}.", seeds.size(), this.strategy);
	}

	/*
	 * 选取一个地址
	 */
	private InetSocketAddress pick() {
		return strategy.pick(seeds);
	}

	/**
	 * 获取上传存储服务器地址
	 * 
	 * @param callback
	 */
	public void getUploadStorage(Callback<StorageServerInfo> callback) {
		getUploadStorage(null, callback);
	}

	/**
	 * 获取上传存储服务器地址
	 * 
	 * @param group
	 * @param callback
	 */
	public void getUploadStorage(String group, Callback<StorageServerInfo> callback) {
		executor.exec(pick(), new GetUploadStorageRequest(group),
				StorageServerInfoDecoder.INSTANCE, callback);
	}

	/**
	 * 获取下载存储服务器地址
	 * 
	 * @param path
	 * @param callback
	 */
	public void getDownloadStorage(StoragePath path, Callback<StorageServerInfo> callback) {
		executor.exec(pick(), new GetDownloadStorageRequest(path),
				StorageServerInfoListDecoder.INSTANCE, Callback.compose(callback, Helper::first));
	}

	/**
	 * 获取更新存储服务器地址
	 * 
	 * @param path
	 * @param callback
	 */
	public void getUpdateStorage(StoragePath path, Callback<StorageServerInfo> callback) {
		executor.exec(pick(), new GetUpdateStorageRequest(path),
				StorageServerInfoListDecoder.INSTANCE, Callback.compose(callback, Helper::first));
	}

	/**
	 * 请求 tracker 获取所有可用的下载 storage 地址列表
	 * 
	 * @param path
	 * @return
	 */
	public void findDownloadStorages(StoragePath path, Callback<List<StorageServerInfo>> callback) {
		executor.exec(pick(), new FindDownloadStoragesRequest(path),
				StorageServerInfoListDecoder.INSTANCE, callback);
	}
}
