package top.jiangffff.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * ChannelFuture配合异步方法处理结果
 *
 * @author JiangHuifeng
 * @create 2023-10-22-10:32
 */
@Slf4j
public class EventLoopClientChannelFuture {
    public static void main(String[] args) throws InterruptedException {
        // 带有 Future，Promise 的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞，main 发起调用，真正执行 connect 是 nio线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 2.1 使用 sync 方法同步处理结果
        /*channelFuture.sync(); // 阻塞住当前线程，直到nio线程建立完毕
        // 无阻塞向下执行获取 channel
        Channel channel = channelFuture.channel();// 代表连接对象
        log.debug("{}", channel);
        channel.writeAndFlush("hello ,world");*/

        // 2.2 使用 addListener(回调对象) 方法异步处理结果
        channelFuture.addListener(new ChannelFutureListener() {
            /**
             * 在 nio 线程连接建立之后，会调用 operationComplete
             * @param future
             * @throws Exception
             */
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel channel = future.channel();
                log.debug("{}", channel);
                channel.writeAndFlush("hello ,world");
            }
        });


    }
}
