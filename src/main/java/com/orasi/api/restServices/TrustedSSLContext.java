package com.orasi.api.restServices;

import static com.orasi.api.WebServiceConstants.DEFAULT_REST_TIMEOUT;
import static com.orasi.utils.TestReporter.logTrace;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import com.orasi.api.WebServiceException;

/**
 * Intended to override the default Trust actions for SSL content to accept all issued certificates
 *
 * @author Justin Phlegar
 *
 */
public class TrustedSSLContext {
    public static CloseableHttpClient buildHttpClient() {
        return buildHttpClient(DEFAULT_REST_TIMEOUT);
    }

    /**
     * Build a HttpClient that associates an overridden SSL Context
     *
     * @return httpClient that will accept all SSL connections
     */
    public static CloseableHttpClient buildHttpClient(int timeout) {
        logTrace("Entering TrustedSSLContext#buildHttpClient");
        logTrace("Creating Http Client Builder object");

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        logTrace("Creating SSL Context that will accept all server certificates");
        SSLContext sslContext = null;
        try {
            logTrace("Generate custom SSL Context to override isTrusted");
            sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new WebServiceException("Failed to create custom SSL Context", e);
        }

        logTrace("Successfully built custom SSL Context");
        logTrace("Associate custom SSL Context to Http Client");
        clientBuilder.setSSLContext(sslContext);

        logTrace("Creating a custom Socket Factory to register https connections to use");
        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        logTrace("Register https connections to use new Socket Factory");
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslSocketFactory)
                .build();

        logTrace("Associating Socket Registry to Http Client");
        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        clientBuilder.setConnectionManager(connMgr);

        logTrace("Building config with timeouts of [ " + (timeout * 1000) + " ] seconds");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .setSocketTimeout(timeout * 1000)
                .build();

        logTrace("Building new Http Client");
        CloseableHttpClient client = clientBuilder.setDefaultRequestConfig(requestConfig).build();
        logTrace("Exiting TrustedSSLContext#buildHttpClient");
        return client;
    }
}
