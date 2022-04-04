package com.infoecos.cn.destiny.lib.预测;

import com.infoecos.cn.destiny.lib.五行.五行;
import com.infoecos.cn.destiny.lib.干.十神;
import com.infoecos.cn.destiny.lib.预测.utils.DestinyUtil;
import com.infoecos.cn.destiny.lib.预测.utils.喜用神;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public enum Report {
    // 命
    五行喜忌, 性格, 家庭, 财运, 身体, 子女, 行业, 贵人, 喜忌, 建议,
    // 运
    运程分析;

    public static final String[] relation = {
            "父祖荫", "母祖荫",
            "父兄姐", "母弟妹",
            "命主", "对象",
            "长子", "次子"};
    public static final String[] body = {
            "头", "脖子",
            "胸部", "腹部",
            "小腹", "屁股",
            "大腿", "小腿脚部"};
    public static int[] ages = {
            9, 18,
            27, 36,
            -1, 45,
            54, 100};


    static {
        五行喜忌.setReport(五行喜忌.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                盘 pan = destiny.盘();
                喜用神 useful = DestinyUtil.用神(pan);
                return String.format("\t本命: %s\t喜用神：%s\t忌神：%s", pan.日主().五行(), Arrays.toString(useful.用神集()), useful.忌神());
            }
        });
        性格.setReport(性格.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                盘 pan = destiny.盘();
                StringBuilder sb = new StringBuilder();
                五行[] e = DestinyUtil.sort5Ele(pan);
                sb.append(e[4].外貌(true));
                十神[][] character = DestinyUtil.十神心性(pan);
                for (十神 s : character[1]) {
                    sb.append(String.format("%s; ", s.心性(true)));
                }
                for (十神 s : character[0]) {
                    sb.append(String.format("%s; ", s.心性(false)));
                }
                sb.append(e[4].人事(true));
                sb.append(e[0].人事(false));
                return sb.toString();
            }
        });
        家庭.setReport(家庭.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        财运.setReport(财运.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        身体.setReport(身体.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        子女.setReport(子女.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        行业.setReport(行业.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        贵人.setReport(贵人.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        喜忌.setReport(喜忌.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        建议.setReport(建议.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                return "";
            }
        });
        运程分析.setReport(运程分析.new report() {
            @Override
            String generateReport(八字排盘 destiny) {
                StringBuilder sb = new StringBuilder();
                DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                for (运 fate : DestinyUtil.推运(destiny)) {
                    sb.append("\r\n");
                    sb.append(String.format("\t[%s ~ %s]",
                            df.format(fate.getTimespan()[0]), df.format(fate.getTimespan()[1])));
                    for (十神 f : fate.getFate().keySet())
                        sb.append(String.format("\t%s: %f;", f, fate.getFate().get(f)));
                    for (int r : fate.getRelation().keySet())
                        sb.append(String.format("\t%s: %f;", relation[r], fate.getRelation().get(r)));
                }
                return sb.toString();
            }
        });
    }

    private report report = null;

    void setReport(report report) {
        this.report = report;
    }

    public String toString(八字排盘 destiny) {
        return String.format("%s: %s", name(), report != null ? report.generateReport(destiny) : "N/A");
    }

    abstract class report {
        abstract String generateReport(八字排盘 destiny);
    }
}
