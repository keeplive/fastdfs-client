/**
 * 
 */
package cn.strong.fastdfs.core;

import java.net.InetSocketAddress;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Fastdfs 连接池组
 * 
 * @author liulongbiao
 *
 */
public class FastdfsChannelPoolMap extends
		AbstractChannelPoolMap<InetSocketAddress, FixedChannelPool> {

	private static Logger LOG = LoggerFactory.getLogger(FastdfsChannelPoolMap.class);

	private EventLoopGroup group;
	private FastdfsSettings settings;

	public FastdfsChannelPoolMap(EventLoopGroup group, FastdfsSettings settings) {
		this.group = Objects.requireNonNull(group);
		this.settings = Objects.requireNonNull(settings);
	}

	public void setGroup(EventLoopGroup group) {
		this.group = group;
	}

	public void setSettings(FastdfsSettings settings) {
		this.settings = settings;
	}

	@Override
	protected FixedChannelPool newPool(InetSocketAddress key) {
		Bootstrap b = new Bootstrap().channel(NioSocketChannel.class).group(group);
		b.remoteAddress(key);
		b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, settings.getConnectTimeout());
		b.option(ChannelOption.TCP_NODELAY, true);
		b.option(ChannelOption.SO_KEEPALIVE, settings.isKeepAlive());
		return new FixedChannelPool(b, FastdfsChannelPoolHandler.INSTANCE,
				settings.getMaxConnPerHost());
	}

	private static enum FastdfsChannelPoolHandler implements ChannelPoolHandler {
		INSTANCE;

		public void channelReleased(Channel ch) throws Exception {
			if (LOG.isDebugEnabled()) {
				LOG.debug("channel released : {}", ch.toString());
			}
			ch.pipeline().get(FastdfsClientHandler.class).protocol(null);
		}

		public void channelAcquired(Channel ch) throws Exception {
			if (LOG.isDebugEnabled()) {
				LOG.debug("channel acquired : {}", ch.toString());
			}
		}

		public void channelCreated(Channel ch) throws Exception {
			if (LOG.isInfoEnabled()) {
				LOG.info("channel created : {}", ch.toString());
			}
			ch.pipeline().addLast(new ChunkedWriteHandler()).addLast(new FastdfsClientHandler());
		}

	}
}
