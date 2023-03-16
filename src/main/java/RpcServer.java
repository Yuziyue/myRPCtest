import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.concurrent.*;

import  java.net.ServerSocket;
public class RpcServer {

    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry serviceRegistry;


//    public RpcServer() {
//        int corePoolSize = 5;
//        int maximumPoolSize = 50;
//        long keepAliveTime = 60;
//        //设置上限为100个线程的阻塞队列
//        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
//        ThreadFactory threadFactory = Executors.defaultThreadFactory();
//        //用线程池创建线程池
//        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
//    }

    public RpcServer(ServiceRegistry serviceRegistry){  //创建RpcServer时候需要传入一个已经注册好服务的ServiceRegistry
        this.serviceRegistry = serviceRegistry;
        //设置上限100个线程的阻塞队列
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //用线程池创建线程池
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
    }
//    这里不再是简单的注册而是多个的情况
//    public void register(Object service, int port){      //简化版 暂时只能注册一个接口（对外提供一个接口的调用服务）
//        try(ServerSocket serverSocket = new ServerSocket(port)){    //使用一个ServerSocket监听某个接口 创建服务器端的ServerSocket对象等待客户端链接
//            //利用ServerSocket监听与客户端发出请求的一致端口，连接到客户端Socket，循环接收请求
//            logger.info("服务器正在启动.....");
//            Socket socket;
//            while((socket=serverSocket.accept())!=null){     //侦听客户端，当未接受到连接请求时，accept()会一直阻塞
//                logger.info("客户端连接！Ip为：" + socket.getInetAddress());
//                threadPool.execute(new WorkerThread(socket, service));   //启动线程 向工作线程WorkerThread传入了socket和用于服务端实例service
//            }
//        }catch (IOException e) {
//            logger.error("连接时有错误发生：", e);
//        }
//    }

    //服务端启动
    //原来的 register 方法也被改成了 start 方法，因为服务的注册已经不由 RpcServer 处理了，它只需要启动就行了
    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){ //使用一个ServerSocket监听某个接口 创建服务器端的ServerSocket对象等待客户端链接
            logger.info("服务器启动....");
            Socket socket;
            //侦听客户端，当未接受到连接请求时，accept()会一直阻塞
            while ((socket = serverSocket.accept() )!= null){
                logger.info("客户端链接！{}：{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceRegistry));
            }
            threadPool.shutdown();
        }catch (IOException e){
            logger.info("服务器启动时有错误发生：",e);
        }
    }

}
