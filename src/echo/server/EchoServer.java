package echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import messagePack.MsgpackDecoder;
import messagePack.MsgpackEncoder;

public class EchoServer {
	
	private final int port;
	private final int sendNumber;
	
	public EchoServer(int port, int sendNumber) {
		this.port = port;
		this.sendNumber = sendNumber;
	}

	public void bind() throws Exception{
		//���÷���˵�NIO�߳���
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
					// TODO Auto-generated method stub
					//�ַ�������
					/*ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
					arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));*/
					
					/*//���Ƚ�����
					arg0.pipeline().addLast(new FixedLengthFrameDecoder(20));
					arg0.pipeline().addLast(new StringDecoder());*/
					
					arg0.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
					arg0.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
					arg0.pipeline().addLast(new EchoServerHandler(sendNumber));
				}
			});
			//�󶨶˿ڵȴ�ͬ���ɹ�
			ChannelFuture f = b.bind(port).sync();
			//�ȴ�����˼����˹ر�
			f.channel().closeFuture().sync();
		} finally {
			//�����˳����ͷ��̳߳���Դ
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
		int sendNumber = 8;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new EchoServer(port,sendNumber).bind();
	}
}
