package com.personal.quantization.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ResourceServiceImpl {

    private Map<String, List<String>> resourceRolesMap;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @PostConstruct
    public void initData() {
    	List<String> adminList = new ArrayList<>();
        List<String> testList = new ArrayList<>();
        adminList.add("ADMIN");
        testList.add("ADMIN");
        testList.add("TEST");
        resourceRolesMap = new TreeMap<>();
        resourceRolesMap.put("/api/hello", adminList);
        resourceRolesMap.put("/api/user/currentUser", testList);
        redisTemplate.opsForHash().putAll("RESOURCE_ROLES_MAP", resourceRolesMap);
    }
}