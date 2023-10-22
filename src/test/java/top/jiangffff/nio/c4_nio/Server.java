package top.jiangffff.nio.c4_nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static top.jiangffff.nio.util.ByteBufferUtil.debugAll;
import static top.jiangffff.nio.util.ByteBufferUtil.debugRead;

/**
 * 使用nio理解阻塞模式
 * 事件类型：
 * accept：会在连接请求式触发
 * connected：是 客户端 建立连接后触发
 * read：可读事件
 * write：可写事件
 *
 * @author JiangHuifeng
 * @create 2023-10-21-09:34
 */
@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 1、创建selector，管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // 2、建立selector和channel的联系（注册）
        // SelectionKey事件发生后，通过它可以知道事件和哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // sscKey只关注 accept 事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("register key:{}", sscKey);

        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            // 3、select方法，没有事件发生时线程阻塞，有事件发生线程才会恢复运行
            // select 在事件未处理时不会阻塞；事件发生后要么处理，要么取消，不能置之不理
            selector.select();
            // 4、处理事件,selectedKeys 内包含了所有发生的事件
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                // 处理key时，需要从selectKeys集合中删除，否则下次处理就会有问题
                iter.remove();
                log.debug("key:{}", key);
                // 5、区分事件类型
                if (key.isAcceptable()) {
//                    key.channel();
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    // 将一个ByteBuffer作为附件关联到SelectionKey上
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("{}", sc);
                } else if (key.isReadable()) {
                    // 拿到触发事件的channel
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        // 获取SelectionKey上关联的附件
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        // 如果是正常断开，read的方法的返回值是-1
                        int read = channel.read(buffer);
                        if (read == -1) {
                            key.cancel();
                        } else {
//                            buffer.flip();
//                            debugRead(buffer);
//                            System.out.println(Charset.defaultCharset().decode(buffer));
                            split(buffer);
                            if (buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 因为客户端断开了，因此需要将key取消（从selectKeys集合中真正删除）
                        key.cancel();
                    }
                }


            }

        }


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

    /**
     * 非阻塞式
     *
     * @throws IOException
     */
    private static void originMethod() throws IOException {
        // 0、ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // 1、创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 非阻塞模式，影响accept方法
        ssc.configureBlocking(false);

        // 2、绑定监听端口
        ssc.bind(new InetSocketAddress(8080));

        // 3、连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
            // 4、accept，建立客户端连接，SocketChannel用来与客户端通信
//            log.debug("connecting....");
            // 阻塞方法，线程停止运行，如果没有建立连接，但sc为null
            SocketChannel sc = ssc.accept();
            if (sc != null) {
                log.debug("connected....{}", sc);
                // 非阻塞模式，影响read方法
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                // 5、接收客户端发送的数据
//                log.debug("before read...{}", channel);
                // 阻塞方法，线程停止运行;非阻塞情况下线程仍然会运行，如果没有读到数据，read返回0
                int read = channel.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.debug("after read...{}", channel);
                }
            }
        }
    }
}
