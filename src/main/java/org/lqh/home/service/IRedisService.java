package org.lqh.home.service;

import org.springframework.data.redis.core.ListOperations;

import java.util.List;
import java.util.Set;

/**
 * @description: TODO 类描述
 * @author: 丁真
 * @date: 2023/11/6
 **/
public interface IRedisService {
    /**
     * 添加key：string缓存
     * @param key
     * @param value
     * @param time
     * @return
     */
    boolean cacheValue(String key,String value,long time);

    /**
     * 添加key：string缓存
     * @param key
     * @param value
     * @return
     */
    boolean cacheValue(String key,String value);

    /**
     * 判断key：string集合是否存在
     * @param key
     * @return
     */
    boolean containsValueKey(String key);

    /**
     * 判断缓存 key:set集合 是否存在
     * @param key
     * @return
     */
    boolean containSetKey(String key);

    /**
     * 判断key：list集合是否存在
     * @param key
     * @return
     */
    boolean containListKey(String key);

    /**
     * 查询缓存key是否存在
     * @param key
     * @return
     */
    boolean containsKey(String key);

    /**
     * 根据key获取缓存value
     * @param key
     * @return
     */
    String getValue(String key);

    /**
     * 根据key移除value缓存
     * @param key
     * @return
     */
    boolean removeValue(String key);

    /**
     * 根据key移除set缓存
     * @param key
     * @return
     */
    boolean removeSet(String key);

    /**
     * 根据key移除list缓存
     * @param key
     * @return
     */
    boolean removeList(String key);

    /**
     * 缓存 set 操作
     * @param key
     * @param value
     * @param time
     * @return
     */
    boolean cacheSet(String key,String value,long time);

    /**
     * 添加set缓存
     * @param key
     * @param value
     * @return
     */
    boolean cacheSet(String key,String value);

    /**
     * 添加 缓存set
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheSet(String k, Set<String> v,long time);

    /**
     * 缓存set
     * @param k
     * @param v
     * @return
     */
    boolean cacheSet(String k, Set<String> v);

    /**
     * 获取缓存set数据
     * @param k
     * @return
     */
    Set<String> getSet(String k);

    /**
     * list缓存
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheList(String k,String v,long time);

    /**
     * 缓存list
     * @param k
     * @param v
     * @return
     */
    boolean cacheList(String k,String v);

    /**
     * 缓存list集合
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheList(String k, List<String>  v,long time);

    /**
     * 缓存list
     * @param K
     * @param v
     * @return
     */
    boolean cacheList(String k,List<String>v);

    /**
     * 根据 key 获取 list 缓存
     * @param k
     * @param start
     * @param end
     * @return
     */
    List<String> getList(String k,long start,long end);

    /**
     * 根据 key 获取总条数 用于分页
     * @param key
     * @return
     */
    long getListSize(String key);

    /**
     * 获取总条数 用于分页
     * @param listOps
     * @param k
     * @return
     */
    long getListSize(ListOperations<String,String> listOps,String k);

    /**
     * 根据key移除list缓存
     * @param k
     * @return
     */
    boolean removeOneOfList(String k);


}
