package top.jiangffff.protocol;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author JiangHuifeng
 * @create 2023-10-28-22:29
 */
public class PrototolFrameDecoder extends LengthFieldBasedFrameDecoder {

    public PrototolFrameDecoder() {
        this(1024, 12, 4, 0, 0);
    }

    public PrototolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
