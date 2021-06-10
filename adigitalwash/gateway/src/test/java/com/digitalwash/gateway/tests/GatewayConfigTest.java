package com.digitalwash.gateway.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.digitalwash.gateway.tests.configs.GatewayConfigComponent;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes={GatewayConfigComponent.class})
public class GatewayConfigTest {
	
	@Autowired
	private ApplicationContext appCxt;
	
	@Autowired
	private DiscoveryClient discoverClient;
	
	@Test
	public void applicationContextLoaded() throws Exception {
		assertNotNull(appCxt);
	}
	@Test
	public void discoveryClientTest() throws Exception {
		assertNotNull(discoverClient);
	}
}
