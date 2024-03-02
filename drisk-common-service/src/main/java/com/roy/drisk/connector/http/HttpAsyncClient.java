package com.roy.drisk.connector.http;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * @author lantianli
 * @date 2023/10/27
 * @desc
 */
public class HttpAsyncClient {
    CloseableHttpAsyncClient client;

    public HttpAsyncClient(CloseableHttpAsyncClient client) {
        this.client = client;
    }

    public boolean isRunning() {
        return client.isRunning();
    }

    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
        return client.execute(requestProducer, responseConsumer, callback);
    }

    public Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
        return client.execute(target, request, context, callback);
    }

    void close() throws IOException {
        client.close();
    }

    void start() {
        client.start();
    }

    public Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
        return client.execute(request, callback);
    }

    public Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback) {
        return client.execute(target, request, callback);
    }

    public Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
        return client.execute(request, context, callback);
    }

    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, HttpContext context, FutureCallback<T> callback) {
        return client.execute(requestProducer, responseConsumer, context, callback);
    }
}
