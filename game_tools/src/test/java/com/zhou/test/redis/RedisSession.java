/**
 * @author: lifangkai@elex-tech.com
 * @date: 2013年9月2日 下午8:14:30
 */
package com.zhou.test.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

import com.google.common.base.Optional;
import com.zhou.util.sfs.data.ISFSObject;
import com.zhou.util.sfs.data.SFSObject;

/**
 * Redis缓存实现
 */
public class RedisSession {
//    private static final String GLOBAL_REDIS_IP = PropertyFileReader.getString("global.redis.ip");
//    private static final String GLOBAL_SLAVE_REDIS_IP = PropertyFileReader.getString("global.slave.redis.ip");
//    private static final int GLOBAL_REDIS_PORT = PropertyFileReader.getInt("global.redis.port", 6379);
//    private static final int GLOBAL_REDIS_USEIDLE = PropertyFileReader.getInt("global.redis.useIdle", 0);
//    private static final int CROSS_REDIS_TIMEOUT = PropertyFileReader.getInt("cross.redis.timeout", 3000);
//    private static final int REDIS_PORT = PropertyFileReader.getInt("redis.port", 6379);
    private Jedis jedis = null;
    private boolean autoClose;
    private RedisConnectionMode mode;

    public enum RedisConnectionMode {
        NOT_POOL, POOL, CODIS
    }

    public RedisSession() {
        this(true);
    }

    public RedisSession(String s) {

    }

    public RedisSession(boolean autoClose) {
        jedis = RedisSessionUtil.getInstance().getResource();
        this.autoClose = autoClose;
        mode = RedisConnectionMode.POOL;
    }


    public static RedisSession getGlobal(boolean autoClose) {
        RedisSession rs = new RedisSession("empty");
        Jedis jedis = RedisSessionUtil.getInstance().getGlobalResource();
        rs.setJedis(jedis);
        rs.setMode(RedisConnectionMode.CODIS);
        rs.setAutoClose(autoClose);
        return rs;
    }

    public void close() {
        if (mode == RedisConnectionMode.NOT_POOL || mode == RedisConnectionMode.CODIS) {
            jedis.close();
        } else if (mode == RedisConnectionMode.POOL) {
            RedisSessionUtil.getInstance().close(jedis);
        }
    }

