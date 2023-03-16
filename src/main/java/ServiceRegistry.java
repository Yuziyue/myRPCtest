
//在前面的简单demo基础之上，将服务的注册和服务器启动相分离，这样服务端就可以提供多个服务
public interface ServiceRegistry {  //实现一个容器，保存服务端提供的所有服务，这样就可以通过服务名字来返回这个服务的具体信息
                                    //也就是利用接口名字获取到具体接口实现类对象
     //将一个服务注册进注册表
    <T> void register(T service);

    //@description 根据服务名获取服务实体
    Object getService(String serviceName);
}
