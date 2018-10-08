package us.codecraft.webmagic.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author code4crafter@gmail.com
 * Date: 17/3/11
 * Time: 10:36
 * @since 0.6.2
 */
public abstract class CharsetUtils {

    private static final String TAG = "CharsetUtils";
    public static final String ERROR_TYPE = "xxx";

    public static String gotCharsetFromHtml(byte[] buff) throws IOException {
        ByteArrayInputStream bips = new ByteArrayInputStream(buff);
        BufferedReader br = new BufferedReader(new InputStreamReader(bips));
        String s = "";
        String defaultCharSet = ERROR_TYPE;
        Pattern meta = Pattern.compile("charset=\".*\"");
        while ((s = br.readLine()) != null) {
            //<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
            if(s.contains("text/html; charset=")) {
                Pattern p1 = Pattern.compile("charset=[a-zA-Z0-9]+");
                Matcher m2 = p1.matcher(s);
                if (m2.find()) {
                    defaultCharSet = m2.group();
                    defaultCharSet = defaultCharSet.substring(8, defaultCharSet.length());
                    break;
                }
            }

            //<meta charset="UTF-8"> <meta charset="UTF-8" />
            if (s.contains("<meta charset=")) {
                Matcher m = meta.matcher(s);
                if (m.find()) {
                    //charset="UTF-8"
                    s = m.group();
                }

                Pattern p = Pattern.compile("\"[a-zA-Z0-9]+");

                Matcher m1 = p.matcher(s);
                if (m1.find()) {
                    defaultCharSet = m1.group();
                    defaultCharSet = defaultCharSet.substring(1, defaultCharSet.length());
                }
                break;
            }

            if (s.contains("</head>")) {
                //</head>
                break;
            }
        }

        br.close();
        bips.close();
        return defaultCharSet;
    }

    public static String detectCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset;
        // charset
        // 1、encoding in http header Content-Type
        charset = UrlUtils.getCharset(contentType);
        if (StringUtils.isNotBlank(contentType) && StringUtils.isNotBlank(charset)) {
            LogPrinter.d(TAG, "Auto get charset: {" + charset + "}");
            return charset;
        }
        // use default charset to decode first time
        Charset defaultCharset = Charset.defaultCharset();
        String content = new String(contentBytes, defaultCharset);
        // 2、charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    charset = metaContent.split("=")[1];
                    break;
                }
                // 2.2、html5 <meta charset="UTF-8" />
                else if (StringUtils.isNotEmpty(metaCharset)) {
                    charset = metaCharset;
                    break;
                }
            }
        }
        LogPrinter.d(TAG, "Auto get charset: {" + charset + "}");
        // 3、todo use tools as cpdetector for content decode
        return charset;
    }

}
