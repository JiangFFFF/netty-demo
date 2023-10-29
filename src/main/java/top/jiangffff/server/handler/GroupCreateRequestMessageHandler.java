package top.jiangffff.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.jiangffff.message.GroupCreateRequestMessage;
import top.jiangffff.message.GroupCreateResponseMessage;
import top.jiangffff.server.session.Group;
import top.jiangffff.server.session.GroupSession;
import top.jiangffff.server.session.GroupSessionFactory;

import java.util.List;
import java.util.Set;

/**
 * @author JiangHuifeng
 * @create 2023-10-29-10:09
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        // 群管理器
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group == null) {
            // 发送创建群成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, groupName + " 创建成功"));

            // 发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true, "您已被拉入 " + groupName));
            }
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, groupName + " 已经存在"));
        }
    }
}
