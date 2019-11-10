package com.pulawskk.sportseventapi.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public interface HttpPostService {

    void postJsonMessage(JSONObject jsonObject, String url) throws IOException;
}
