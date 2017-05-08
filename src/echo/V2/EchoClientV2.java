package echo.V2;


import echo.client.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import messagePack.MsgpackDecoder;
import messagePack.MsgpackEncoder;

public class EchoClientV2 {
	
	private String host;
	private int port;
	private int sendNumber;
	public EchoClientV2(String host, int port, int sendNumber) {
		this.host = host;
		this.port = port;
		this.sendNumber = sendNumber;
	}
	
	public void run() throws InterruptedException{
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 2,0,2));
					arg0.pipeline().addLast("msgPack Decoder",new MsgpackDecoder());
					arg0.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
					arg0.pipeline().addLast("msgPack Encoder",new MsgpackEncoder());
					arg0.pipeline().addLast(new EchoClientHandler(sendNumber));
				}
			});
			
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		String host = "127.0.0.1";
		int sendNumber = 100;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		new EchoClientV2(host,port,sendNumber).run();
	}
}
