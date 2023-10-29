package top.jiangffff.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import top.jiangffff.message.LoginRequestMessage;

/**
 * @author JiangHuifeng
 * @create 2023-10-28-17:10
 */
public class TestMessageCodec {
    public static void main(String[] args) throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new MessageCodec());
        /**
         * encode
         */
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
//        channel.writeOneOutbound(message);

        /**
         * decode
         */
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);
        // 入站
//        channel.writeInbound(buf);

        ByteBuf s1 = buf.slice(0, 100);
        ByteBuf s2 = buf.slice(100, buf.readableBytes() - 100);
        buf.retain();
        channel.writeInbound(s1); // release 0
        channel.writeInbound(s2);



    }
}
