/**
 * 
 */
package cn.strong.fastdfs.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.GatheringByteChannel;
import java.util.Objects;

import cn.strong.fastdfs.core.FastdfsException;
import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.ProgressivePromise;
import io.netty.util.concurrent.Promise;

/**
 * 流接收处理器
 * 
 * @author liulongbiao
 *
 */
public class StreamReceiver extends AbstractReceiver<Void> {

	/**
	 * 新建 StreamReceiver 实例
	 * 
	 * @param out
	 * @return
	 */
	public static StreamReceiver newInstance(Object out) {
		return new StreamReceiver(newSink(out));
	}

	private static Sink newSink(Object out) {
		Objects.requireNonNull(out);
		if (out instanceof OutputStream) {
			return new OioSink((OutputStream) out);
		} else if (out instanceof GatheringByteChannel) {
			return new NioSink((GatheringByteChannel) out);
		} else {
			throw new FastdfsException("unknown sink output type " + out.getClass().getName());
		}
	}

	private final Sink sink;
	private long readed = 0;

	private StreamReceiver(Sink sink) {
		this.sink = sink;
	}

	@Override
	protected void readContent(ByteBuf in, Promise<Void> promise) {
		try {
			int before = in.readableBytes();
			sink.write(in);
			int after = in.readableBytes();
			readed += before - after;

			if (promise instanceof ProgressivePromise) {
				((ProgressivePromise<Void>) promise).tryProgress(readed, length);
			}
			if (readed >= length) {
				promise.setSuccess(null);
			}
		} catch (IOException e) {
			throw new FastdfsException("write response to output error.", e);
		}
	}

	public static interface Sink {
		void write(ByteBuf buf) throws IOException;
	}

	public static class OioSink implements Sink {
		private OutputStream out;

		public OioSink(OutputStream out) {
			this.out = out;
		}

		@Override
		public void write(ByteBuf buf) throws IOException {
			buf.readBytes(out, buf.readableBytes());
		}

	}

	public static class NioSink implements Sink {
		private GatheringByteChannel out;

		public NioSink(GatheringByteChannel out) {
			this.out = out;
		}

		@Override
		public void write(ByteBuf buf) throws IOException {
			buf.readBytes(out, buf.readableBytes());
		}

	}

}
