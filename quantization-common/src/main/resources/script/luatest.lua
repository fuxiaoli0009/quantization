if redis.call("get",KEYS[1]) ~= nil then
    print("a23d")
    return redis.call("del", KEYS[1])
else
    return 0
end
