package com.roy.drisk.connector.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc <code>CloseableHttpClient</code>代理类，屏蔽了{@link #close()}方法，由Connector统一管理
 */
public class HttpSyncClient {
    CloseableHttpClient client;

    public HttpSyncClient(CloseableHttpClient client) {
        this.client = client;
    }

    public CloseableHttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return client.execute(target, request, context);
    }

    void close() throws IOException {
        client.close();
    }

    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return client.execute(target, request, responseHandler);
    }

    public CloseableHttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
        return client.execute(request, context);
    }

    public CloseableHttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        return client.execute(target, request);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        return client.execute(request, responseHandler);
    }

    public CloseableHttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        return client.execute(request);
    }

    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return client.execute(target, request, responseHandler, context);
    }

    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
        return client.execute(request, responseHandler, context);
    }
}
