/*
 * package com.personal.quantization.gateway.filter; import
 * java.util.Collection; import java.util.HashMap; import java.util.Map; import
 * java.util.Set;
 * 
 * import org.springframework.cloud.gateway.filter.GatewayFilterChain; import
 * org.springframework.cloud.gateway.filter.GlobalFilter; import
 * org.springframework.core.Ordered; import
 * org.springframework.http.server.reactive.ServerHttpRequest; import
 * org.springframework.security.core.Authentication; import
 * org.springframework.security.core.GrantedAuthority; import
 * org.springframework.security.core.authority.AuthorityUtils; import
 * org.springframework.security.core.context.SecurityContextHolder; import
 * org.springframework.security.oauth2.provider.OAuth2Authentication; import
 * org.springframework.stereotype.Component; import
 * org.springframework.util.Base64Utils; import
 * org.springframework.web.server.ServerWebExchange;
 * 
 * import com.alibaba.fastjson.JSON;
 * 
 * import lombok.extern.slf4j.Slf4j; import reactor.core.publisher.Mono;
 * 
 * @Slf4j
 * 
 * @Component public class AuthenticationFilter implements GlobalFilter, Ordered
 * {
 * 
 * @Override public Mono<Void> filter(ServerWebExchange exchange,
 * GatewayFilterChain chain) {
 * 
 * Authentication authentication =
 * SecurityContextHolder.getContext().getAuthentication(); //
 * 如果解析到令牌就会封装到OAuth2Authentication对象 if( !(authentication instanceof
 * OAuth2Authentication)) { return null; } log.info("网关获取到认证对象：" +
 * authentication); // 用户名,没有其他用户信息 Object principal =
 * authentication.getPrincipal(); // 获取用户所拥有的权限 Collection<? extends
 * GrantedAuthority> authorities = authentication.getAuthorities(); Set<String>
 * authoritySet = AuthorityUtils.authorityListToSet(authorities); // 请求详情 Object
 * details = authentication.getDetails(); Map<String, Object> result = new
 * HashMap<>(); result.put("principal", principal); result.put("authorities",
 * authoritySet); result.put("details", details); // 获取当前请求上下文 //RequestContext
 * context = RequestContext.getCurrentContext(); ServerHttpRequest request =
 * exchange.getRequest(); // 将用户信息和权限信息转成json,再通过base64进行编码 String base64 =
 * Base64Utils.encodeToString(JSON.toJSONString(result).getBytes()); // 添加到请求头
 * request.getHeaders().set("auth-token", base64); return null; }
 * 
 *//**
	 * 指定过滤器的执行顺序，返回值越小，优先级越高
	 *
	 * @return
	 *//*
		 * @Override public int getOrder() { return 1; } }
		 */