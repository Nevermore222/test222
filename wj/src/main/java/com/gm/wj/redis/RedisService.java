package com.gm.wj.redis;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis service.
 *
 * @author Evan
 * @date 2020/3/11 12:42
 */
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public Long delete(Set<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Set<String> getKeysByPattern(String pattern) {
        return redisTemplate.keys(pattern);
    }
    //将list放入缓存
    public void setList(String key, List<String> list){
        redisTemplate.boundListOps(key).rightPushAll(list);
    }

    //获取list
    public List<String> getList(String key){
        Object o = redisTemplate.boundListOps(key).range(0, -1).get(0);
        //Object转list
        List<String> typeList = castList(o, String.class);
        return typeList;
    }

    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}
