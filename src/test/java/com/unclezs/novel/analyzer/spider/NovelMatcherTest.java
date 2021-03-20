package com.unclezs.novel.analyzer.spider;

import com.unclezs.novel.analyzer.core.helper.RuleHelper;
import com.unclezs.novel.analyzer.core.model.AnalyzerRule;
import com.unclezs.novel.analyzer.core.model.TocRule;
import com.unclezs.novel.analyzer.request.RequestParams;
import com.unclezs.novel.analyzer.spider.pipline.TxtFilePipeline;
import com.unclezs.novel.analyzer.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author blog.unclezs.com
 * @date 2021/1/6 7:37
 */
public class NovelMatcherTest {
    @Before
    public void loadRule() throws IOException {
        String ruleConfigs = FileUtils.readUtf8String(NovelMatcherTest.class.getResource("/analyzer/rule.json").getFile());
        RuleHelper.loadRules(ruleConfigs);
    }

    @Test
    public void testContent() throws IOException {
        NovelSpider spider = new NovelSpider();
        AnalyzerRule rule = RuleHelper.rule("https://vipreader.qidian.com/chapter/1020842452/549224997");
        spider.setRule(rule);
        RequestParams params = RequestParams.create("https://vipreader.qidian.com/chapter/1020842452/549224997");
        params.setDynamic(true);
        params.addHeader("Cookie","_csrfToken=kvjXnYAXzq40X2lCbM4KEYuDRbk3XDdxLhzYk60w; newstatisticUUID=1616218128_966730623; qdrs=6%7C3%7C0%7C0%7C1; showSectionCommentGuide=1; qdgd=1; lrbc=1020842452%7C549224997%7C1; rcr=1020842452; bc=1020842452; ywkey=yw7X12MWXfBd; ywguid=1585503310; ywopenid=7387FAAE42281F44434B7CF4977CFD3E; pageOps=1");
        String content = spider.content(params);
        System.out.println(content);
    }

    @Test
    public void testToc() throws IOException {
        AnalyzerRule rule = RuleHelper.rule("https://m.jx.la/book/394/index_93.html");
        TocSpider tocSpider = new TocSpider(rule);
        tocSpider.setOnNewItemAddHandler(chapter -> System.out.println(chapter.getName()));
        tocSpider.toc("https://m.jx.la/book/394/index_93.html");
        while (true) {
            System.out.println(tocSpider.getNextPageUrl() + "  " + tocSpider.hasMore());
            if(tocSpider.hasMore()){
                tocSpider.loadMore();
            }else {
                return;
            }
        }
    }

    @Test
    public void testTocAll() throws IOException {
        String url = "http://m.ibiquge.net/99/99713/";
        NovelSpider novelSpider = new NovelSpider();
        AnalyzerRule rule = RuleHelper.rule(url);
        TocRule toc = new TocRule();
        toc.setFilter(true);
        rule.setToc(toc);
        novelSpider.setRule(rule);
        novelSpider.toc(url).forEach(System.out::println);
    }

    @Test
    public void testSearchEngine() throws IOException {
        AnalyzerRule rule = RuleHelper.getRule("http://www.quanben.me/modules/article/search.php");
        List<AnalyzerRule> rules = Collections.singletonList(rule);
        SearchSpider searchSpider = new SearchSpider(rules);
        searchSpider.setOnNewItemAddHandler(novel -> {
            System.out.printf("%s - %s\n", novel.getTitle(), novel.getUrl());
        });
        searchSpider.search("完美");
        while (searchSpider.hasMore()) {
            searchSpider.loadMore();
        }
    }

    @Test
    public void test() throws IOException {
        String url = "https://m.jx.la/book/394/index_93.html";
//        url = "http://www.520tingshu.com/book/book17837.html";
//        url = "http://www.baidu.com/link?url=0TR41tEQgMEPwL9j6YFGa2ZH6gBjuCwgQjMIcKoW728ZNh8jwd5eCD7DL8ez3HYr";
        Spider.create(url)
            .pipeline(new TxtFilePipeline())
            .run();
    }
}
