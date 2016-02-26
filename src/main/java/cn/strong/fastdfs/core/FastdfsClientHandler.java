/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.pool.ChannelPool;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.util.List;

import cn.strong.fastdfs.request.Sender;
import cn.strong.fastdfs.response.Receiver;

/**
 * Fastdfs 协议处理器
 * 
 * @author liulongbiao
 *
 */
public class FastdfsClientHandler extends ByteToMessageDecoder {

	private Operation<?> operation;

	public void protocol(Operation<?> operation) {
		this.operation = operation;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (operation != null) {
			operation.receive(in);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (operation != null) {
			operation.setError(cause);
		}
		ctx.close();
	}

	public static final class Operation<T> {

		public final ChannelPool pool;
		public final Channel channel;
		public final Sender sender;
		public final Receiver<T> receiver;

		public final Promise<T> promise;

		public Operation(ChannelPool pool, Channel channel, Sender sender, Receiver<T> receiver) {
			super();
			this.pool = pool;
			this.channel = channel;
			this.sender = sender;
			this.receiver = receiver;
			this.promise = new DefaultPromise<>(channel.eventLoop());
			this.promise.addListener(f -> {
				pool.release(channel);
			});
		}

		public void execute() {
			channel.pipeline().get(FastdfsClientHandler.class).protocol(this);
			try {
				sender.send(channel);
			} catch (Exception e) {
				setError(e);
			}
		}

		public void receive(ByteBuf in) {
			try {
				receiver.receive(in, promise);
			} catch (Exception e) {
				setError(e);
			}
		}

		public void setError(Throwable cause) {
			promise.setFailure(cause);
		}
	}
}
