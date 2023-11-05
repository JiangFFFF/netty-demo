package top.jiangffff.protocol;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JiangHuifeng
 * @create 2023-11-05-09:37
 */
public class SequenceIdGenerator {

    private static final AtomicInteger id = new AtomicInteger();

    public static int nextId() {
        return id.incrementAndGet();
    }
}
