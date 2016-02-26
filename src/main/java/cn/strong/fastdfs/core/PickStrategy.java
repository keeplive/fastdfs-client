package cn.strong.fastdfs.core;

import java.util.List;
import java.util.Random;

/**
 * 种子挑选策略
 * 
 * @author liulongbiao
 *
 */
public enum PickStrategy {

	ROUND_ROBIN {
		private int idx;

		@Override
		public <T> T pick(List<T> list) {
			idx %= list.size();
			return list.get(idx++);
		}

	},
	RANDOM {
		private final Random RDM = new Random();

		@Override
		public <T> T pick(List<T> list) {
			return list.get(RDM.nextInt(list.size()));
		}

	},
	FIRST {

		@Override
		<T> T pick(List<T> list) {
			return list.get(0);
		}

	};

	abstract <T> T pick(List<T> list);
}