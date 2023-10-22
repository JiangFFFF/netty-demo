package top.jiangffff.nio.c3_file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * FileChannel传输数据
 *
 * @author JiangHuifeng
 * @create 2023-10-19-21:55
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try (
                FileChannel from = new FileInputStream("data.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel();
        ) {
            // 效率高，底层应用操作系统的零拷贝进行优化，2g数据
            long size = from.size();
            // left变量代表还剩余多少字节
            for (long left = size; left > 0; ) {
                left -= from.transferTo(size - left, left, to);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
