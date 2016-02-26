/**
 * 
 */
package cn.strong.fastdfs.response;

import static cn.strong.fastdfs.core.Consts.FDFS_FIELD_SEPERATOR;
import static cn.strong.fastdfs.core.Consts.FDFS_RECORD_SEPERATOR;
import static cn.strong.fastdfs.util.Helper.readString;

import java.util.LinkedHashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;

/**
 * 文件属性解码器
 * 
 * @author liulongbiao
 *
 */
public enum MetadataDecoder implements ResponseDecoder<Map<String, String>> {
	INSTANCE;

	@Override
	public Map<String, String> decode(ByteBuf buf) {
		Map<String, String> result = new LinkedHashMap<String, String>();
		String content = readString(buf);
		String[] pairs = content.split(FDFS_RECORD_SEPERATOR);
		for (String pair : pairs) {
			String[] kv = pair.split(FDFS_FIELD_SEPERATOR, 2);
			if (kv.length == 2) {
				result.put(kv[0], kv[1]);
			}
		}
		return result;
	}

}
