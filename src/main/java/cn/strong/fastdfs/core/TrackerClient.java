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

import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.request.tracker.GetUploadStorageRequest;
import cn.strong.fastdfs.response.DefaultReciver;
import cn.strong.fastdfs.response.StorageServerInfoDecoder;
import cn.strong.fastdfs.util.Callback;

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

	public void getUploadStorage(Callback<StorageServerInfo> callback) {
		getUploadStorage(null, callback);
	}

	public void getUploadStorage(String group, Callback<StorageServerInfo> callback) {
		executor.exec(pick(), new GetUploadStorageRequest(group), new DefaultReciver<>(
				StorageServerInfoDecoder.INSTANCE), callback);
	}
}
