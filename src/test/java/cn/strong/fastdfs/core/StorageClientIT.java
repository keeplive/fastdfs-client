/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.ByteArrayOutputStream;
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
		executor = new FastdfsExecutor(new FastdfsSettings());
		client = new StorageClient(executor);
	}

	@After
	public void destroy() {
		Helper.closeQuietly(executor);
	}

	@Test
	@Ignore
	public void testUploadFile() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(info, new File("pom.xml")).action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
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
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(info, input, file.length(), Helper.getFileExt(file.getName())).action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
					latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testUploadBytes() throws InterruptedException, IOException {
		byte[] bytes = "Hello fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(info, bytes, bytes.length, "inf").action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testUploadAppenderBytes() throws InterruptedException, IOException {
		byte[] bytes = "Hello fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		CountDownLatch latch = new CountDownLatch(1);
		client.uploadAppender(info, bytes, bytes.length, "inf").action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testAppenderBytes() throws InterruptedException, IOException {
		byte[] bytes = "\nappend fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQIp6EEG9xAAAAADVhaBw260.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.append(info, spath, bytes).action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testModify() throws InterruptedException, IOException {
		byte[] bytes = "modify fastdfs".getBytes();
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQIp6EEG9xAAAAADVhaBw260.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.modify(info, spath, 12, bytes).action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDelete() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQHjuACGt4AAAADTVhaBw716.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.delete(info, spath).action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testTruncate() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQIp6EEG9xAAAAADVhaBw260.inf");
		CountDownLatch latch = new CountDownLatch(1);
		client.truncate(info, spath, 10).action((a, ex) -> {
			if(ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(a);
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDownload() throws InterruptedException, IOException {
		StorageServerInfo info = new StorageServerInfo("group1", "192.168.20.68", 23000);
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQIp6EEG9xAAAAADVhaBw260.inf");
		CountDownLatch latch = new CountDownLatch(1);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		client.download(info, spath, out).action((a, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(new String(out.toByteArray()));
			}
			latch.countDown();
		});
		latch.await();
	}
}
