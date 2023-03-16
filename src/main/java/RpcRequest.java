import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
@Data
//创建者模式，一次性给所有变量初始赋值
@Builder

//传输协议
//在客户端和服务端之间的传输的数据规定一个固定格式
//肯定要知道接口的名字和方法，由于方法重载，需要这个方法的所有参数的类型（泛型）
public class RpcRequest implements Serializable {
    /**
     * 待调用接口名称
     */
    private String interfaceName;
    /**
     * 待调用方法名称
     */
    private String methodName;
    /**
     * 调用方法的参数
     */
    private Object[] parameters;
    /**
     * 调用方法的参数类型
     */
    private Class<?>[] paramTypes;
}
