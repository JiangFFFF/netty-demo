package top.jiangffff.nio.c2_testbuffer;

import java.nio.ByteBuffer;

import static top.jiangffff.nio.util.ByteBufferUtil.debugAll;

/**
 * @author JiangHuifeng
 * @create 2023-10-16-22:06
 */
public class TestByteBufferRead {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();

        // rewind 从头开始读
//        buffer.get(new byte[4]);
//        debugAll(buffer);
//        buffer.rewind();
//        System.out.println((char)buffer.get());

        // mark & resrt
        // mark做标记，记录position位置， reset是将position重置到mark的位置
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());
//        // 加标记，索引2的位置
//        buffer.mark();
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());
//        // 将position重置到索引2
//        buffer.reset();
//        System.out.println((char) buffer.get());
//        System.out.println((char) buffer.get());

        // get(i) 不会改变读索引的位置
        System.out.println((char) buffer.get(3));
        debugAll(buffer);

    }

}
