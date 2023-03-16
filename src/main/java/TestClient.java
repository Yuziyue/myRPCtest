public class TestClient {
    public static void main(String[] args) {
        //接口与代理对象之间的中介对象
        //由于在客户端没有实现接口的具体实现类，就没有办法直接生成实例对象
        //可以通过动态代理的方式生成实例
        RpcClientProxy proxy = new RpcClientProxy("127.0.0.1", 9000);
        //创建代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        //接口方法的参数对象
        HelloObject object = new HelloObject(12, "This is test message");
        //由动态代理可知，代理对象调用hello()实际会执行invoke()
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
