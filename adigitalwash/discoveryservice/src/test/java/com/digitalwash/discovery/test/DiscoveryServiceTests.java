package com.digitalwash.discovery.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.digitalwash.discovery.test.configs.DiscoveryClientConfigs;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = DiscoveryClientConfigs.class)
public class DiscoveryServiceTests {

	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Test
	public void appContextLoaded() throws Exception {
		assertNotNull(appContext);
	}

	@Test
	public void eurekaServerTest() throws Exception {
		assertNotNull(discoveryClient);
	}

	@Test
	public void resourcesLoadedTest() throws Exception {
		boolean exists = false;
		exists = appContext.getResource("classpath:discoveryservice-test.yml").exists();
		assertTrue(exists);
	}
}
