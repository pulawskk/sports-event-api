package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.service.HttpPostService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;

@Service
public class HttpPostServiceImpl implements HttpPostService {

    private final CloseableHttpClient client;
    private final HttpPost httpPost;

    private final Logger logger = LoggerFactory.getLogger(HttpPostServiceImpl.class);

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
            logger.warn("["+getClass().getSimpleName()+"] method: postJsonMessage couldn't send a message due to: " + e.getMessage());
        } finally {
            client.close();
        }
    }
}
