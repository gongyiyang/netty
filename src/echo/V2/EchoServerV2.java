package echo.V2;

import echo.server.EchoServer;
import echo.server.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import messagePack.MsgpackDecoder;
import messagePack.MsgpackEncoder;

public class EchoServerV2 {
	
	private int port;
	private int sendNumber;
	public EchoServerV2(int port, int sendNumber) {
		this.port = port;
		this.sendNumber = sendNumber;
	}
	
	public void run() throws InterruptedException{
		
		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 2,0,2));
					arg0.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
					arg0.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
					arg0.pipeline().addLast("msgpack Encoder",new MsgpackEncoder());
					arg0.pipeline().addLast(new EchoServerHandler(sendNumber));
				}
			});
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			// TODO: handle finally clause
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		int sendNumber = 100;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new EchoServerV2(port,sendNumber).run();
	}
}