    public String set(String key, String field) {
        try {
            String ret = jedis.set(key, field);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String set(byte[] key, byte[] field) {
        try {
            String ret = jedis.set(key, field);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public int hsetnx(String key, String field, String value) {
        try {
            long result = jedis.hsetnx(key, field, value);
            //增加统计
            
            return (int) result;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Set<String> sMember(String key) {
        try {
            Set<String> set = jedis.smembers(key);
            //增加统计
            
            return set;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long sRem(String key, String... value) {
        try {
        	  //增加统计
            
            return jedis.srem(key, value);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public boolean sIsMember(String key, String member) {
        try {
        	  //增加统计
            
            return jedis.sismember(key, member);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long sAdd(String key, String... value) {
        try {
        	  //增加统计
            
            return jedis.sadd(key, value);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void del(byte[] key) {
        try {
        	 //增加统计
            
            jedis.del(key);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void del(String key) {
        try {
        	  //增加统计
            
            jedis.del(key);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long lPush(byte[] key, byte[] value) {
        long ret = 0;
        try {
            ret = jedis.lpush(key, value);
            //增加统计
            
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public long lPush(String key, String value) {
        long ret = 0;
        try {
            ret = jedis.lpush(key, value);
            //增加统计
            
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public List<String> lRange(String key, long start, long end) {
        List<String> ret = null;
        try {
            ret = jedis.lrange(key, start, end);
            //增加统计
            
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String rPop(String key) {
        String ret = null;
        try {
            ret = jedis.rpop(key);
            //增加统计
            
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String lPop(String key) {
        String ret = null;
        try {
            ret = jedis.lpop(key);
            //增加统计
            
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String rPoplPush(String skey, String dkey) {
        String ret = null;
        try {
            ret = jedis.rpoplpush(skey, dkey);
            //增加统计
            
        } catch (Exception e) {
            RedisSessionUtil.getInstance().closeBroken(jedis);
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public void lRem(byte[] key, int count, byte[] field) {
        try {
            jedis.lrem(key, count, field);
            //增加统计
            
        } finally {
            if (autoClose)
                close();
        }
    }

    public Set<String> keys(String pattern) {
        Set<String> ret;
        try {
            ret = jedis.keys(pattern);
            //增加统计
            
        } finally {
            if (autoClose)
                close();
        }
        return ret;
    }

    public String lIndex(String key, long count) {
        String ret;
        try {
            ret = jedis.lindex(key, count);
            //增加统计
            
        } finally {
            if (autoClose)
                close();
        }
        return ret;
    }

    public long rPush(String key, String value) {
        long ret = 0;
        try {
            ret = jedis.rpush(key, value);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }
        }
        return ret;
    }

    public String get(String key) {
        try {
            String ret = jedis.get(key);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }


    public byte[] get(byte[] key) {
        try {
            byte[] ret = jedis.get(key);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long incrBy(String key, long incValue) {
        try {
            long ret = jedis.incrBy(key, incValue);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long incr(String key) {
        try {
            long ret = jedis.incr(key);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long decr(String key) {
        try {
            long ret = jedis.decr(key);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public byte[] hGet(byte[] key, byte[] field) {
        try {
            byte[] ret = jedis.hget(key, field);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String hGet(String key, String field) {
        try {
            String ret = jedis.hget(key, field);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> hMGet(String key, String... field) {
        try {
            List<String> ret = jedis.hmget(key, field);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> hMGet(String key, List<String> fields) {
        try {
            String[] arr = new String[fields.size()];
            int i = 0;
            for (String item : fields) {
                arr[i++] = item;
            }
            List<String> ret = jedis.hmget(key, arr);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }

        }
    }

    public Map<String, String> hMGetAll(String key) {
        try {
            Map<String, String> ret = jedis.hgetAll(key);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void setExpireTime(String key, int seconds) {
        try {
            jedis.expire(key, seconds);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }

        }
    }

    public String hMSet(String key, String nestedKey, String value) {
        Map<String, String> map = new HashMap<>();
        try {
            map.put(nestedKey, value);
            String ret = jedis.hmset(key, map);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String hMSet(String key, Map<String, String> map) {
        try {
            String ret = jedis.hmset(key, map);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void mset(Map<String, String> map) {
        if (map == null && map.size() > 0) {
            return;
        }
        String keyValues[] = new String[map.size() * 2];
        int index = 0;
        for (Map.Entry<String, String> mapEntry : map.entrySet()) {
            keyValues[index++] = mapEntry.getKey();
            keyValues[index++] = mapEntry.getValue();
        }
        try {
            jedis.mset(keyValues);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long hDel(String key, List<String> fields) {
    	//增加统计
        
        return hDel(key, fields.toArray(new String[fields.size()]));
    }

    public long hDel(String key, String... fields) {
        try {
            long ret = jedis.hdel(key, fields);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }

        }
    }

    public long hSet(byte[] key, byte[] field, byte[] value) {
        try {
            long ret = jedis.hset(key, field, value);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }


    public long hSet(String key, String field, String value) {
        try {
            long ret = jedis.hset(key, field, value);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Set<byte[]> hKeys(byte[] key) {
        Set<byte[]> fields = new HashSet<>();
        try {
            fields = jedis.hkeys(key);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }
        }
        return fields;
    }

    public Set<String> hKeys(String key) {
        Set<String> fields = new HashSet<>();
        try {
            fields = jedis.hkeys(key);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }
        }
        return fields;
    }

    public long hLen(String key) {
        try {
            long length = jedis.hlen(key);
            //增加统计
            
            return length;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public boolean hExists(String key, String field) {
        try {
            boolean ret = jedis.hexists(key, field);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long pushListFromLeft(String key, String... members) {
        try {
            long ret = jedis.lpush(key, members);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String lSet(String key, long index, String members) {
        try {
            String ret = jedis.lset(key, index, members);
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<String> getRangeList(String key, int start, int end) {
        try {
            List<String> retList = jedis.lrange(key, start, end);
            //增加统计
            
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long getLength(String key) {
        try {
            long length = jedis.llen(key);
            //增加统计
            
            return length;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public List<byte[]> getRangeList(byte[] key, int start, int end) {
        try {
            List<byte[]> retList = jedis.lrange(key, start, end);
            //增加统计
            
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void lRem(String key, int count, String value) {
        try {
            jedis.lrem(key, count, value);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void lTrim(String key, int start, int end) {
        try {
            jedis.ltrim(key, start, end);
            //增加统计
            
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public String deleteListFromRight(String key) {
        try {
            String retList = jedis.rpop(key);
            //增加统计
            
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long hincrBy(String key, String field, long num) {
        try {
            //增加统计
            
            return jedis.hincrBy(key, field, num);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public long zAdd(String key, String member, long score) {
        try {
            //增加统计
            
            return jedis.zadd(key, (double) score, member);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Optional<Double> zScore(String key, String member) {
        try {
            //增加统计
            
            return Optional.fromNullable(jedis.zscore(key, member));
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Optional<Long> zGetRankByAsc(String key, String member) {
        try {
            //增加统计
            
            return Optional.fromNullable(jedis.zrank(key, member));
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public Optional<Long> zGetRankByDesc(String key, String member) {
        try {
            //增加统计
            
            return Optional.fromNullable(jedis.zrevrank(key, member));
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public HashMap<String, Double> zRangByAsc(String key, long start, long end) {
        try {
            HashMap<String, Double> ret = new HashMap<>();
            Set<Tuple> result = jedis.zrangeWithScores(key, start, end);
            if (result != null && result.size() > 0) {
                for (Tuple member : result) {
                    ret.put(member.getElement(), member.getScore());
                }
            }
            //增加统计
            
            return ret;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    /**
     * @param key
     * @param start
     * @param end
     * @return ISFSObject: key(String), score(Double)
     */
    public List<ISFSObject> zRangByDesc(String key, long start, long end) {
        try {
            List<ISFSObject> retList = new LinkedList<>();
            Set<Tuple> result = jedis.zrevrangeWithScores(key, start, end);
            if (result != null && result.size() > 0) {
                for (Tuple member : result) {
                    ISFSObject obj = SFSObject.newInstance();
                    obj.putUtfString("key", member.getElement());
                    obj.putDouble("score", member.getScore());
                    retList.add(obj);
                }
            }
            //增加统计
            
            return retList;
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void subscribe(JedisPubSub listener, String... channels) {
        try {
            jedis.subscribe(listener, channels);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }
    /**
     * 发布信息
     * @param channel
     * @param msg
     * @return
     */
    public long publish(String channel, String msg) {
        try {
            return jedis.publish(channel, msg);
        } finally {
            if (autoClose) {
                close();
            }
        }
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public void setMode(RedisConnectionMode mode) {
        this.mode = mode;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public Map<String, String> getBatch(RedisKey redisKey, Collection<String> list) {
        Map<String, String> result = new HashMap<>();
        Map<String, Response<String>> responses = new HashedMap();
        try {
            Pipeline pipeline = jedis.pipelined();
            for (String l : list) {
                String key = redisKey.suffix(l);
                responses.put(l, pipeline.get(key));
            }
            pipeline.sync();
            for (String key : responses.keySet()) {
                String v = responses.get(key).get();
                if (StringUtils.isNotBlank(v)) {
                    result.put(key, v);
                }
            }
        } finally {
            if (autoClose) {
                jedis.close();
            }
        }
        return result;
    }

}
