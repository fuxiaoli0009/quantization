package com.personal.quantization.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 当前类用于管理所有的资源: 认证服务器资源,front资源服务器,center资源服务器
 */
@Configuration
public class ResourceServerConfig {
	
	public static final String FRONT_RESOURCE_ID = "quantization-front";
    public static final String CENTER_RESOURCE_ID = "quantization-center";
    public static final String AUTH_RESOURCE_ID = "auth-server";
    
    @Autowired
    private TokenStore tokenStore;

    // 认证服务资源
    @Configuration
    @EnableResourceServer
    public class AuthResourceServerConfig extends ResourceServerConfigurerAdapter{
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(AUTH_RESOURCE_ID)
                    .tokenStore(tokenStore)
            ;
        }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            // 关于请求认证服务器资源,则所有请求放行
            http.authorizeRequests().antMatchers("/auth/**").permitAll();
        }
    }

    // front资源服务器的资源
    @Configuration
    @EnableResourceServer
    public class FrontResourceServerConfig extends ResourceServerConfigurerAdapter{
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(FRONT_RESOURCE_ID)
                    .tokenStore(tokenStore)
            ;
        }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/quantization/**")
                    .access("#oauth2.hasScope('PRODUCT_API')");
        }
    }

 // center资源服务器的资源
    @Configuration
    @EnableResourceServer
    public class CenterResourceServerConfig extends ResourceServerConfigurerAdapter{
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.resourceId(CENTER_RESOURCE_ID)
                    .tokenStore(tokenStore)
            ;
        }
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/center/**")
                    .access("#oauth2.hasScope('PRODUCT_API')");
        }
    }

}
