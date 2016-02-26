/**
 * 
 */
package cn.strong.fastdfs.response;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Promise;

/**
 * 接收处理
 * 
 * @author liulongbiao
 *
 */
public interface Receiver<T> {

	void receive(ByteBuf in, Promise<T> promise);
}
