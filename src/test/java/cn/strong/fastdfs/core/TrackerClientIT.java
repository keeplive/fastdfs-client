package cn.strong.fastdfs.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.util.Helper;

public class TrackerClientIT {

	private FastdfsExecutor executor;
	private TrackerClient client;

	@Before
	public void setup() {
		executor = new FastdfsExecutor();
		executor.afterPropertiesSet();
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
		CountDownLatch latch = new CountDownLatch(1);
		client.getUploadStorage("group1", (info, ex) -> {
			if (ex != null) {
				System.out.println("error: " + ex);
			} else {
				System.out.println(info);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testGetDownloadStorage() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.getDownloadStorage(spath, (info, ex) -> {
			if (ex != null) {
				System.out.println("error: " + ex);
			} else {
				System.out.println(info);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testFindDownloadStorages() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.findDownloadStorages(spath, (infos, ex) -> {
			if (ex != null) {
				System.out.println("error: " + ex);
			} else {
				infos.forEach(info -> {
					System.out.println(info);
				});
			}
			latch.countDown();
		});
		latch.await();
	}

}
