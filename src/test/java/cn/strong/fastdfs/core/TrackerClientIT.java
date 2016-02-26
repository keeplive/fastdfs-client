package cn.strong.fastdfs.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import cn.strong.fastdfs.util.Helper;

public class TrackerClientIT {

	@Test
	public void testGetUploadStorageString() throws InterruptedException, IOException {
		FastdfsExecutor session = new FastdfsExecutor();
		session.afterPropertiesSet();
		List<InetSocketAddress> seeds = Arrays
				.asList(new InetSocketAddress("192.168.20.68", 22122));
		TrackerClient client = new TrackerClient(session, seeds);
		CountDownLatch latch = new CountDownLatch(1);
		client.getUploadStorage("group1", (info, ex) -> {
			if (ex != null) {
				System.out.println("error: " + ex);
			} else {
				System.out.println(info);
			}
			latch.countDown();
			Helper.closeQuietly(session);
		});
		latch.await();
	}

}
