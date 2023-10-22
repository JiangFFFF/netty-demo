package top.jiangffff.nio.c2_testbuffer;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author JiangHuifeng
 * @create 2023-10-15-10:53
 */
@Slf4j
public class TestByteBuffer {

    public static void main(String[] args) {
        /**
         * FileChannel
         * 1、输入输出流
         * 2、RandomAccessFile
         */
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                // 从channel读取数据(向buffer写入)
                int len = channel.read(buffer);
                log.debug("读取到的字节数 {}", len);
                // 没有内容需退出
                if (len == -1) {
                    break;
                }
                /**
                 * 打印buffer内容
                 */
                // 切换至读模式(将position的位置重置为0)
                buffer.flip();
                // 检查是否还有剩余未读数据
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    log.debug("实际字节 {}", (char) b);
                }
                // 切换为写模式
                buffer.clear();
            }
        } catch (IOException e) {

        }


    }

}
