package top.jiangffff.nio.c4_nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static top.jiangffff.nio.util.ByteBufferUtil.debugAll;

/**
 * 异步io
 *
 * @author JiangHuifeng
 * @create 2023-10-21-23:18
 */
@Slf4j
public class AIOFileChannel {
    public static void main(String[] args) {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
            /**
             * 参数1：ByteBuffer
             * 参数2：读取的起始位置
             * 参数3：附件
             * 参数4：回调对象 CompletionHandler
             */
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.warn("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                /**
                 * 正确读取执行
                 * @param result 实际读取的字节数
                 * @param attachment 附件
                 */
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    log.warn("read completed...{}", result);
                    attachment.flip();
                    debugAll(attachment);
                }

                /**
                 * read失败执行
                 * @param exc
                 * @param attachment
                 */
                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            log.warn("read end...");
            Thread.sleep(2000);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
