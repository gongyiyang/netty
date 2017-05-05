package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeClient {

	public void connect(int port,String host) throws Exception{
		//���ÿͻ���NIO�߳���
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					// TODO Auto-generated method stub
				arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));	
				arg0.pipeline().addLast(new StringDecoder());	
				arg0.pipeline().addLast(new TimeClientHandler());	
				}
			});
			//�����첽���Ӳ���
			ChannelFuture f = b.connect(host,port).sync();
			//�ȴ��ͻ�����·�ر�
			f.channel().closeFuture().sync();
		} finally {
			// TODO: handle finally clause
			//�����˳����ͷ�NIO�߳���
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
		if (args != null && args.length > 0) {
			try {
				
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
			
		}
		new TimeClient().connect(port, "127.0.0.1");
	}
}
