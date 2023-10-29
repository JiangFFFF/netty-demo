package top.jiangffff.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import top.jiangffff.message.Message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * 消息编解码器
 *
 * @author JiangHuifeng
 * @create 2023-10-28-16:41
 */
@Slf4j
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        // 1、4 字节的魔数（用来第一时间判定是否是无效数据包）
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2、1 字节的版本
        out.writeByte(1);
        // 3、1 字节的序列化方式 jdk 0 ，json 1
        out.writeByte(0);
        // 4、1 字节的指令类型
        out.writeByte(msg.getMessageType());
        // 5、4 字节的指令序号
        out.writeInt(msg.getSequenceId());
        // 无意义，对齐填充
        out.writeByte(0xff);
        // 6、获取内容的字节数组
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();
        // 7、长度
        out.writeInt(bytes.length);
        // 8、内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int magicNum = in.readInt();
        byte version = in.readByte();
        byte serializerType = in.readByte();
        byte messageType = in.readByte();
        int sequencedId = in.readInt();
        in.readByte();
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        Message message = null;
        if (serializerType == 0) {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            message = (Message) ois.readObject();
        }
        log.debug("{}, {}, {}, {}, {}, {}", magicNum, version, serializerType, messageType, sequencedId, length);
        log.debug("{}", message);
        out.add(messageType);
    }
}
