package top.jiangffff.source;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JiangHuifeng
 * @create 2023-10-29-13:52
 */
@Slf4j
public class TestConnectionTimeout {
    public static void main(String[] args) {
        /**
         * 1、客户端
         *      通过 .option() 方法配置参数
         * 2、服务器端
         *      new ServerBootstrap().option(); 是给 ServerSocketChannel 配置参数
         *      new ServerBootstrap().childOption(); 是给 SocketChannel 配置参数
         */
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                    .handler(new LoggingHandler());
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
            future.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.debug("timeout");
        } finally {
            group.shutdownGracefully();
        }
    }
}
