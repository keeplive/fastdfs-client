/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cn.strong.fastdfs.model.StoragePath;
import cn.strong.fastdfs.util.Helper;
import cn.strong.fastdfs.util.ProgressCallback;

/**
 * @author liulongbiao
 *
 */
public class FastdfsClientIT {

	private FastdfsExecutor executor;
	private FastdfsClient client;

	@Before
	public void setup() {
		executor = new FastdfsExecutor(new FastdfsSettings());
		client = new FastdfsClient(executor, Arrays.asList(new InetSocketAddress("192.168.20.68", 22122)));
	}

	@After
	public void destroy() {
		Helper.closeQuietly(executor);
	}

	@Test
	@Ignore
	public void testUpload() throws InterruptedException {
		byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
		CountDownLatch latch = new CountDownLatch(1);
		client.upload(bytes, bytes.length, "txt", null).action((path, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("upload path: " + path.toString());
			}
			latch.countDown();
		});
		latch.await();
	}

	@Test
	@Ignore
	public void testDownload() throws InterruptedException, IOException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FF/wKgURFbQHj6AC0PlAAAn-DDcYN8543.mp3");
		File file = new File("d:\\test.mp3");
		final RandomAccessFile raf = new RandomAccessFile(file, "rw");
		final FileChannel channel = FileChannel.open(file.toPath(),
				StandardOpenOption.WRITE);

		CountDownLatch latch = new CountDownLatch(1);
		client.download(spath, channel).action(new ProgressCallback<Void>() {

			@Override
			public void call(Void result, Throwable ex) {
				if (ex != null) {
					ex.printStackTrace();
				} else {
					System.out.println("download finished ");
				}
				Helper.closeQuietly(channel);
				Helper.closeQuietly(raf);
				latch.countDown();
			}

			@Override
			public void onProgress(long progress, long total) {
				int MAX = 20;
				int loaded = (int) (MAX * progress / total);
				StringBuilder sb = new StringBuilder();
				appendRepeat(sb, '#', loaded);
				appendRepeat(sb, '-', MAX - loaded);
				sb.append(' ');
				sb.append(progress);
				sb.append('/');
				sb.append(total);
				System.out.println(sb.toString());
			}
		});
		latch.await();
	}

	private static void appendRepeat(StringBuilder sb, char ch, int count) {
		for (int i = 0; i < count; i++) {
			sb.append(ch);
		}
	}
}
