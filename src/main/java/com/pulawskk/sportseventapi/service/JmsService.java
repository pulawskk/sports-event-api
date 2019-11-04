package com.pulawskk.sportseventapi.service;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface JmsService {
    void sendJsonMessage(String queueName, JSONObject jsonObject);
}
