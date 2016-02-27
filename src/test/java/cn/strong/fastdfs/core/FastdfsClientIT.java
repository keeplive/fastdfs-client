/**
 * 
 */
package cn.strong.fastdfs.core;

import java.net.InetSocketAddress;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.strong.fastdfs.util.Helper;
import io.netty.util.CharsetUtil;

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
	public void test() {
		byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
		client.upload(bytes, bytes.length, "txt", null).action((path, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println("upload path: " + path.toString());
			}
		});
	}
}
