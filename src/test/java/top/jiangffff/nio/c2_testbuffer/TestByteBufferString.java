package top.jiangffff.nio.c2_testbuffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static top.jiangffff.nio.util.ByteBufferUtil.debugAll;

/**
 * @author JiangHuifeng
 * @create 2023-10-16-22:17
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        // 1、字符串转ByteBuffer
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        buffer1.put("hello".getBytes());
        debugAll(buffer1);

        // 2、Charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        debugAll(buffer2);

        // 3、wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        debugAll(buffer3);

        // 4、转字符串
        String str2 = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(str2);

        buffer1.flip();
        String str1 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str1);
    }
}
