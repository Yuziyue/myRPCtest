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

    public RpcServer() {
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //用线程池创建线程池
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    public void register(Object service, int port){      //简化版 暂时只能注册一个接口（对外提供一个接口的调用服务）
        try(ServerSocket serverSocket = new ServerSocket(port)){    //使用一个ServerSocket监听某个接口 创建服务器端的ServerSocket对象等待客户端链接
            logger.info("服务器正在启动.....");
            Socket socket;
            while((socket=serverSocket.accept())!=null){     //侦听客户端
                logger.info("客户端连接！Ip为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, service));   //启动线程 向工作线程WorkerThread传入了socket和用于服务端实例service
            }
        }catch (IOException e) {
            logger.error("连接时有错误发生：", e);
        }
    }
}
