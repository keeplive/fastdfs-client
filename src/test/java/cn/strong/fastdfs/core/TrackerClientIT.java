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
		client.getUploadStorage("group1").action((info, ex) -> {
			if(ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(info);
			}
		});
	}

	@Test
	@Ignore
	public void testGetDownloadStorage() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		client.getDownloadStorage(spath).action((info, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				System.out.println(info);
			}
		});
	}

	@Test
	@Ignore
	public void testFindDownloadStorages() throws InterruptedException {
		StoragePath spath = StoragePath
				.fromFullPath("group1/M00/09/FE/wKgURFbQBVSAcFjdAAAADTVhaBw216.inf");
		client.findDownloadStorages(spath).action((infos, ex) -> {
			if (ex != null) {
				ex.printStackTrace();
			} else {
				infos.forEach(info -> {
					System.out.println(info);
				});
			}
		});
	}

}
