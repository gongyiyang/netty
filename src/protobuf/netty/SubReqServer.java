package protobuf.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import protobuf.SubscribeReqProto;
import sun.applet.Main;

public class SubReqServer {
	
	public void buind(int port) throws InterruptedException{
		//配置服务端线程组
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					arg0.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
					arg0.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					arg0.pipeline().addLast(new ProtobufEncoder());
					arg0.pipeline().addLast(new SubReqServerHandler());
					
				}
			});
			//绑定端口，等待同步成功
			ChannelFuture f = b.bind(port).sync();
			//等待服务端监听端口关闭
			f.channel().closeFuture().sync();
			
		} finally {
			// TODO: handle finally clause
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		if (args != null && args.length>0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new SubReqServer().buind(port);
	}
}
