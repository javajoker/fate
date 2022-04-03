package com.infoecos.cn.destiny.lib.干;

import com.infoecos.cn.destiny.lib.术数;
import com.infoecos.cn.destiny.lib.五行.五行生克关系;


/**
 * @author dch
 * 
 */
public enum 十神 {
	/**
	 * 克我者为正官、七杀，男命为职权子息，女命为夫星。
	 */
	正官, 七杀 /* 偏官 */,
	/**
	 * 生我者为正印、偏印，总称叫印绶，是母亲之谓。
	 */
	正印, 偏印,
	/**
	 * 我克者为正财、偏财，男命为父亲、妻子、财产，女命为侍夫的才智。
	 */
	正财, 偏财,
	/**
	 * 我生者为食神、伤官，男命为聪明才智，女命为子息。
	 */
	食神, 伤官,
	/**
	 * 同类者为比肩、比劫，是兄弟姊妹之谓，也是分福耗财之谓。
	 */
	比肩, 劫财 /* 比劫 */,
	/**
	 * 日主
	 */
	NA;

	private 五行生克关系 relation;
	private String 心性 = "";
	private String[] 指事 = new String[3]; // 指事, 旺, 衰

	public 五行生克关系 五行生克关系() {
		return relation;
	}

	public String 心性() {
		return 心性;
	}

	public String 指事() {
		return 指事[0];
	}

	public String 旺() {
		return 指事[1];
	}

	public String 衰() {
		return 指事[2];
	}

	static {
		正官.relation = 五行生克关系.官杀;
		七杀.relation = 五行生克关系.官杀;

		正印.relation = 五行生克关系.印绶;
		偏印.relation = 五行生克关系.印绶;

		正财.relation = 五行生克关系.妻财;
		偏财.relation = 五行生克关系.妻财;

		食神.relation = 五行生克关系.食神;
		伤官.relation = 五行生克关系.食神;

		比肩.relation = 五行生克关系.比肩;
		劫财.relation = 五行生克关系.比肩;

		正官.心性 = "正直负责，端庄严肃，循规蹈矩，但易流于刻板、墨守成规，反为意志不坚";
		七杀.心性 = "豪爽侠义，积极进取，威严机敏，但易流于偏激，叛逆霸道，反为堕落极端";
		正印.心性 = "聪颖仁慈，淡泊名利，逆来顺受，但易流于庸碌，缺乏进取，反为迟钝消极";
		偏印.心性 = "精明干练，反应机警，多才多艺，但易流于孤独，缺乏人情，反为自私冷漠";
		比肩.心性 = "稳健刚毅，冒险勇敢，积极进取，但易流于孤僻，缺乏合群，反为孤立寡合";
		劫财.心性 = "热诚坦直，坚韧志旺，奋斗不屈，但易流于盲目，缺乏理智，反为蛮横冲动";
		食神.心性 = "温文随和，待人宽厚，善良体贴，但易流于虚伪，缺乏是非，反为迂腐懦怯";
		伤官.心性 = "聪明活跃，才华横溢，逞强好胜，但易流于任性，缺乏约束，反为桀傲不驯";
		正财.心性 = "勤劳节俭，踏实保守，任劳任怨，但易流于苟且，缺乏进取，反为懦弱无能";
		偏财.心性 = "慷慨重情，聪敏机灵，乐观开朗，但易流于虚浮，缺乏节制，反为浮华风流";

		正官.指事 = new String[] { "工作职权，仕途", "升迁机会", "官位不保" };
		七杀.指事 = new String[] { "江湖地位，权力", "大权在握", "权力不保" };
		正印.指事 = new String[] { "事业", "事业有成", "事业不稳" };
		偏印.指事 = new String[] { "学业", "学业有成", "" };
		比肩.指事 = new String[] { "朋友", "得道多助", "失道寡助" };
		劫财.指事 = new String[] { "损友", "损友败财", "" };
		食神.指事 = new String[] { "技术能力，外在", "有所得", "有所失" };
		伤官.指事 = new String[] { "聪明才智，内在", "有所得", "有所失" };
		正财.指事 = new String[] { "财富（明财）", "日常收入涨", "" };
		偏财.指事 = new String[] { "财富（暗财）", "有意外暗财", "" };
	}

	public static 十神 求神(术数 host, 术数 x) {
		五行生克关系 relation = host.五行().关系(x.五行());
		if (五行生克关系.印绶.equals(relation)) {
			if (host.阴阳().equals(x.阴阳())) {
				return 偏印;
			} else {
				return 正印;
			}
		}
		if (五行生克关系.食神.equals(relation)) {
			if (host.阴阳().equals(x.阴阳())) {
				return 食神;
			} else {
				return 伤官;
			}
		}
		if (五行生克关系.官杀.equals(relation)) {
			if (host.阴阳().equals(x.阴阳())) {
				return 七杀;
			} else {
				return 正官;
			}
		}
		if (五行生克关系.妻财.equals(relation)) {
			if (host.阴阳().equals(x.阴阳())) {
				return 偏财;
			} else {
				return 正财;
			}
		}
		if (五行生克关系.比肩.equals(relation)) {
			if (host.阴阳().equals(x.阴阳())) {
				return 比肩;
			} else {
				return 劫财;
			}
		}

		return null;
	}
}
