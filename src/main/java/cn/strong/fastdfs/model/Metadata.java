/**
 * 
 */
package cn.strong.fastdfs.model;

import static cn.strong.fastdfs.core.Consts.FDFS_FIELD_SEPERATOR;
import static cn.strong.fastdfs.core.Consts.FDFS_RECORD_SEPERATOR;

import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件元数据
 * 
 * @author liulongbiao
 *
 */
public class Metadata {

	final Map<String, String> map;

	public Metadata() {
		this.map = new LinkedHashMap<>();
	}

	public Metadata append(String key, String value) {
		map.put(key, value);
		return this;
	}

	public byte[] toBytes(Charset charset) {
		if (map.isEmpty()) {
			return new byte[0];
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			if (!first) {
				sb.append(FDFS_RECORD_SEPERATOR);
			}
			sb.append(entry.getKey());
			sb.append(FDFS_FIELD_SEPERATOR);
			sb.append(entry.getValue());
			first = false;
		}
		return sb.toString().getBytes(charset);
	}

}
