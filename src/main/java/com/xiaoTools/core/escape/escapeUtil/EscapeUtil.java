package com.xiaoTools.core.escape.escapeUtil;

import com.xiaoTools.core.escape.filter.Filter;
import com.xiaoTools.util.strUtil.StrUtil;

/**
 * [转义工具类](Escape tool class)
 * @description: zh - 转义工具类
 * @description: en - Escape tool class
 * @version: V1.0
 * @author XiaoXunYao
 * @since 2021/6/8 9:28 上午
*/
public class EscapeUtil {

    /**
     * [设置不转义的字符](Sets characters that are not escaped)
     */
    private static final String NOT_ESCAPE_CHARS = "*@-_+./";

    /**
     * [JS转换过滤器](JS conversion filter)
     */
    private static final Filter<Character> JS_ESCAPE_FILTER = c -> !(
            Character.isDigit(c)
                    || Character.isLowerCase(c)
                    || Character.isUpperCase(c)
                    || StrUtil.contains(NOT_ESCAPE_CHARS, c)
    );

    /**
     * [ Escape编码（Unicode）]( Escape encoding (Unicode) )
     * @description: zh - Escape编码（Unicode）
     * @description: en - Escape encoding (Unicode)
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 10:27 上午
     * @param content: [被转义的内容](Escaped content)
     * @return java.lang.String
    */
    public static String escape(CharSequence content){
        return escape(content, JS_ESCAPE_FILTER);
    }

    /**
     * [Escape编码（Unicode）]( Escape encoding (Unicode) )
     * @description: zh - 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换。
     * @description: en - This method does not encode ASCII letters and numbers. All other characters are replaced by escape sequences.
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 10:30 上午
     * @param content: [被转义的内容](Escaped content)
     * @return java.lang.String
    */
    public static String escapeAll(CharSequence content) {
        return escape(content, c -> true);
    }

    /**
     * [Escape编码（Unicode）](Escape encoding (Unicode))
     * @description: zh - 该方法不会对 ASCII 字母和数字进行编码。其他所有的字符都会被转义序列替换
     * @description: en - This method does not encode ASCII letters and numbers. All other characters are replaced by escape sequences
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 10:22 上午
     * @param content: [被转义的内容](Escaped content)
     * @param filter: [编码过滤器，对于过滤器中accept为false的字符不做编码](Encoding filter, do not encode the character whose accept is false in the filter)
     * @return java.lang.String
    */
    public static String escape(CharSequence content, Filter<Character> filter){
        //判断字符串是否为空
        if (StrUtil.isEmpty(content)) { return StrUtil.str(content); }
        final StringBuilder tmp = new StringBuilder(content.length() * 6);
        char j;
        for (int i = 0; i < content.length(); i++) {
            j = content.charAt(i);
            if (!filter.accept(j)) {
                tmp.append(j);
            } else if (j < 256) {
                tmp.append("%");
                if (j < 16) { tmp.append("0"); }
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * [安全的unescape文本，当文本不是被escape的时候，返回原文](Safe unescape text, when the text is not escape, return the original text)
     * @description: zh - 安全的unescape文本，当文本不是被escape的时候，返回原文
     * @description: en - Safe unescape text, when the text is not escape, return the original text
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 10:38 上午
     * @param content: [被编义的内容](The content of the paraphrase)
     * @return java.lang.String
    */
    public static String safeUnescape(String content) {
        try {
            return unescape(content);
        } catch (Exception ignored) { }
        return content;
    }

    /**
     * [Escape解码](Escape decoding)
     * @description: zh - Escape解码
     * @description: en - Escape decoding
     * @version: V1.0
     * @author XiaoXunYao
     * @since 2021/6/8 10:37 上午
     * @param content: [被转义的内容](Escaped content)
     * @return java.lang.String
    */
    public static String unescape(String content){
        if (StrUtil.isBlank(content)) { return content; }
        StringBuilder tmp = new StringBuilder(content.length());
        int lastPos = 0;
        int pos;
        char ch;
        while (lastPos < content.length()) {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                } else {
                    tmp.append(content, lastPos, pos);
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }


}
