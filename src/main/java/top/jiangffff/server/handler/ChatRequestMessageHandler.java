package top.jiangffff.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.jiangffff.message.ChatRequestMessage;
import top.jiangffff.message.ChatResponseMessage;
import top.jiangffff.server.session.SessionFactory;

/**
 * @author JiangHuifeng
 * @create 2023-10-29-09:37
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        if (channel != null) {
            // 在线
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else {
            // 不在线
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或不在线"));

        }
    }
}
