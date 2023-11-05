package top.jiangffff.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import top.jiangffff.message.RpcRequestMessage;
import top.jiangffff.protocol.MessageCodecSharable;
import top.jiangffff.protocol.PrototolFrameDecoder;
import top.jiangffff.protocol.SequenceIdGenerator;
import top.jiangffff.server.handler.RpcResponseMessageHandler;
import top.jiangffff.server.service.HelloService;

import java.lang.reflect.Proxy;

/**
 * @author JiangHuifeng
 * @create 2023-11-04-15:31
 */
@Slf4j
public class RpcClientManager {
    private static Channel channel = null;
    private static final Object Lock = new Object();

    public static void main(String[] args) {
        /*getChannel().writeAndFlush(new RpcRequestMessage(1,
                "top.jiangffff.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"张三"}));*/

        HelloService service = getProxyService(HelloService.class);
        service.sayHello("里斯");
        service.sayHello("王武");

    }

    // 创建代理类
    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        Object o = Proxy.newProxyInstance(loader, interfaces, ((proxy, method, args) -> {
            // 1、将方法调用转为消息对象
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            // 2、将消息对象发送出去
            getChannel().writeAndFlush(msg);

            // 3、准备promise对象来接收结果，指定promise对象异步接收结果线程
            DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
            RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);
            // 4、等待 promise 结果
            promise.await();
            if (promise.isSuccess()) {
                // 调用正常
                return promise.getNow();
            } else {
                // 调用失败
                throw new RuntimeException(promise.cause());
            }
        }));
        return (T) o;
    }

    /**
     * 单例获取channel
     *
     * @return
     */
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (Lock) {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    /**
     * 初始化channel
     */
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();

        // rpc 响应消息处理器，待实现
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new PrototolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }
}
