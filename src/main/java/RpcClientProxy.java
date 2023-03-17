import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//Rpc客户端动态代理
public class RpcClientProxy implements InvocationHandler {
//    private String host;
//    private int port;
    private final RpcClient client;

//    public RpcClientProxy(String host, int port){
//        this.host = host;
//        this.port = port;
//    }
    public RpcClientProxy(RpcClient client){
        this.client = client;
    }

    //抑制编译器产生警告信息
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        //创建代理对象
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    //重写invoke方法 invoke方法用来指明代理对象的方法被调用时的操作
    //这里显然是客户端需要生成一个RpcRequest对象发送出去
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        //客户端香服务端传输的对象， Builder模式生成利用反射获取相关信息
//        RpcRequest rpcRequest = RpcRequest.builder()
//                .interfaceName(method.getDeclaringClass().getName())
//                .methodName(method.getName())
//                .parameters(args)
//                .paramTypes(method.getParameterTypes())
//                .build();
//        //进行远程调用的客户端
//        SocketClient socketClient = new SocketClient();
//        //返回服务器的结果即可
//        return ((RpcResponse) socketClient.sendRequest(rpcRequest, host, port)).getData();
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return ((RpcResponse)client.sendRequest(rpcRequest)).getData();
    }
}
