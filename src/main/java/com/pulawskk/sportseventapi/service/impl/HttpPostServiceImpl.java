package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.service.HttpPostService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class HttpPostServiceImpl implements HttpPostService {

    private final CloseableHttpClient client;
    private final HttpPost httpPost;

    public HttpPostServiceImpl(CloseableHttpClient client, HttpPost httpPost) {
        this.client = client;
        this.httpPost = httpPost;
    }

    @Override
    public void postJsonMessage(JSONObject jsonObject, String url) throws IOException {
        httpPost.setURI(URI.create(url));
        StringEntity entity = new StringEntity(jsonObject.toString());
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        try {
            CloseableHttpResponse response = client.execute(httpPost);
        } catch (Exception e) {
            System.out.println("Could not send a message due to: -> " + e.getMessage());
        } finally {
            client.close();
        }
    }
}
