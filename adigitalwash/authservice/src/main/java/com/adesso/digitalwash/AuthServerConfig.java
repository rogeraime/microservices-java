package com.adesso.digitalwash;

import java.security.Principal;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.LdapUserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adesso.digitalwash.model.CustomTokenEnhancer;
import com.adesso.digitalwash.model.CustomUserDetailsContextMapper;

@Configuration
@EnableAuthorizationServer
@EnableResourceServer
@RestController
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Value("${client.laundryclient.scopes}")
	private String scopes;

	@Value("${client.laundryclient.accessTokenValiditySeconds}")
	private int accessTokenValiditySeconds;

	@Value("${client.laundryclient.refreshTokenValiditySeconds}")
	private int refreshTokenValiditySeconds;

	@Value("${ldap.user.namePattern}")
	private String namePattern;

	@Value("${ldap.group.searchBase}")
	private String groupSearchBase;

	@Value("${ldap.baseDn}")
	private String ldapBaseDn;

	@Value("${ldap.server}")
	private String url;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private TokenEnhancer tokenEnhancer;

	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	@Autowired
	private CustomUserDetailsContextMapper userDetailsContextMapper;

	@Autowired
	private DefaultSpringSecurityContextSource contextSource;

	@Bean
	public UserDetailsService customLdapUserDetailsService() {
		final FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch("", namePattern, contextSource);
		final LdapUserDetailsService service = new LdapUserDetailsService(userSearch);
		service.setUserDetailsMapper(userDetailsContextMapper);
		return service;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.checkTokenAccess("permitAll()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer, accessTokenConverter));
		endpoints.tokenStore(tokenStore).tokenEnhancer(tokenEnhancerChain).authenticationManager(authenticationManager)
				.userDetailsService(customLdapUserDetailsService());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("laundryclient").secret("secret")
				.authorizedGrantTypes("authorization_code", "password", "refresh_token").scopes(scopes)
				.accessTokenValiditySeconds(accessTokenValiditySeconds)
				.refreshTokenValiditySeconds(refreshTokenValiditySeconds);
	}

	@Bean
	public TokenStore tokenStore(JwtAccessTokenConverter accessTokenConverter) {
		return new JwtTokenStore(accessTokenConverter);
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
				new ClassPathResource("tokenKeys.jks"),"@deSSo-DigIwash!".toCharArray());
		converter.setKeyPair(keyStoreKeyFactory.getKeyPair("tokenKeys"));
		return converter;
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

	@Bean
	@Primary
	public DefaultTokenServices tokenServices(TokenStore tokenStore) {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setTokenStore(tokenStore);
		return defaultTokenServices;
	}

	@RequestMapping("oauth/user")
	public Principal user(Principal p) {
		return p;
	}
}