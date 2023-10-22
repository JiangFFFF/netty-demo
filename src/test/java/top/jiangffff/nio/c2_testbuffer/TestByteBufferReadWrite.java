package top.jiangffff.nio.c2_testbuffer;

import java.nio.ByteBuffer;

import static top.jiangffff.nio.util.ByteBufferUtil.debugAll;


/**
 * @author JiangHuifeng
 * @create 2023-10-15-22:47
 */
public class TestByteBufferReadWrite {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte) 0x61);
        buffer.put(new byte[]{0x62 ,0x63,0x64});
        debugAll(buffer);
//        System.out.println(buffer.get());
        buffer.flip();
        System.out.println(buffer.get());
        debugAll(buffer);
        // 未读部分向前移动，不会重头写
        buffer.compact();
        debugAll(buffer);
        buffer.put(new byte[]{0x65 ,0x66});
        debugAll(buffer);
    }
}
