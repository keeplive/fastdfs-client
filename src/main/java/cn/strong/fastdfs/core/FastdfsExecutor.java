/**
 * 
 */
package cn.strong.fastdfs.core;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import cn.strong.fastdfs.core.FastdfsClientHandler.Operation;
import cn.strong.fastdfs.request.Sender;
import cn.strong.fastdfs.response.DefaultReciver;
import cn.strong.fastdfs.response.Receiver;
import cn.strong.fastdfs.response.ResponseDecoder;
import cn.strong.fastdfs.response.StreamReceiver;
import cn.strong.fastdfs.util.AsyncAction;
import cn.strong.fastdfs.util.AsyncActions;
import cn.strong.fastdfs.util.ProgressiveFutureAsyncAction;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;

/**
 * FastdfsExecutor
 * 
 * @author liulongbiao
 *
 */
public class FastdfsExecutor implements Closeable {

	private NioEventLoopGroup group;
	private FastdfsChannelPoolMap poolMap;

	public FastdfsExecutor(FastdfsSettings settings) {
		if (settings == null) {
			settings = new FastdfsSettings();
		}
		group = new NioEventLoopGroup(settings.getEventLoopThreads());
		poolMap = new FastdfsChannelPoolMap(group, settings);
	}

	/**
	 * 访问 Fastdfs 服务器
	 * 
	 * @param addr
	 * @param sender
	 * @param decoder
	 * @return
	 */
	public <T> AsyncAction<T> execute(InetSocketAddress addr, Sender sender, ResponseDecoder<T> decoder) {
		Promise<T> promise = group.next().newPromise();
		execute(addr, sender, new DefaultReciver<>(decoder), promise);
		return AsyncActions.from(promise);
	}

	public <T> void execute(InetSocketAddress addr, Sender sender, Receiver<T> receiver, Promise<T> promise) {
		FixedChannelPool pool = poolMap.get(addr);
		pool.acquire().addListener(new PoolChannelFutureListener<T>(promise, pool, sender, receiver));
	}

	/**
	 * 访问 Fastdfs 服务器，并获取流式结果
	 * 
	 * @param addr
	 * @param sender
	 * @param receiver
	 * @return
	 */
	public ProgressiveFutureAsyncAction<Void> execute(InetSocketAddress addr, Sender sender, StreamReceiver receiver) {
		ProgressivePromise<Void> promise = group.next().newProgressivePromise();
		execute(addr, sender, receiver, promise);
		return AsyncActions.from(promise);
	}

	private static class PoolChannelFutureListener<T> implements FutureListener<Channel> {

		final Promise<T> promise;
		final FixedChannelPool pool;
		final Sender sender;
		final Receiver<T> receiver;

		public PoolChannelFutureListener(Promise<T> promise, FixedChannelPool pool, Sender sender,
				Receiver<T> receiver) {
			this.promise = promise;
			this.pool = pool;
			this.sender = sender;
			this.receiver = receiver;
		}

		@Override
		public void operationComplete(Future<Channel> future) throws Exception {
			if (future.isCancelled()) {
				promise.tryFailure(new FastdfsException("connection is canceled"));
			} else if (!future.isSuccess()) {
				promise.tryFailure(future.cause());
			} else {
				Channel ch = future.getNow();
				promise.addListener(f -> {
					pool.release(ch);
				});
				try {
					Operation<T> operation = new Operation<T>(promise, ch, sender, receiver);
					operation.execute();
				} catch (Exception e) {
					promise.tryFailure(e);
				}
			}
		}

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
