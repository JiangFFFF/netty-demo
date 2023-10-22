package top.jiangffff.nio.c2_testbuffer;

import java.nio.ByteBuffer;

/**
 * @author JiangHuifeng
 * @create 2023-10-16-21:59
 */
public class TestByteBufferAllocate {
    public static void main(String[] args) {
        /**
         * class java.nio.HeapByteBuffer
         * java堆内存，读写效率较低，受到GC的影响
         */
        System.out.println(ByteBuffer.allocate(16).getClass());
        /**
         * class java.nio.DirectByteBuffer
         * 直接内存，读写效率高(少一次拷贝)，系统内存，不会受GC影响，分配的效率低
         */
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}
