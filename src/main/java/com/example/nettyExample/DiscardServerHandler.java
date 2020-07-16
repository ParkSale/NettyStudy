package com.example.nettyExample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    //네티에서 제공하는 채널인바운드핸들러어댑터를 상속받음으로서 필요한 기능들을 구현

    //그중 채널리드와 예외발생시 두가지를 구현
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //메세지를 수신했을 때 일어나는 메소드인데, 네티에서 사용하는 ByteBuf형을 사용
        //ByteBuf형에 대한 설명은 추후
        ((ByteBuf)msg).release();
        //DiscardProtocol은 메세지를 수신하면 그냥 갖다 버리는 프로토콜이므로 바로 그냥 release해줌
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
