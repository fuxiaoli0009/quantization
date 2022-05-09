/*
 * package com.personal.quantization.config;
 * 
 * import java.io.IOException;
 * 
 * import org.apache.commons.io.IOUtils; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.core.io.ClassPathResource; import
 * org.springframework.security.oauth2.provider.token.TokenStore; import
 * org.springframework.security.oauth2.provider.token.store.
 * JwtAccessTokenConverter; import
 * org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
 * 
 * @Configuration public class TokenConfig {
 * 
 * @Bean public TokenStore tokenStore() { return new
 * JwtTokenStore(jwtAccessTokenConverter()); }
 * 
 * @Bean public JwtAccessTokenConverter jwtAccessTokenConverter() {
 * JwtAccessTokenConverter converter = new JwtAccessTokenConverter(); //
 * 非对称加密，资源服务器使用公钥解密 public.txt ClassPathResource resource = new
 * ClassPathResource("public.txt"); String publicKey = null; try { publicKey =
 * IOUtils.toString(resource.getInputStream(), "UTF-8"); } catch (IOException e)
 * { e.printStackTrace(); } converter.setVerifierKey(publicKey); return
 * converter; }
 * 
 * }
 */