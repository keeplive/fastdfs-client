package cn.strong.fastdfs.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.util.Helper;

public class TrackerClientIT {

	private FastdfsExecutor executor;
	private TrackerClient client;

	@Before
	public void setup() {
		executor = new FastdfsExecutor(new FastdfsSettings());
		List<InetSocketAddress> seeds = Arrays
				.asList(new InetSocketAddress("192.168.20.68", 22122));
		client = new TrackerClient(executor, seeds);
	}

	@After
	public void destroy() {
		Helper.closeQuietly(executor);
	}

	@Test
	@Ignore
	public void testGetUploadStorageString() throws InterruptedException, IOException {
		client.getUploadStorage("group1").addListener(f -> {
			if (f.isSuccess()) {
				System.out.println(f.getNow());
			} else {
				f.cause().printStackTrace();
			}
		}).await();
	}

	@Test
	@Ignore
	public void testGetDownloadStorage() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		client.getDownloadStorage(spath).addListener(f -> {
			if (f.isSuccess()) {
				System.out.println(f.getNow());
			} else {
				f.cause().printStackTrace();
			}
		}).await();
	}

	@Test
	@Ignore
	public void testFindDownloadStorages() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		client.findDownloadStorages(spath).addListener(f -> {
			if (f.isSuccess()) {
				@SuppressWarnings("unchecked")
				List<StorageServerInfo> infos = (List<StorageServerInfo>) f.getNow();
				infos.forEach(info -> {
					System.out.println(info);
				});
			} else {
				f.cause().printStackTrace();
			}
		}).await();
	}

}
