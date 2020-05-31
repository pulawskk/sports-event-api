package com.pulawskk.sportseventapi.service.impl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class HttpPostServiceImplTest {

    @InjectMocks
    HttpPostServiceImpl httpPostService;

    @Mock
    HttpPost httpPost;

    @Mock
    CloseableHttpClient closeableHttpClient;

    @BeforeEach
    void setUp() {
        httpPostService = new HttpPostServiceImpl(closeableHttpClient, httpPost);
    }

    @Test
    void shouldSendMessage_whenHttpPostClientIsAvailable() throws IOException {
        //given
        final String url = "http://localhost:8080";
        final JSONObject jsonObject = new JSONObject();
        when(closeableHttpClient.execute(any(HttpPost.class))).thenReturn(any(CloseableHttpResponse.class));

        //when
        httpPostService.postJsonMessage(jsonObject, url);

        //then
        then(closeableHttpClient).should(times(1)).execute(any(HttpPost.class));
    }
}

