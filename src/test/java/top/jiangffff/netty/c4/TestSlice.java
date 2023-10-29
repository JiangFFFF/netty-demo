package top.jiangffff.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * 零拷贝 Slice
 *
 * @author JiangHuifeng
 * @create 2023-10-25-22:37
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        TestByteBuf.log(buf);

        // 在切片过程中没有发生数据复制
        ByteBuf f1 = buf.slice(0, 5);
        // 引用计数+1
        f1.retain();

        ByteBuf f2 = buf.slice(5, 5);
        f1.retain();

        TestByteBuf.log(f1); // abcde
        TestByteBuf.log(f2); // fghij

        System.out.println("释放原有 byteBuf 内存");
        buf.release();
        TestByteBuf.log(f1);

        f1.release();
        f2.release();

        /*f1.setByte(0,'b');
        TestByteBuf.log(f1); // bbcde
        TestByteBuf.log(buf); // bbcdefghij*/

    }
}
