package top.jiangffff.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.jiangffff.message.GroupChatRequestMessage;
import top.jiangffff.message.GroupChatResponseMessage;
import top.jiangffff.server.session.GroupSessionFactory;

import java.util.List;

/**
 * @author JiangHuifeng
 * @create 2023-10-29-10:23
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        List<Channel> channels = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        for (Channel channel : channels) {
            channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getContent()));
        }
    }
}
