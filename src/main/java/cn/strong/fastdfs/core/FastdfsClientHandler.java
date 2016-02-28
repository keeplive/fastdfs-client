/**
 * 
 */
package cn.strong.fastdfs.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.Promise;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.strong.fastdfs.request.Sender;
import cn.strong.fastdfs.response.Receiver;

/**
 * Fastdfs 协议处理器
 * 
 * @author liulongbiao
 *
 */
public class FastdfsClientHandler extends ByteToMessageDecoder {
	private static Logger LOG = LoggerFactory.getLogger(FastdfsClientHandler.class);

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
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			throw new FastdfsTimeoutException("channel was idle for maxIdleSeconds");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (operation != null) {
			operation.setError(cause);
		} else {
			if (cause instanceof FastdfsTimeoutException) {
				LOG.info(cause.getMessage(), cause);
			} else {
				LOG.error(cause.getMessage(), cause);
			}
		}
		ctx.close();
	}

	public static final class Operation<T> {

		public final Promise<T> promise;
		public final Channel channel;
		public final Sender sender;
		public final Receiver<T> receiver;

		public Operation(Promise<T> promise, Channel channel, Sender sender, Receiver<T> receiver) {
			this.promise = promise;
			this.channel = channel;
			this.sender = sender;
			this.receiver = receiver;
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
			promise.tryFailure(cause);
		}
	}
}
