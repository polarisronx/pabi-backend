package com.polaris.project.utils;

/**
 * @author polaris
 * @version 1.0
 * ClassName SslUtil
 * Package com.polaris.project.utils
 * Description
 * @create 2024-07-07 15:48
 */
import org.springframework.core.io.DefaultResourceLoader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class SslUtil {

    public static SSLContext createSSLContext(String type, InputStream jksInputStream, String sslPassword) throws Exception {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        InputStream inputStream = jksInputStream;
        char[] passArray = sslPassword.toCharArray();
        SSLContext sslContext = SSLContext.getInstance("SSLv3"); // 这里TLS或者SSLv3都可以
        KeyStore ks = KeyStore.getInstance("JKS");
        // 加载keytool 生成的文件
        ks.load(inputStream, passArray);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, passArray);
        sslContext.init(kmf.getKeyManagers(), null, null);
        inputStream.close();
        return sslContext;
    }
}
