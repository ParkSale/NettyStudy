package com.example.nettyExample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


public class NettyExampleApplication {
	private int port;

	public NettyExampleApplication(int port){
		this.port = port;
	}

	public void run() throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//NioEventLoopGroup은 I/O작업을 다루는 멀티쓰레드환경 이벤르투프
		//EventLoopGroup에 대해서 여러 구현체를 제공, 그 중 NioEventLoopGroup을 사용
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		//boss와 worker두개를 설정
		//보스는 incoming connection을 받는 역할, 워커는 보스가 수락한 커넥션 트래픽 처리
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new DiscardServerHandler());
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}
	public static void main(String[] args) throws Exception{

		int port = 8080;
		if(args.length > 0){
			port = Integer.parseInt(args[0]);
		}

		new NettyExampleApplication(port).run();
	}

}
