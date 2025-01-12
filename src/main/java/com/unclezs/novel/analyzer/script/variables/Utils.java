package com.unclezs.novel.analyzer.script.variables;

import com.unclezs.novel.analyzer.core.helper.DebugHelper;
import com.unclezs.novel.analyzer.core.matcher.Matchers;
import com.unclezs.novel.analyzer.core.rule.CommonRule;
import com.unclezs.novel.analyzer.request.Http;
import com.unclezs.novel.analyzer.request.HttpMethod;
import com.unclezs.novel.analyzer.request.MediaType;
import com.unclezs.novel.analyzer.request.RequestParams;
import com.unclezs.novel.analyzer.util.GsonUtils;
import com.unclezs.novel.analyzer.util.StringUtils;
import com.unclezs.novel.analyzer.util.uri.UrlUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * 提供一些Js调用的java方法
 *
 * @author blog.unclezs.com
 * @since 2021/1/28 22:29
 */
@Slf4j
public class Utils implements ScriptGlobalVariables<Utils> {
  public static final String NAME = "utils";

  @Override
  public String getVariableName() {
    return NAME;
  }

  /**
   * 输出调试日志
   *
   * @param msg 日志信息
   */
  public void log(String msg) {
    DebugHelper.debug(msg);
  }

  /**
   * get请求
   *
   * @param url 链接
   * @return 结果
   */
  public String get(String url) {
    return Http.get(url);
  }

  /**
   * get请求
   *
   * @param url 链接
   * @return 结果
   */
  public String post(String url, String body) throws IOException {
    RequestParams params = RequestParams.builder()
      .url(url)
      .method(HttpMethod.POST.name())
      .body(body == null ? StringUtils.EMPTY : body)
      .mediaType(MediaType.JSON.getMediaType())
      .build();
    return Http.content(params);
  }

  /**
   * HTTP请求 支持各种请求方法
   *
   * @param paramsJson 请求数据
   * @return 结果
   */
  public String request(String paramsJson) throws IOException {
    RequestParams params = GsonUtils.parse(paramsJson, RequestParams.class);
    if (params == null || StringUtils.isEmpty(params.getUrl())) {
      log.trace("请求解析失败, 参数不合法: {}.", params);
      return null;
    }
    return Http.content(params);
  }

  /**
   * HTTP请求 支持各种请求方法
   *
   * @param paramsJson 请求数据
   * @return 返回byte[] 数据
   */
  public byte[] bytes(String paramsJson) throws IOException {
    RequestParams params = GsonUtils.parse(paramsJson, RequestParams.class);
    if (params == null || StringUtils.isEmpty(params.getUrl())) {
      log.trace("请求解析失败, 参数不合法: {}.", params);
      return null;
    }
    return Http.bytes(params);
  }

  /**
   * 转化为json（rhino 在带有java对象的变量用JSON.stringify会出现Stackoverflow）
   * <p>
   * https://github.com/mozilla/rhino/issues/578
   *
   * @param obj 对象
   * @return 结果
   */
  public String toJson(Object obj) {
    return GsonUtils.toJson(obj);
  }

  /**
   * 匹配一个
   * eg: xpath://xxx
   *
   * @param src          源
   * @param withTypeRule 带类型的规则
   * @return 匹配结果
   */
  public String match(String src, String withTypeRule) {
    return Matchers.match(src, withTypeRule);
  }

  /**
   * 匹配列表
   * eg: xpath://xxx
   *
   * @param src          源
   * @param withTypeRule 带类型的规则
   * @return 匹配结果列表
   */
  public List<Object> matchList(String src, String withTypeRule) {
    return Matchers.matchList(src, CommonRule.create(withTypeRule));
  }

  /**
   * 获取完整URL
   *
   * @param baseUrl      基准URL
   * @param relativePath 相对URL
   * @return 完整URL
   */
  public String absUrl(String baseUrl, String relativePath) {
    return UrlUtils.completeUrl(baseUrl, relativePath);
  }
}
