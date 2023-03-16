import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServiceRegistry{
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    //key = 服务名称(即接口名), value = 服务实体(即实现类的实例对象)
    //服务名与提供服务的对象的对应关系保存在一个 ConcurrentHashMap 中
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();  //ConcurrentHashMap()线程安全
    //用来存放实现类的名称，Set存取更高效，存放实现类名称相比存放接口名称占的空间更小，因为一个实现类可能实现了多个接口
    //用一个 Set 来保存当前有哪些对象已经被注册
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void register(T service) {   //synchronized是Java中的关键字，是一种同步锁 保证线程同步
        String serviceImplName = service.getClass().getCanonicalName(); //默认采用这个对象实现的接口的完整类作为服务名 每个接口只会对应一个对象
        if(registeredService.contains(serviceImplName)){
            return;
        }
        registeredService.add(serviceImplName);
        //可能实现了多个接口，故使用Class数组接收
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> i : interfaces){
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务：{}", interfaces, serviceImplName);
    }

    //获取服务的对象，直接去Map查找就可以
    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
