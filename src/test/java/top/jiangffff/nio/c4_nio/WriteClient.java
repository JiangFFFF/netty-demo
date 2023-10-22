package top.jiangffff.nio.c4_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author JiangHuifeng
 * @create 2023-10-21-16:10
 */
public class WriteClient {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));

        // 3、接收数据
        int count = 0;
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            int read = sc.read(buffer);
            count += read;
            System.out.println(count);
            buffer.clear();
        }
    }
}
