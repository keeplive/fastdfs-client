/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.model.StorageServerInfo;
import cn.strong.fastdfs.util.Helper;

/**
 * @author liulongbiao
 *
 */
public class StorageClientIT {

	private FastdfsExecutor executor;
	private StorageClient client;

	@Before
	public void setup() {
		executor = new FastdfsExecutor();
		executor.afterPropertiesSet();
		client = new StorageClient(executor);
	}

	@After
	public void destroy() {
		Helper.closeQuietly(executor);
	}

	@Test
	@Ignore
	public void testUploadFile() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		client.upload(info, new File("pom.xml"), (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testUploadInputStream() throws InterruptedException, IOException {
		File file = new File("pom.xml");
		InputStream input = new FileInputStream(file);
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		client.upload(info, input, file.length(), Helper.getFileExt(file.getName()), (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testUploadBytes() throws InterruptedException, IOException {
		byte[] bytes = "Hello fastdfs".getBytes();
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		client.upload(info, bytes, bytes.length, "inf", (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testUploadAppenderBytes() throws InterruptedException, IOException {
		byte[] bytes = "Hello fastdfs".getBytes();
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		client.uploadAppender(info, bytes, bytes.length, "inf", (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testAppenderBytes() throws InterruptedException, IOException {
		byte[] bytes = "\nappend fastdfs".getBytes();
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQIp6EEG9xAAAAADVhaBw260.inf");
		client.append(info, spath, bytes, (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testModify() throws InterruptedException, IOException {
		byte[] bytes = "modify fastdfs".getBytes();
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQIp6EEG9xAAAAADVhaBw260.inf");
		client.modify(info, spath, 12, bytes, (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDelete() throws InterruptedException, IOException {
		CountDownLatch latch = new CountDownLatch(1);
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQHjuACGt4AAAADTVhaBw716.inf");
		client.delete(info, spath, (p, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(p);
			}
			latch.countDown();
		});
		latch.await();
	}
}
