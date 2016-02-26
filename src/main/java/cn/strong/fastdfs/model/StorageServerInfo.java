/**
 * 
 */
package cn.strong.fastdfs.model;

import java.net.InetSocketAddress;


/**
 * 存储服务器信息
 * 
 * @author liulongbiao
 *
 */
public class StorageServerInfo {

	public final String group;
	public final String host;
	public final int port;
	public final int storePathIndex;

	public StorageServerInfo(String group, String host, int port,
			int storePathIndex) {
		this.group = group;
		this.host = host;
		this.port = port;
		this.storePathIndex = storePathIndex;
	}

	public StorageServerInfo(String group, String host, int port, byte storePath) {
		this(group, host, port, storePath < 0 ? 256 + storePath : storePath);
	}

	public StorageServerInfo(String group, String host, int port) {
		this(group, host, port, 0);
	}

	@Override
	public String toString() {
		return '[' + group + ']' + host + ':' + port + '/' + storePathIndex;
	}

	/**
	 * 获取通信地址
	 * 
	 * @return
	 */
	public InetSocketAddress getAddress() {
		return new InetSocketAddress(host, port);
	}
}
