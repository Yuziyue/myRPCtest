public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        RpcServer rpcServer = new RpcServer();
        //注册HelloServiceImpl服务
        rpcServer.register(helloService, 9000);
    }
}


//客户端和服务端都可以访问到通用的接口(HelloService)
// 但是只有服务端有这个接口的实现类(HelloServiceImpl)
// 客户端调用这个接口的方式，是通过网络传输，告诉服务端我要调用这个接口，
// 服务端收到之后找到这个接口的实现类，并且执行，将执行的结果返回给客户端，
// 客户端拿到返回的结果，结束。
