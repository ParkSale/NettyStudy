package com.example.nettyExample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    //네티에서 제공하는 채널인바운드핸들러어댑터를 상속받음으로서 필요한 기능들을 구현

    //그중 채널리드와 예외발생시 두가지를 구현
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //메세지를 수신했을 때 일어나는 메소드인데, 네티에서 사용하는 ByteBuf형을 사용
        //ByteBuf형에 대한 설명은 추후
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) {
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        //간단하게 입력받은 메시지를 출력함으로써 discard protocol이 제대로 작성되었는지 확인
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
