package netty.service;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import netty.client.TimeClientHandler;

public class TimeService {
	
	public void bind(int port) throws Exception {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workerGorup = new NioEventLoopGroup();
		try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGorup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildChannelHandler());
				//绑定端口，同步等待成功
				ChannelFuture f = b.bind(port).sync();
				//等待服务端监听端口关闭
				f.channel().closeFuture().sync();
		} finally {
			//优雅退出，释放线程池资源
			bossGroup.shutdownGracefully();
			workerGorup.shutdownGracefully();
		}
	}
	
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			System.out.println("成功进入");
			//解码器，以/n或/r/n作为结束符
			arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));	
			//将消息转化为string
			arg0.pipeline().addLast(new StringDecoder());	
			arg0.pipeline().addLast(new TimeClientHandler());
		}
		
	}
	
	
	public static void main(String[] args) throws Exception{
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
		}
		new TimeService().bind(port);
	}
}
