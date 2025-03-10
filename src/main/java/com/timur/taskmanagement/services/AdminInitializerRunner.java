package com.timur.taskmanagement.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializerRunner implements CommandLineRunner {

    private final AdminInitializer adminInitializer;

    public AdminInitializerRunner(AdminInitializer adminInitializer) {
        this.adminInitializer = adminInitializer;
    }

    @Override
    public void run(String... args) throws Exception {
        adminInitializer.initializeAdmin();
    }
}
