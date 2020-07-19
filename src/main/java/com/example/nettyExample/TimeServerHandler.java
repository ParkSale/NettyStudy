package com.example.nettyExample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    //네티에서 제공하는 채널인바운드핸들러어댑터를 상속받음으로서 필요한 기능들을 구현

    //그중 채널리드와 예외발생시 두가지를 구현

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        //ChannelRead를 쓰지 않아도 됨
        //TimeServer는 32비트 정수형 하나를 전송하고 바로 연결을 끊을거기 때문에
        //전송을 channelActive에서 진행
        final ByteBuf time = ctx.alloc().buffer(4); //메세지를 보내기 위해 선언 32비트 정수 하나 전송이므로 4바이트버퍼필요
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        });
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
