package top.jiangffff.nio.c2_testbuffer;

import java.nio.ByteBuffer;

import static top.jiangffff.nio.util.ByteBufferUtil.debugAll;

/**
 * 网络上有多条数据发送给服务端，数据之间使用 \n 进行分隔
 * 但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
 * <p>
 * * Hello,world\n
 * * I'm zhangsan\n
 * * How are you?\n
 * <p>
 * 变成了下面的两个 byteBuffer (黏包，半包)
 * <p>
 * * Hello,world\nI'm zhangsan\nHo
 * * w are you?\n
 * <p>
 * 现在要求你编写程序，将错乱的数据恢复成原始的按 \n 分隔的数据
 *
 * @author JiangHuifeng
 * @create 2023-10-18-22:10
 */
public class TestByteBufferExam {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        //                     11            24
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);

        source.put("w are you?\n".getBytes());
        split(source);
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到一条完整消息
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                // 把这条完整消息存入新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从source读，向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                debugAll(target);
            }

        }
        // 未读部分向前移动，不会重头写
        source.compact();
    }

}
