/**
 * 
 */
package cn.strong.fastdfs.response;

import static cn.strong.fastdfs.core.Consts.FDFS_FIELD_SEPERATOR;
import static cn.strong.fastdfs.core.Consts.FDFS_RECORD_SEPERATOR;
import static cn.strong.fastdfs.util.Helper.readString;

import cn.strong.fastdfs.model.Metadata;
import io.netty.buffer.ByteBuf;

/**
 * 文件属性解码器
 * 
 * @author liulongbiao
 *
 */
public enum MetadataDecoder implements ResponseDecoder<Metadata> {
	INSTANCE;

	@Override
	public Metadata decode(ByteBuf buf) {
		Metadata result = new Metadata();
		String content = readString(buf);
		String[] pairs = content.split(FDFS_RECORD_SEPERATOR);
		for (String pair : pairs) {
			String[] kv = pair.split(FDFS_FIELD_SEPERATOR, 2);
			if (kv.length == 2) {
				result.append(kv[0], kv[1]);
			}
		}
		return result;
	}

}
