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

	private int maxConnPerHost = 10;
	private int connectTimeout = 10000;
	private boolean keepAlive = true;

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

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

}
