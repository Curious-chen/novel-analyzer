package com.unclezs.novel.analyzer.core.text.matcher;

import com.unclezs.novel.analyzer.utils.regex.ReUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 内容匹配器 - 正则 全文匹配
 *
 * @author blog.unclezs.com
 * @date 2020/12/20 8:06 下午
 */
public class RegexContentMatcher {
  /**
   * 全文正则
   */
  private static final String
      REGEX ="[pvri\\-/\"]>([^字<*][\\pP\\w\\pN\\pL\\pM" + ReUtil.CHINESE + ReUtil.UNICODE_LETTER_NUMBER + ReUtil.CHINESE_PUNCTUATION+ "]{3,}[^字\\w>]{0,2})(<br|</p|</d|<p|<!|<d|</li)";
  /**
   * 预编译
   */
  private static final Pattern CORE_PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

  private RegexContentMatcher(){}
  /**
   * 匹配正文
   *
   * @param originalText 源文本
   * @return /
   */
  public static String matching(String originalText) {
    StringBuilder content = new StringBuilder();
    Matcher m = CORE_PATTERN.matcher(originalText);
    while (m.find()) {
      String paragraph = m.group(1);
      if (!paragraph.isEmpty()) {
        content.append(paragraph).append("\r\n");
      }
    }
    return content.toString();
  }
}
