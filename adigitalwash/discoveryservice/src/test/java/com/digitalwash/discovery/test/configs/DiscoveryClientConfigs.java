package com.digitalwash.discovery.test.configs;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;

@Component
@TestComponent
@EnableDiscoveryClient
public class DiscoveryClientConfigs {
}
