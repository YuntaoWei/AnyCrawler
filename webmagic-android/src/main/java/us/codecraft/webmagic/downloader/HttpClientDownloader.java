package us.codecraft.webmagic.downloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.ProxyProvider;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;
import us.codecraft.webmagic.utils.LogPrinter;


/**
 * The http downloader based on HttpClient.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class HttpClientDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, OkHttpClient> httpClients = new HashMap<String, OkHttpClient>();

    private OkhttpClientGenerator httpClientGenerator = new OkhttpClientGenerator(5);

    private ProxyProvider proxyProvider;

    private boolean responseHeader = true;

    public void setProxyProvider(ProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    private OkHttpClient getHttpClient(Site site, Task t) {
        if (site == null) {
            return httpClientGenerator.getClient(null, proxyProvider == null ? null : proxyProvider.getProxy(t));
        }
        String domain = site.getDomain();
        OkHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site, proxyProvider == null ? null : proxyProvider.getProxy(t));
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    @Override
    public Page download(Request request, Task task) {
        Page page = Page.fail();
        OkHttpClient httpClient = getHttpClient(task.getSite(), task);
        Call c = httpClient.newCall(createRequest(task.getSite(), request.getUrl()));
        Response rsp = null;
        try {
            rsp = c.execute();
            byte[] body = rsp.body().bytes();
            String charset = CharsetUtils.gotCharsetFromHtml(body);
            if(charset.equals(CharsetUtils.ERROR_TYPE)) {
                charset = CharsetUtils.detectCharset(rsp.body().contentType().type(), body);
            }

            String data = new String(body, charset);
            page = handleResponse(request, rsp, charset, data);
            rsp.body().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    protected Page handleResponse(Request request, Response rsp, String charset, String rawData) {
        Page page = new Page();
        if (!request.isBinaryContent()) {
            page.setCharset(charset);
            page.setRawText(rawData);
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(rsp.code());
        page.setDownloadSuccess(true);
        if (responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(rsp.headers()));
        }
        return page;
    }

    private String downloadTest(final String url) {
        OkHttpClient client = new OkHttpClient();
        String data = null;
        okhttp3.Request.Builder builder1 = new okhttp3.Request.Builder()
                .get()
                .url(url)
                .header("Content-Type", "text/html; gb2312");
        Call call = client.newCall(builder1.build());
        try {
            Response rsp = call.execute();
            data = new String(rsp.body().bytes(), "gb2312");
            LogPrinter.i("ttt", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private okhttp3.Request createRequest(Site site, String url) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .get()
                .header("Content-Type", "text/html; gb2312")
                .url(url);
        /*if (site.getUserAgent() != null) {
            builder.header("User-Agent", site.getUserAgent());
        }

        if (site.isUseGzip()) {
            builder.header("Accept-Encoding", "gzip");
        }*/
        return builder.build();
    }

    @Override
    public void setThread(int thread) {
        httpClientGenerator.setPoolSize(thread);
    }

    protected Page handleResponse(Request request, String charset, Response httpResponse) throws IOException {
        byte[] bytes = httpResponse.body().bytes();
        String contentType = httpResponse.body().contentType().type() == null ? "" : httpResponse.body().contentType().toString();
        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()) {
            if (charset == null) {
                charset = getHtmlCharset(contentType, bytes);
            }
            page.setCharset(charset);
            String data = new String(bytes, charset);
            LogPrinter.i("ttt", "got data : " + data);
            page.setRawText(data);
        }
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.code());
        page.setDownloadSuccess(true);
        if (responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.headers()));
        }
        return page;
    }

    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            logger.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }
        return charset;
    }
}
