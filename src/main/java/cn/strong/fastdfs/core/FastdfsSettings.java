/**
 * 
 */
package cn.strong.fastdfs.core;


/**
 * 连接设置
 * 
 * @author liulongbiao
 *
 */
public class FastdfsSettings {

	private int eventLoopThreads = 0; // 线程数量
	private int maxConnPerHost = 10; // 每个IP最大连接数
	private int connectTimeout = 10000; // 连接超时时间(毫秒)
	private int maxIdleSeconds = 20; // 最大闲置时间(秒)

	public int getEventLoopThreads() {
		return eventLoopThreads;
	}

	public void setEventLoopThreads(int eventLoopThreads) {
		this.eventLoopThreads = eventLoopThreads;
	}

	public int getMaxConnPerHost() {
		return maxConnPerHost;
	}

	public void setMaxConnPerHost(int maxConnPerHost) {
		this.maxConnPerHost = maxConnPerHost;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getMaxIdleSeconds() {
		return maxIdleSeconds;
	}

	public void setMaxIdleSeconds(int maxIdleSeconds) {
		this.maxIdleSeconds = maxIdleSeconds;
	}

}
