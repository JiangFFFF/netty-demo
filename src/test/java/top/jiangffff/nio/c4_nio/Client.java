package top.jiangffff.nio.c4_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author JiangHuifeng
 * @create 2023-10-21-09:48
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
//        sc.write(Charset.defaultCharset().encode("hello\nworld\n"));
        sc.write(Charset.defaultCharset().encode("0123456789abcdef"));
        System.out.println("waiting...");
    }

}
