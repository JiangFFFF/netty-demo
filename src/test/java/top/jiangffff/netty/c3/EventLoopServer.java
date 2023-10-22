package top.jiangffff.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author JiangHuifeng
 * @create 2023-10-22-11:47
 */
@Slf4j
public class EventLoopServer {
    public static void main(String[] args) {
        // 细分2：创建独立的 EventLoopGroup
        EventLoopGroup group = new DefaultEventLoopGroup();
        new ServerBootstrap()
                // 细分1：boss 只负责 ServerSocketChannel 上的 accpet 事件； worker负责 socketChannel 上的读写
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                            /**
                             *
                             * @param ctx
                             * @param msg ByteBuf
                             * @throws Exception
                             */
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                                // 将消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                log.debug(byteBuf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
