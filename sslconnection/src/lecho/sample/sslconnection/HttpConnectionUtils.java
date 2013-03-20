package lecho.sample.sslconnection;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.Registry;
import org.simpleframework.xml.convert.RegistryStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Context;
import android.util.Log;

/**
 * Helps to create HttpClient and RestTemplate for ssl connection
 * 
 * TODO probably better to use HttpUrlConnection
 * 
 * @author lecho
 * 
 */
public class HttpConnectionUtils {

    public static RestTemplate getRestTemplate(Context context) {
        HttpClient client = HttpConnectionUtils.getDumbSSLHttpClient();
        // HttpClient client = HttpConnectionUtils.getSSLHttpClient();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);
        requestFactory.getHttpClient().getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
        requestFactory.getHttpClient().getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

        RestTemplate template = new RestTemplate();

        setSerializers(requestFactory, template);

        return template;
    }

    /**
     * Sets serializer if some class should be treat different then default in
     * simplexml library.
     * 
     * @param requestFactory
     * @param template
     * @param messageConverters
     * @param xmlConverter
     */
    private static void setSerializers(HttpComponentsClientHttpRequestFactory requestFactory, RestTemplate template) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        SimpleXmlHttpMessageConverter xmlConverter = new SimpleXmlHttpMessageConverter();
        Registry registry = new Registry();
        try {
            // registry.bind(...);//here bind class to converter
        } catch (Exception e) {
            Log.e(HttpConnectionUtils.class.getSimpleName(), "Error binding converter", e);
        }
        Strategy strategy = new RegistryStrategy(registry);
        Serializer serializer = new Persister(strategy);
        xmlConverter.setSerializer(serializer);
        messageConverters.add(xmlConverter);
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
        template.setRequestFactory(requestFactory);
        template.setMessageConverters(messageConverters);
    }

    protected static org.apache.http.conn.ssl.SSLSocketFactory createAdditionalCertsSSLSocketFactory(Context context) {
        try {
            final KeyStore ks = KeyStore.getInstance("BKS");

            // keystore with root certificate in bks format
            final InputStream in = context.getResources().openRawResource(R.raw.keystore);
            try {
                ks.load(in, "password".toCharArray());
            } finally {
                in.close();
            }

            return new AdditionalKeyStoresSSLSocketFactory(ks);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * HttpClient checking certificates tree
     * 
     */
    public static DefaultHttpClient getSSLHttpClient(Context context) {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", createAdditionalCertsSSLSocketFactory(context), 443));

        final HttpParams params = new BasicHttpParams();
        final ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);

        DefaultHttpClient client = new DefaultHttpClient(cm, params);
        return client;
    }

    /**
     * HttpClient accepting all certificates
     * 
     * @return
     */
    public static HttpClient getDumbSSLHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new DumbSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
