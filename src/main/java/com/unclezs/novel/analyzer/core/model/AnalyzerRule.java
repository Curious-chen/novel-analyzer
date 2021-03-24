package com.unclezs.novel.analyzer.core.model;

import com.unclezs.novel.analyzer.model.Verifiable;
import com.unclezs.novel.analyzer.util.SerializationUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 解析规则
 *
 * @author blog.unclezs.com
 * @date 2020/12/20 6:44 下午
 */
@Data
public class AnalyzerRule implements Serializable, Verifiable {
    private static final long serialVersionUID = -5141682318667696007L;
    /**
     * 网址名称
     */
    private String name;
    /**
     * 网站域名
     */
    private String site;
    /**
     * 分组
     */
    private String group;
    /**
     * 启用
     */
    private boolean enable = true;
    /**
     * 权重(越高优先被搜索)
     */
    private int weight = 0;
    /**
     * 正文
     */
    private ContentRule content = new ContentRule();
    /**
     * 目录规则
     */
    private TocRule toc = new TocRule();
    /**
     * 小说详情信息规则，针对目录页面获取详情
     */
    private DetailRule detail = new DetailRule();
    /**
     * 搜索规则
     */
    private SearchRule search = new SearchRule();

    @Override
    public boolean isEffective() {
        return site != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnalyzerRule that = (AnalyzerRule) o;
        return Objects.equals(site, that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site);
    }

    /**
     * 深克隆
     *
     * @return 规则克隆对象
     */
    public AnalyzerRule copy() {
        return SerializationUtils.deepClone(this);
    }
}