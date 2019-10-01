package com.pulawskk.sportseventapi;

import org.testcontainers.containers.PostgreSQLContainer;

public class MainPostgresqlContainer extends PostgreSQLContainer<MainPostgresqlContainer> {
    private static final String IMAGE_VERSION = "postgres:latest";
    private static MainPostgresqlContainer container;

    private MainPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static MainPostgresqlContainer getInstance() {
        if (container == null) {
            container = new MainPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {

    }
}
