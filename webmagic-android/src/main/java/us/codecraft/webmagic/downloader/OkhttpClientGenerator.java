package us.codecraft.webmagic.downloader;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionPool;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.utils.SSLVerifyUtils;

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

        if(TextUtils.isEmpty(site.getCertificateFileLocation())) {
            cBuilder.sslSocketFactory(createDefaultSSLVerifier(), createTrustManager());
        } else {
            cBuilder.sslSocketFactory(createSSLVerifier(site.getCertificateFileLocation(), site.getCertificatePassword()),
                    createTrustManager());
        }
        if (proxy != null) {
            cBuilder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getHost(), proxy.getPort())));
        }
        return cBuilder.build();
    }

    private SSLSocketFactory createSSLVerifier(String certificateFile, char[] pas) {
        File f = new File(certificateFile);
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(f.exists()) {
            try {
                KeyStore ks = KeyStore.getInstance("BKS");
                ks.load(SSLVerifyUtils.getCertificate(certificateFile), pas);
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("x509");
                tmf.init(ks);
                TrustManager[] trustManagers = tmf.getTrustManagers();
                sslContext.init(null, trustManagers, null);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            return createDefaultSSLVerifier();

        return sslContext.getSocketFactory();
    }

    private SSLSocketFactory createDefaultSSLVerifier() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] {new MyTrustManager()}, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslContext.getSocketFactory();
    }

    private X509TrustManager createTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
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

    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
