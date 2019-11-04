package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.service.JmsService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class JmsServiceImpl implements JmsService {

    private final String URI = "amqp://mxddlpbm:3nP42tVOl_XbgGvhODI8nIu4GdAPXB2g@golden-kangaroo.rmq.cloudamqp.com/mxddlpbm";
    private final ConnectionFactory connectionFactory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    public JmsServiceImpl() throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        connectionFactory.setUri(URI);
    }

    public void sendJsonMessage(String queueName, JSONObject jsonObject) {
        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);
            channel.basicPublish("", queueName, null, jsonObject.toString().getBytes());
            channel.close();
            connection.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
