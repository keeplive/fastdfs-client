/**
 * 
 */
package cn.strong.fastdfs.core;

import static cn.strong.fastdfs.util.Helper.execAsync;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import cn.strong.fastdfs.core.FastdfsClientHandler.Operation;
import cn.strong.fastdfs.request.Sender;
import cn.strong.fastdfs.response.Receiver;
import cn.strong.fastdfs.util.Callback;

/**
 * FastdfsSession 连接
 * 
 * @author liulongbiao
 *
 */
public class FastdfsExecutor implements Closeable {

	private EventLoopGroup group;
	private FastdfsChannelPoolMap poolMap;

	private FastdfsSettings settings;

	public void setSettings(FastdfsSettings settings) {
		this.settings = settings;
	}

	@PostConstruct
	public void afterPropertiesSet() {
		if (settings == null) {
			settings = new FastdfsSettings();
		}
		group = new NioEventLoopGroup();
		poolMap = new FastdfsChannelPoolMap(group, settings);
	}

	public <T> void exec(InetSocketAddress addr, Sender sender, Receiver<T> receiver,
			Callback<T> callback) {
		FixedChannelPool pool = poolMap.get(addr);
		execAsync(pool.acquire(), (r, ex) -> {
			if (ex != null) {
				callback.onComplete(null, new FastdfsConnectionException("connect to host "
						+ addr.toString() + " error", ex));
			} else {
				try {
					Operation<T> operation = new Operation<T>(pool, r, sender, receiver);
					operation.execute();
					execAsync(operation.promise, callback);
				} catch (Exception e) {
					callback.onComplete(null, e);
				}
			}
		});
	}

	@PreDestroy
	public void close() throws IOException {
		if (poolMap != null) {
			try {
				poolMap.close();
			} catch (Exception e) {
				// ignore
			}
		}
		if (group != null) {
			group.shutdownGracefully();
		}
	}
}
