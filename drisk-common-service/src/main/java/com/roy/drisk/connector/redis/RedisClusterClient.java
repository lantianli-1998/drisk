package com.roy.drisk.connector.redis;

import redis.clients.jedis.*;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author roy
 * @date 2021/10/27
 * @desc JedisCluster客户端代理类
 */
public class RedisClusterClient implements CloseableJedisCommands {
    private JedisCluster client;

    public RedisClusterClient(JedisCluster client) {
        this.client = client;
    }
    
    public String set(String key, String value) {
        return client.set(key, value);
    }

    public String mset(byte[]... keysvalues) {
        return client.mset(keysvalues);
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        return client.zrangeByScoreWithScores(key, min, max);
    }

    public byte[] scriptLoad(byte[] script, byte[] key) {
        return client.scriptLoad(script, key);
    }

    public Long bitcount(String key, long start, long end) {
        return client.bitcount(key, start, end);
    }

    public Boolean hexists(String key, String field) {
        return client.hexists(key, field);
    }

    public List<Boolean> scriptExists(String key, String... sha1) {
        return client.scriptExists(key, sha1);
    }

    public Set<String> hkeys(String key) {
        return client.hkeys(key);
    }

    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        return client.linsert(key, where, pivot, value);
    }

    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        return client.zrangeByScore(key, min, max, offset, count);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        return client.zrevrangeByScoreWithScores(key, max, min);
    }

    public byte[] get(byte[] key) {
        return client.get(key);
    }

    public Long sadd(String key, String... member) {
        return client.sadd(key, member);
    }

    public String rpoplpush(String srckey, String dstkey) {
        return client.rpoplpush(srckey, dstkey);
    }

    public Set<String> sinter(String... keys) {
        return client.sinter(keys);
    }

    public Long append(String key, String value) {
        return client.append(key, value);
    }

    public byte[] spop(byte[] key) {
        return client.spop(key);
    }

    public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        return client.sort(key, sortingParameters, dstkey);
    }

    public Map<String, String> hgetAll(String key) {
        return client.hgetAll(key);
    }

    public Object eval(String script, int keyCount, String... params) {
        return client.eval(script, keyCount, params);
    }

    public Set<byte[]> sinter(byte[]... keys) {
        return client.sinter(keys);
    }

    public List<String> srandmember(String key, int count) {
        return client.srandmember(key, count);
    }

    public Set<byte[]> hkeys(byte[] key) {
        return client.hkeys(key);
    }

    public Long lrem(String key, long count, String value) {
        return client.lrem(key, count, value);
    }

    public Set<String> zrevrangeByScore(String key, double max, double min) {
        return client.zrevrangeByScore(key, max, min);
    }

    public byte[] lindex(byte[] key, long index) {
        return client.lindex(key, index);
    }

    public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return client.zremrangeByLex(key, min, max);
    }

    public Long zrem(String key, String... member) {
        return client.zrem(key, member);
    }

    public Long zinterstore(byte[] dstkey, byte[]... sets) {
        return client.zinterstore(dstkey, sets);
    }

    public Long scard(String key) {
        return client.scard(key);
    }

    public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
        return client.sort(key, sortingParameters);
    }

    public Long pexpire(byte[] key, long milliseconds) {
        return client.pexpire(key, milliseconds);
    }

    @Deprecated
    public String shutdown() {
        return client.shutdown();
    }

    public List<String> blpop(int timeout, String... keys) {
        return client.blpop(timeout, keys);
    }

    public String substr(String key, int start, int end) {
        return client.substr(key, start, end);
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        return client.zrangeWithScores(key, start, end);
    }

    public List<String> brpop(int timeout, String key) {
        return client.brpop(timeout, key);
    }

    public String lset(byte[] key, long index, byte[] value) {
        return client.lset(key, index, value);
    }

    public String set(byte[] key, byte[] value) {
        return client.set(key, value);
    }

    public Long rpush(byte[] key, byte[]... args) {
        return client.rpush(key, args);
    }

    public Long hincrBy(String key, String field, long value) {
        return client.hincrBy(key, field, value);
    }

    public Long hsetnx(byte[] key, byte[] field, byte[] value) {
        return client.hsetnx(key, field, value);
    }

    public Long exists(byte[]... keys) {
        return client.exists(keys);
    }

    public Long sort(byte[] key, byte[] dstkey) {
        return client.sort(key, dstkey);
    }

    public Double zscore(byte[] key, byte[] member) {
        return client.zscore(key, member);
    }

    public String srandmember(String key) {
        return client.srandmember(key);
    }

    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
        return client.hscan(key, cursor);
    }

    public Boolean getbit(byte[] key, long offset) {
        return client.getbit(key, offset);
    }

    public Boolean sismember(byte[] key, byte[] member) {
        return client.sismember(key, member);
    }

    public Long zrank(byte[] key, byte[] member) {
        return client.zrank(key, member);
    }

    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
        return client.sscan(key, cursor, params);
    }

    public ScanResult<String> sscan(String key, String cursor) {
        return client.sscan(key, cursor);
    }

    public byte[] rpop(byte[] key) {
        return client.rpop(key);
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        return client.zadd(key, scoreMembers);
    }

    public Long zremrangeByRank(String key, long start, long end) {
        return client.zremrangeByRank(key, start, end);
    }

    public String get(String key) {
        return client.get(key);
    }

    public List<GeoCoordinate> geopos(String key, String... members) {
        return client.geopos(key, members);
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
        return client.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Long zremrangeByScore(String key, double start, double end) {
        return client.zremrangeByScore(key, start, end);
    }

    public byte[] hget(byte[] key, byte[] field) {
        return client.hget(key, field);
    }

    public List<byte[]> blpop(int timeout, byte[]... keys) {
        return client.blpop(timeout, keys);
    }

    @Deprecated
    public String info(String section) {
        return client.info(section);
    }

    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return client.zrangeByLex(key, min, max);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        return client.zrevrangeByScoreWithScores(key, max, min);
    }

    public Double incrByFloat(byte[] key, double value) {
        return client.incrByFloat(key, value);
    }

    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        return client.zrangeByScore(key, min, max, offset, count);
    }

    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return client.zadd(key, scoreMembers, params);
    }

    public Long bitcount(byte[] key, long start, long end) {
        return client.bitcount(key, start, end);
    }

    public Long pfadd(String key, String... elements) {
        return client.pfadd(key, elements);
    }

    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return client.georadius(key, longitude, latitude, radius, unit, param);
    }

    @Deprecated
    public String slaveofNoOne() {
        return client.slaveofNoOne();
    }

    public Long exists(String... keys) {
        return client.exists(keys);
    }

    @Deprecated
    public String set(String key, String value, String nxxx) {
        return client.set(key, value, nxxx);
    }

    public Set<byte[]> sunion(byte[]... keys) {
        return client.sunion(keys);
    }

    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
        return client.georadiusByMember(key, member, radius, unit);
    }

    public String echo(String string) {
        return client.echo(string);
    }

    public Boolean setbit(String key, long offset, String value) {
        return client.setbit(key, offset, value);
    }

    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return client.zscan(key.getBytes(StandardCharsets.UTF_8), cursor.getBytes(StandardCharsets.UTF_8), params);
    }

    public Long sort(String key, SortingParams sortingParameters, String dstkey) {
        return client.sort(key, sortingParameters, dstkey);
    }

    public String brpoplpush(String source, String destination, int timeout) {
        return client.brpoplpush(source, destination, timeout);
    }

    public String set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, long time) {
        return client.set(key, value, nxxx, expx, time);
    }

    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return client.georadiusByMember(key, member, radius, unit, param);
    }

    public List<Long> bitfield(String key, String... arguments) {
        return client.bitfield(key, arguments);
    }

    public Long zcount(String key, String min, String max) {
        return client.zcount(key, min, max);
    }

    @Deprecated
    public String flushDB() {
        return client.flushDB();
    }

    public String setex(String key, int seconds, String value) {
        return client.setex(key, seconds, value);
    }

    public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return client.zadd(key, score, member, params);
    }

    public Long zcount(String key, double min, double max) {
        return client.zcount(key, min, max);
    }

    public String type(byte[] key) {
        return client.type(key);
    }

    public String hmset(String key, Map<String, String> hash) {
        return client.hmset(key, hash);
    }

    public Set<String> zrangeByLex(String key, String min, String max) {
        return client.zrangeByLex(key, min, max);
    }

    public Set<byte[]> spop(byte[] key, long count) {
        return client.spop(key, count);
    }

    public Double zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        return client.zincrby(key, score, member, params);
    }

    public Long sunionstore(byte[] dstkey, byte[]... keys) {
        return client.sunionstore(dstkey, keys);
    }

    public String lpop(String key) {
        return client.lpop(key);
    }

    public Object evalsha(byte[] script, byte[] key) {
        return client.evalsha(script, key);
    }

    public Boolean scriptExists(String sha1, String key) {
        return client.scriptExists(sha1, key);
    }

    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        return client.zrevrangeByScore(key, max, min, offset, count);
    }

    public Long expire(byte[] key, int seconds) {
        return client.expire(key, seconds);
    }

    @Deprecated
    public Long dbSize() {
        return client.dbSize();
    }

    public Set<String> sunion(String... keys) {
        return client.sunion(keys);
    }

    public Long zremrangeByScore(byte[] key, double start, double end) {
        return client.zremrangeByScore(key, start, end);
    }

    public String hmset(byte[] key, Map<byte[], byte[]> hash) {
        return client.hmset(key, hash);
    }

    public Set<String> zrevrange(String key, long start, long end) {
        return client.zrevrange(key, start, end);
    }

    public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
        return client.smove(srckey, dstkey, member);
    }

    public Long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return client.geoadd(key, memberCoordinateMap);
    }

    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return client.georadius(key, longitude, latitude, radius, unit, param);
    }

    public Double incrByFloat(String key, double value) {
        return client.incrByFloat(key, value);
    }

    public Long smove(String srckey, String dstkey, String member) {
        return client.smove(srckey, dstkey, member);
    }

    public Long bitop(BitOP op, String destKey, String... srcKeys) {
        return client.bitop(op, destKey, srcKeys);
    }

    public Long zinterstore(String dstkey, String... sets) {
        return client.zinterstore(dstkey, sets);
    }

    public Long del(String key) {
        return client.del(key);
    }

    public List<String> lrange(String key, long start, long end) {
        return client.lrange(key, start, end);
    }

    public Boolean hexists(byte[] key, byte[] field) {
        return client.hexists(key, field);
    }

    public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
        return client.rpoplpush(srckey, dstkey);
    }

    public Object eval(byte[] script, byte[] keyCount, byte[]... params) {
        return client.eval(script, keyCount, params);
    }

    public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
        return client.geopos(key, members);
    }

    public String scriptKill(byte[] key) {
        return client.scriptKill(key);
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return client.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Object evalsha(String sha1, int keyCount, String... params) {
        return client.evalsha(sha1, keyCount, params);
    }

    public Long rpushx(byte[] key, byte[]... arg) {
        return client.rpushx(key, arg);
    }

    public Long msetnx(byte[]... keysvalues) {
        return client.msetnx(keysvalues);
    }

    public Boolean setbit(String key, long offset, boolean value) {
        return client.setbit(key, offset, value);
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return client.zrevrangeByScore(key, max, min, offset, count);
    }

    public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return client.eval(script, keys, args);
    }

    public String scriptFlush(byte[] key) {
        return client.scriptFlush(key);
    }

    public String set(String key, String value, String nxxx, String expx, long time) {
        return client.set(key, value, nxxx, expx, time);
    }

    public byte[] srandmember(byte[] key) {
        return client.srandmember(key);
    }

    public List<String> geohash(String key, String... members) {
        return client.geohash(key, members);
    }

    public String hget(String key, String field) {
        return client.hget(key, field);
    }

    public Long srem(String key, String... member) {
        return client.srem(key, member);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public Double geodist(byte[] key, byte[] member1, byte[] member2) {
        return client.geodist(key, member1, member2);
    }

    public Set<byte[]> zrevrange(byte[] key, long start, long end) {
        return client.zrevrange(key, start, end);
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
        return client.zrangeByScore(key, min, max);
    }

    public Boolean getbit(String key, long offset) {
        return client.getbit(key, offset);
    }

    public Long persist(byte[] key) {
        return client.persist(key);
    }

    public Long setnx(byte[] key, byte[] value) {
        return client.setnx(key, value);
    }

    public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return client.geodist(key, member1, member2, unit);
    }

    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        return client.georadiusByMember(key, member, radius, unit);
    }

    public Long del(byte[]... keys) {
        return client.del(keys);
    }

    public long pfcount(String... keys) {
        return client.pfcount(keys);
    }

    public Long zlexcount(String key, String min, String max) {
        return client.zlexcount(key, min, max);
    }

    public Long hdel(String key, String... field) {
        return client.hdel(key, field);
    }

    public Long incr(String key) {
        return client.incr(key);
    }

    public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
        return client.hscan(key, cursor, params);
    }

    public Long zrem(byte[] key, byte[]... member) {
        return client.zrem(key, member);
    }

    public Boolean setbit(byte[] key, long offset, byte[] value) {
        return client.setbit(key, offset, value);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return client.zrevrangeByScoreWithScores(key, max, min);
    }

    public String mset(String... keysvalues) {
        return client.mset(keysvalues);
    }

    public Long zunionstore(String dstkey, ZParams params, String... sets) {
        return client.zunionstore(dstkey, params, sets);
    }

    public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return client.zremrangeByScore(key, start, end);
    }

    public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
        return client.evalsha(sha1, keyCount, params);
    }

    public Long expire(String key, int seconds) {
        return client.expire(key, seconds);
    }

    public Long pfadd(byte[] key, byte[]... elements) {
        return client.pfadd(key, elements);
    }

    public Long rpushx(String key, String... string) {
        return client.rpushx(key, string);
    }

    @Override
    public List<String> blpop(String s) {
        return client.blpop(s);
    }

    public List<String> sort(String key, SortingParams sortingParameters) {
        return client.sort(key, sortingParameters);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return client.zrevrangeByScoreWithScores(key, max, min);
    }

    public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
        return client.georadius(key, longitude, latitude, radius, unit);
    }

    public List<byte[]> brpop(int timeout, byte[]... keys) {
        return client.brpop(timeout, keys);
    }

    public Long rpush(String key, String... string) {
        return client.rpush(key, string);
    }

    public Long zremrangeByLex(String key, String min, String max) {
        return client.zremrangeByLex(key, min, max);
    }

    public void subscribe(JedisPubSub jedisPubSub, String... channels) {
        client.subscribe(jedisPubSub, channels);
    }

    public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return client.zinterstore(dstkey, params, sets);
    }

    public Long pexpireAt(String key, long millisecondsTimestamp) {
        return client.pexpireAt(key, millisecondsTimestamp);
    }

    public Long zrevrank(String key, String member) {
        return client.zrevrank(key, member);
    }

    public byte[] getrange(byte[] key, long startOffset, long endOffset) {
        return client.getrange(key, startOffset, endOffset);
    }

    public long pfcount(byte[] key) {
        return client.pfcount(key);
    }

    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        return client.zrangeByLex(key, min, max, offset, count);
    }

    public List<String> mget(String... keys) {
        return client.mget(keys);
    }

    public Long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return client.geoadd(key, longitude, latitude, member);
    }

    public Long zcard(String key) {
        return client.zcard(key);
    }

    public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
        client.psubscribe(jedisPubSub, patterns);
    }

    public Long zremrangeByRank(byte[] key, long start, long end) {
        return client.zremrangeByRank(key, start, end);
    }

    @Deprecated
    public String ping() {
        return client.ping();
    }

    public Long decr(String key) {
        return client.decr(key);
    }

    public Long strlen(String key) {
        return client.strlen(key);
    }

    public Long llen(String key) {
        return client.llen(key);
    }

    public Long hsetnx(String key, String field, String value) {
        return client.hsetnx(key, field, value);
    }

    public Set<String> zrangeByScore(String key, String min, String max) {
        return client.zrangeByScore(key, min, max);
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        return client.zrangeByScoreWithScores(key, min, max);
    }

    public List<String> blpop(int timeout, String key) {
        return client.blpop(timeout, key);
    }

    public void close() {
    }

    public Long expireAt(String key, long unixTime) {
        return client.expireAt(key, unixTime);
    }

    public List<String> hmget(String key, String... fields) {
        return client.hmget(key, fields);
    }

    public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        return client.bitop(op, destKey, srcKeys);
    }

    @Deprecated
    public String auth(String password) {
        return client.auth(password);
    }

    public Long pttl(String key) {
        return client.pttl(key);
    }

    public Double zincrby(String key, double score, String member) {
        return client.zincrby(key, score, member);
    }

    public Long incr(byte[] key) {
        return client.incr(key);
    }

    public Long sort(String key, String dstkey) {
        return client.sort(key, dstkey);
    }

    public Long hdel(byte[] key, byte[]... field) {
        return client.hdel(key, field);
    }

    public Double hincrByFloat(byte[] key, byte[] field, double value) {
        return client.hincrByFloat(key, field, value);
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        return client.hgetAll(key);
    }

    public Long zcount(byte[] key, double min, double max) {
        return client.zcount(key, min, max);
    }

    public Long hset(String key, String field, String value) {
        return client.hset(key, field, value);
    }

    public Long sdiffstore(String dstkey, String... keys) {
        return client.sdiffstore(dstkey, keys);
    }

    public Long expireAt(byte[] key, long unixTime) {
        return client.expireAt(key, unixTime);
    }

    public Set<byte[]> sdiff(byte[]... keys) {
        return client.sdiff(keys);
    }

    public byte[] substr(byte[] key, int start, int end) {
        return client.substr(key, start, end);
    }

    public Long hset(byte[] key, byte[] field, byte[] value) {
        return client.hset(key, field, value);
    }

    public List<byte[]> hmget(byte[] key, byte[]... fields) {
        return client.hmget(key, fields);
    }

    @Deprecated
    public String save() {
        return client.save();
    }

    public Long zcount(byte[] key, byte[] min, byte[] max) {
        return client.zcount(key, min, max);
    }

    public Long decr(byte[] key) {
        return client.decr(key);
    }

    public Long decrBy(String key, long integer) {
        return client.decrBy(key, integer);
    }

    public String setex(byte[] key, int seconds, byte[] value) {
        return client.setex(key, seconds, value);
    }

    public Long sinterstore(byte[] dstkey, byte[]... keys) {
        return client.sinterstore(dstkey, keys);
    }

    public String scriptLoad(String script, String key) {
        return client.scriptLoad(script, key);
    }

    public Boolean setbit(byte[] key, long offset, boolean value) {
        return client.setbit(key, offset, value);
    }

    public Long renamenx(byte[] oldkey, byte[] newkey) {
        return client.renamenx(oldkey, newkey);
    }

    public Long sinterstore(String dstkey, String... keys) {
        return client.sinterstore(dstkey, keys);
    }

    public Long zadd(String key, double score, String member, ZAddParams params) {
        return client.zadd(key, score, member, params);
    }

    @Deprecated
    public String quit() {
        return client.quit();
    }

    public Long linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot, byte[] value) {
        return client.linsert(key, where, pivot, value);
    }

    @Deprecated
    public Long getDB() {
        return client.getDB();
    }

    public Long zunionstore(String dstkey, String... sets) {
        return client.zunionstore(dstkey, sets);
    }

    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return client.zrangeByScore(key, min, max);
    }

    public String lset(String key, long index, String value) {
        return client.lset(key, index, value);
    }

    public List<byte[]> geohash(byte[] key, byte[]... members) {
        return client.geohash(key, members);
    }

    public Long zcard(byte[] key) {
        return client.zcard(key);
    }

    public Set<String> sdiff(String... keys) {
        return client.sdiff(keys);
    }

    public byte[] lpop(byte[] key) {
        return client.lpop(key);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public String spop(String key) {
        return client.spop(key);
    }

    public Long bitcount(String key) {
        return client.bitcount(key);
    }

    public Long setrange(byte[] key, long offset, byte[] value) {
        return client.setrange(key, offset, value);
    }

    public String ltrim(byte[] key, long start, long end) {
        return client.ltrim(key, start, end);
    }

    public Long lpushx(byte[] key, byte[]... arg) {
        return client.lpushx(key, arg);
    }

    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return client.zrevrangeByLex(key, max, min);
    }

    public byte[] echo(byte[] arg) {
        return client.echo(arg);
    }

    public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
        return client.pexpireAt(key, millisecondsTimestamp);
    }

    public Long lpush(String key, String... string) {
        return client.lpush(key, string);
    }

    public Collection<byte[]> hvals(byte[] key) {
        return client.hvals(key);
    }

    public Object eval(String script, String key) {
        return client.eval(script, key);
    }

    public Double hincrByFloat(String key, String field, double value) {
        return client.hincrByFloat(key, field, value);
    }

    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        return client.zincrby(key, score, member, params);
    }

    public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
        return client.zrevrangeByLex(key, max, min, offset, count);
    }

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        return client.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Long lpush(byte[] key, byte[]... args) {
        return client.lpush(key, args);
    }

    public String pfmerge(String destkey, String... sourcekeys) {
        return client.pfmerge(destkey, sourcekeys);
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return client.zrangeByScoreWithScores(key, min, max);
    }

    public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
        client.subscribe(jedisPubSub, channels);
    }

    public Boolean exists(byte[] key) {
        return client.exists(key);
    }

    public Long llen(byte[] key) {
        return client.llen(key);
    }

    public Long zlexcount(byte[] key, byte[] min, byte[] max) {
        return client.zlexcount(key, min, max);
    }

    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        return client.zrevrangeByScore(key, max, min, offset, count);
    }

    public Long pexpire(String key, long milliseconds) {
        return client.pexpire(key, milliseconds);
    }

    public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return client.zrangeByLex(key, min, max, offset, count);
    }

    public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
        client.psubscribe(jedisPubSub, patterns);
    }

    @Deprecated
    public String configResetStat() {
        return client.configResetStat();
    }

    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public Map<String, JedisPool> getClusterNodes() {
        return client.getClusterNodes();
    }

    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        return client.zrevrangeByLex(key, max, min, offset, count);
    }

    public Long zrevrank(byte[] key, byte[] member) {
        return client.zrevrank(key, member);
    }

    public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
        return client.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    @Deprecated
    public String info() {
        return client.info();
    }

    public Double geodist(String key, String member1, String member2) {
        return client.geodist(key, member1, member2);
    }

    public Long bitcount(byte[] key) {
        return client.bitcount(key);
    }

    public Long renamenx(String oldkey, String newkey) {
        return client.renamenx(oldkey, newkey);
    }

    public Long strlen(byte[] key) {
        return client.strlen(key);
    }

    public long pfcount(String key) {
        return client.pfcount(key);
    }

    @Deprecated
    public List<String> brpop(String arg) {
        return client.brpop(arg);
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
        return client.zrevrangeByScore(key, max, min, offset, count);
    }

    public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
        return client.zrangeWithScores(key, start, end);
    }

    @Deprecated
    public String select(int index) {
        return client.select(index);
    }

    public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
        return client.brpoplpush(source, destination, timeout);
    }

    public Object evalsha(String script, String key) {
        return client.evalsha(script, key);
    }

    public Long zinterstore(String dstkey, ZParams params, String... sets) {
        return client.zinterstore(dstkey, params, sets);
    }

    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        return client.hscan(key, cursor, params);
    }

    @Deprecated
    public ScanResult<Tuple> zscan(String key, int cursor) {
        return client.zscan(key, cursor);
    }

    public List<byte[]> lrange(byte[] key, long start, long end) {
        return client.lrange(key, start, end);
    }

    @Deprecated
    public String debug(DebugParams params) {
        return client.debug(params);
    }

    public Long publish(String channel, String message) {
        return client.publish(channel, message);
    }

    public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return client.zunionstore(dstkey, params, sets);
    }

    public Long hincrBy(byte[] key, byte[] field, long value) {
        return client.hincrBy(key, field, value);
    }

    @Deprecated
    public Long move(String key, int dbIndex) {
        return client.move(key, dbIndex);
    }

    public Long publish(byte[] channel, byte[] message) {
        return client.publish(channel, message);
    }

    public List<byte[]> mget(byte[]... keys) {
        return client.mget(keys);
    }

    @Deprecated
    public Long lastsave() {
        return client.lastsave();
    }

    public Long zadd(String key, double score, String member) {
        return client.zadd(key, score, member);
    }

    public Long zremrangeByScore(String key, String start, String end) {
        return client.zremrangeByScore(key, start, end);
    }

    public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return client.evalsha(sha1, keys, args);
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return client.zrevrangeByScore(key, max, min);
    }

    public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
        return client.pfmerge(destkey, sourcekeys);
    }

    public String lindex(String key, long index) {
        return client.lindex(key, index);
    }

    public Long setnx(String key, String value) {
        return client.setnx(key, value);
    }

    public Long hlen(byte[] key) {
        return client.hlen(key);
    }

    public Long geoadd(String key, double longitude, double latitude, String member) {
        return client.geoadd(key, longitude, latitude, member);
    }

    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        return client.georadius(key, longitude, latitude, radius, unit);
    }

    public Long zrank(String key, String member) {
        return client.zrank(key, member);
    }

    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        return client.hscan(key, cursor);
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return client.zrangeByScoreWithScores(key, min, max);
    }

    public Object eval(String script, List<String> keys, List<String> args) {
        return client.eval(script, keys, args);
    }

    public Long sadd(byte[] key, byte[]... member) {
        return client.sadd(key, member);
    }

    public String getSet(String key, String value) {
        return client.getSet(key, value);
    }

    public Set<String> spop(String key, long count) {
        return client.spop(key, count);
    }

    public Long bitpos(String key, boolean value, BitPosParams params) {
        return client.bitpos(key, value, params);
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String s, int i) {
        return client.hscan(s,i);
    }

    @Deprecated
    public ScanResult<String> sscan(String key, int cursor) {
        return client.sscan(key, cursor);
    }

    public Boolean sismember(String key, String member) {
        return client.sismember(key, member);
    }

    public String rename(String oldkey, String newkey) {
        return client.rename(oldkey, newkey);
    }

    public Long ttl(String key) {
        return client.ttl(key);
    }

    public List<String> brpop(int timeout, String... keys) {
        return client.brpop(timeout, keys);
    }

    public Object eval(byte[] script, byte[] key) {
        return client.eval(script, key);
    }

    public Set<String> smembers(String key) {
        return client.smembers(key);
    }

    public String getrange(String key, long startOffset, long endOffset) {
        return client.getrange(key, startOffset, endOffset);
    }

    public Long setrange(String key, long offset, String value) {
        return client.setrange(key, offset, value);
    }

    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        return client.zrevrangeWithScores(key, start, end);
    }

    public Object evalsha(String sha1, List<String> keys, List<String> args) {
        return client.evalsha(sha1, keys, args);
    }

    public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
        return client.zrevrangeByScore(key, max, min);
    }

    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        return client.sscan(key, cursor, params);
    }

    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return client.zadd(key, scoreMembers, params);
    }

    public Set<String> zrevrangeByLex(String key, String max, String min) {
        return client.zrevrangeByLex(key, max, min);
    }

    public Long bitpos(String key, boolean value) {
        return client.bitpos(key, value);
    }

    public byte[] getSet(byte[] key, byte[] value) {
        return client.getSet(key, value);
    }

    public String rpop(String key) {
        return client.rpop(key);
    }

    public String ltrim(String key, long start, long end) {
        return client.ltrim(key, start, end);
    }

    public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return client.zrangeByScore(key, min, max, offset, count);
    }

    public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
        return client.sscan(key, cursor);
    }

    public List<Long> scriptExists(byte[] key, byte[][] sha1) {
        return client.scriptExists(key, sha1);
    }

    public Long persist(String key) {
        return client.persist(key);
    }

    public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
        return client.georadiusByMember(key, member, radius, unit, param);
    }

    public List<String> hvals(String key) {
        return client.hvals(key);
    }

    public Set<byte[]> smembers(byte[] key) {
        return client.smembers(key);
    }

    public List<byte[]> sort(byte[] key) {
        return client.sort(key);
    }

    public Long del(String... keys) {
        return client.del(keys);
    }

    public Set<byte[]> zrange(byte[] key, long start, long end) {
        return client.zrange(key, start, end);
    }

    public Double zscore(String key, String member) {
        return client.zscore(key, member);
    }

    public Object eval(byte[] script, int keyCount, byte[]... params) {
        return client.eval(script, keyCount, params);
    }

    public ScanResult<Tuple> zscan(String key, String cursor) {
        return client.zscan(key, cursor);
    }

    public Long zunionstore(byte[] dstkey, byte[]... sets) {
        return client.zunionstore(dstkey, sets);
    }

    public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
        return client.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Long sdiffstore(byte[] dstkey, byte[]... keys) {
        return client.sdiffstore(dstkey, keys);
    }

    public List<byte[]> srandmember(byte[] key, int count) {
        return client.srandmember(key, count);
    }

    public Long pfcount(byte[]... keys) {
        return client.pfcount(keys);
    }

    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return client.geoadd(key, memberCoordinateMap);
    }

    public Long decrBy(byte[] key, long integer) {
        return client.decrBy(key, integer);
    }

    public Set<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
        return client.zrangeByScore(key, min, max, offset, count);
    }

    public Long incrBy(String key, long integer) {
        return client.incrBy(key, integer);
    }

    public Double zincrby(byte[] key, double score, byte[] member) {
        return client.zincrby(key, score, member);
    }

    public Long srem(byte[] key, byte[]... member) {
        return client.srem(key, member);
    }

    public List<String> sort(String key) {
        return client.sort(key);
    }

    public Boolean exists(String key) {
        return client.exists(key);
    }

    public Long lpushx(String key, String... string) {
        return client.lpushx(key, string);
    }

    public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
        return client.zrevrangeWithScores(key, start, end);
    }

    public Long ttl(byte[] key) {
        return client.ttl(key);
    }

    @Deprecated
    public String flushAll() {
        return client.flushAll();
    }

    public Long del(byte[] key) {
        return client.del(key);
    }

    public Long scard(byte[] key) {
        return client.scard(key);
    }

    public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return client.zadd(key, scoreMembers);
    }

    public Long msetnx(String... keysvalues) {
        return client.msetnx(keysvalues);
    }

    public String rename(byte[] oldkey, byte[] newkey) {
        return client.rename(oldkey, newkey);
    }

    public Long sunionstore(String dstkey, String... keys) {
        return client.sunionstore(dstkey, keys);
    }

    public Set<String> zrange(String key, long start, long end) {
        return client.zrange(key, start, end);
    }

    public Long lrem(byte[] key, long count, byte[] value) {
        return client.lrem(key, count, value);
    }

    public Long append(byte[] key, byte[] value) {
        return client.append(key, value);
    }

    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        return client.geodist(key, member1, member2, unit);
    }

    public Long zadd(byte[] key, double score, byte[] member) {
        return client.zadd(key, score, member);
    }

    public String psetex(String key, long milliseconds, String value) {
        return client.psetex(key, milliseconds, value);
    }

    public Long incrBy(byte[] key, long integer) {
        return client.incrBy(key, integer);
    }

    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
        return client.zscan(key, cursor, params);
    }

    public String type(String key) {
        return client.type(key);
    }

    public Set<String> zrevrangeByScore(String key, String max, String min) {
        return client.zrevrangeByScore(key, max, min);
    }

    public Long hlen(String key) {
        return client.hlen(key);
    }

    public Set<String> zrangeByScore(String key, double min, double max) {
        return client.zrangeByScore(key, min, max);
    }

    public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
        return client.zscan(key, cursor);
    }
}
