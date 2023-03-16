
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class HelloServiceImpl implements HelloService{ //一个非抽象类实现接口，这个类必须重写这个接口的所有的方法

    // 使用HelloServiceImpl初始化日志对象，方便在日志输出的时候，可以打印出日志信息所属的类。

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(HelloObject object){   //重写接口种的hello方法
        //使用{}可以直接将getMessage()内容输出
        logger.info("接收到：{}",object.getMessage());
        return "这是调用的返回值，id="+object.getId();
    }
}
