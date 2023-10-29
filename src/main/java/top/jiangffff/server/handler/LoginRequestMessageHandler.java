package top.jiangffff.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import top.jiangffff.message.LoginRequestMessage;
import top.jiangffff.message.LoginResponseMessage;
import top.jiangffff.server.service.UserServiceFactory;
import top.jiangffff.server.session.SessionFactory;

/**
 * @author JiangHuifeng
 * @create 2023-10-29-09:35
 */
@Slf4j
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(username, password);
        LoginResponseMessage message;
        if (login) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            message = new LoginResponseMessage(login, "登录成功");
        } else {
            message = new LoginResponseMessage(login, "用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
