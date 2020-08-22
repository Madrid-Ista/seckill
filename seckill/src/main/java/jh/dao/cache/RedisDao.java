package jh.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import jh.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;
    private Jedis jedis;
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port){
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId){
        //redis操作逻辑

        try{
            jedis = jedisPool.getResource();
            String key = "seckill:" + seckillId;
            // 没有实现内部序列化
            // get -> byte[] -> 反序列化 -> Object(Seckill)
            // 采用自定义序列化
            // protostuff : pojo
            byte[] bytes = jedis.get(key.getBytes());
            // 缓存获取到
            if(bytes != null){
                // 空对象
                Seckill seckill = schema.newMessage();
                // seckill被反序列化
                ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                return seckill;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }finally {
            jedis.close();
        }

        return null;
    }

    public String putSeckill(Seckill seckill){
        // set Object -> 序列化-> byte[] -> 发送给redis
        try{
            jedis = jedisPool.getResource();

            String key = "seckill:" + seckill.getSeckillId();
            byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
            // 超时缓存
            int timeout = 60 * 60;
            String result = jedis.setex(key.getBytes(), timeout, bytes);
            return result;
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }finally {
            jedis.close();
        }
        return null;
    }
}
