import com.itheima.gossip.utils.JedisUtils;
import redis.clients.jedis.Jedis;

public class TopKeyToRedis {
    public static void main(String[] args) {
        Jedis jedis = JedisUtils.getJedis();
        jedis.zadd("bigData:gossip:topkey",3449233,"赵薇败诉");
        jedis.zadd("bigData:gossip:topkey",2458008,"霍顿队友主动和孙杨握手");
        jedis.zadd("bigData:gossip:topkey",2228726,"水自由");
        jedis.zadd("bigData:gossip:topkey",2135075,"230万用户完成携号转网");
        jedis.zadd("bigData:gossip:topkey",2086094,"吴佩慈怀四胎");
        jedis.zadd("bigData:gossip:topkey",1233284,"霍顿父亲发声");
        jedis.zadd("bigData:gossip:topkey",1194326,"李沁造型");
        jedis.zadd("bigData:gossip:topkey",1112676,"移动回应逝者手机号过户问题");
        jedis.zadd("bigData:gossip:topkey",1108352,"强化严重精神障碍患者管理");
        jedis.zadd("bigData:gossip:topkey",1108239,"外交部回应孙杨夺冠霍顿拒上领奖台");
        jedis.zadd("bigData:gossip:topkey",1108218,"连续50天被夸奖的女孩子");
        jedis.zadd("bigData:gossip:topkey",917252,"这个夏天最忙的三个人");
        jedis.zadd("bigData:gossip:topkey",544254,"baby回应机场事件");
        jedis.zadd("bigData:gossip:topkey",477143,"孟美岐演碧瑶");
        jedis.zadd("bigData:gossip:topkey",466879,"兄嫂本无缘全靠我花钱");
        jedis.zadd("bigData:gossip:topkey",385405,"杨紫呼吁不要提前散播剧集");
        jedis.zadd("bigData:gossip:topkey",384148,"梁洛施");

        jedis.close();
    }
}
