package top.jiangffff.netty.c5;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import top.jiangffff.message.LoginRequestMessage;
import top.jiangffff.protocol.MessageCodecSharable;

/**
 * @author JiangHuifeng
 * @create 2023-10-29-13:42
 */
public class TestSerializer {
    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123");
        channel.writeOutbound(message);
    }


}
