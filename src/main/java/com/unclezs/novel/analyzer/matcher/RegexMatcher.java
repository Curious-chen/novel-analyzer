package com.unclezs.novel.analyzer.matcher;

import com.unclezs.novel.analyzer.model.Pair;
import com.unclezs.novel.analyzer.utils.regex.RegexUtil;
import lombok.experimental.UtilityClass;

/**
 * @author zhanghongguo@sensorsdata.cn
 * @since 2020/12/21 11:39
 */
@UtilityClass
public class RegexMatcher {
    /**
     * 分组模板分隔符
     */
    public static final String REGEX_TEMPLATE_DELIMITER = "@@";

    /**
     * 正则匹配
     *
     * @param src   源
     * @param regex 正则
     * @param index 组
     * @return /
     */
    public String matcher(String src, String regex, int index) {
        String ret = RegexUtil.get(regex, src, index);
        return ret == null ? "" : ret;
    }

    /**
     * 正则匹配
     * <p>
     * 从src中匹配出多个值并根据regex生成新的字符串
     * 匹配结束后会删除匹配内容之前的内容（包括匹配内容）
     * 例如：<br>
     * src 2013年5月 regex (.*?)年(.*?)月@@$1-$2 得到 2013-5
     *
     * @param src   源
     * @param regex 正则
     * @return /
     */
    public String matcher(String src, String regex) {
        Pair<String, String> pair = getTemplate(regex);
        String ret = RegexUtil.extractMulti(pair.getLeft(), src, pair.getRight());
        return ret == null ? "" : ret;
    }

    /**
     * 获取正则模板 left：正则 right：模板 $1:$2
     *
     * @param regex 正则
     * @return /
     */
    private Pair<String, String> getTemplate(String regex) {
        String[] ret = regex.split(REGEX_TEMPLATE_DELIMITER);
        Pair<String, String> pair = Pair.of(ret[0], "$0");
        if (ret.length > 1) {
            pair.setRight(ret[1]);
        }
        return pair;
    }
}
