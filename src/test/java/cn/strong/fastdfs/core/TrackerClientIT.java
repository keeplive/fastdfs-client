package cn.strong.fastdfs.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

}
