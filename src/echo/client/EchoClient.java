package echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import messagePack.MsgpackDecoder;
import messagePack.MsgpackEncoder;

public class EchoClient {
	
	private final String host;
	private final int port;
	private final int sendNumber;
	

	public EchoClient(String host, int port, int sendNumber) {
		this.host = host;
		this.port = port;
		this.sendNumber = sendNumber;
	}

	public void connect() throws InterruptedException {
		// 配置客户端线程组
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel arg0) throws Exception {
							// TODO Auto-generated method stub
							/*ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
							arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							arg0.pipeline().addLast(new StringDecoder());*/
							
							arg0.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
							arg0.pipeline().addLast("msgpack encoder",new MsgpackEncoder());
							arg0.pipeline().addLast(new EchoClientHandler(sendNumber));
						}
					});
			// 发起异步连接
			ChannelFuture f = b.connect(host, port).sync();
			// 等待客户端链路关闭
			f.channel().closeFuture().sync();
		} finally {
			// 优雅退出释放NIO线程组
			group.shutdownGracefully();
		}

	}

	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		String host = "127.0.0.1";
		int sendNumber = 8;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new EchoClient(host,port,sendNumber).connect();
	}
}
