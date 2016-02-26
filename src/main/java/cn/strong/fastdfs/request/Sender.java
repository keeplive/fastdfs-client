/**
 * 
 */
package cn.strong.fastdfs.request;

import io.netty.channel.Channel;

/**
 * 发送器
 * 
 * @author liulongbiao
 *
 */
public interface Sender {

	void send(Channel ch);
}
