package us.codecraft.webmagic.downloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.codecraft.webmagic.Site;

public class OkhttpClientGenerator {

    private ConnectionPool connectionPool;

    public OkhttpClientGenerator(int maxConnectionSize) {
        connectionPool = new ConnectionPool(maxConnectionSize, 5, TimeUnit.MINUTES);
    }

    public void setPoolSize(int size) {
    }

    public OkHttpClient getClient(Site site, us.codecraft.webmagic.proxy.Proxy proxy) {
        return generateClient(site, proxy);
    }

    private OkHttpClient generateClient(final Site site, us.codecraft.webmagic.proxy.Proxy proxy) {
        /*if(true) {
            return new OkHttpClient();
        }*/

        if (site == null)
            throw new IllegalArgumentException("Site must not be null!");
        //create client
        final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
        OkHttpClient.Builder cBuilder = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .connectionPool(connectionPool)
                .connectTimeout(site.getTimeOut(), TimeUnit.MILLISECONDS);
        ;
        /*if (proxy != null) {
            cBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getHost(), proxy.getPort())));
        }*/
        return cBuilder.build();
    }

    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request).newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "max-age=" + 3600 * 24 * 30)
                    .build();
            return response;
        }
    }

}
