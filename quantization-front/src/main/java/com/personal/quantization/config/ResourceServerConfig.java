package com.personal.quantization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 1、资源服务器配置类：
 *    当用户请求某个微服务资源时，首先要进行认证与授权，通过后再才可访问到对应资源。
 *    
 * 2、@EnableResourceServer：该注解标识系统为资源服务器。
 *    请求服务中的资源时，就要带着token过来，找不到token或token无效就访问不了资源。
 *    
 * 3、资源服务器需要知道自己是什么资源服务器（配置资源服务器ID）,去哪里验令牌,怎么验令牌,要带什么信息去验证。
 *
 * 4、进行资源的安全配置，让资源管理器知道资源的每个访问权限是什么。
 * 
 * 5、当认证服务器和资源服务器不是在同一工程时,要使用ResourceServerTokenServices去远程请求认证服务器来校验
	  令牌的合法性，如果用户访问量较大时将会影响系统的性能。生成令牌采用JWT格式就可以解决上面的问题。
	  因为当用户认证后获取到一个JWT令牌，而这个JWT令牌包含了用户基本信息，客户端只需要携带JWT访问资源服
	  务器，资源服务器会通过事先约定好的算法进行解析出来，然后直接对JWT令牌校验，不需要每次远程请求认证服
	  务器完成授权。
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启方法级别权限控制
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String RESOURCE_ID = "quantization";

    @Autowired
    private TokenStore tokenStore;

    /**
     * 6、配置当前资源服务器的资源id，认证服务器会认证客户端有没有访问这个资源id的权限
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore);
    }

    /**
     * 7、资源服务器通过该方法指定授权规则。
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                 // SpringSecurity不会使用也不会创建HttpSession实例
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                // 授权规则配置
                // .antMatchers("/product/*").hasAuthority("product")
                // 所有请求，都需要有all范围（scope）
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                // 等价于上面
//              .anyRequest().access("#oauth2.hasScope('all')")
            ;
    }
}
