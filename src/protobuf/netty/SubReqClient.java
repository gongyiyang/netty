package protobuf.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import protobuf.SubscribeRespProto;

public class SubReqClient {
	public void connet(int port,String host) throws InterruptedException{
		//����NIO�߳���
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel arg0) throws Exception {
					arg0.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					arg0.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
					arg0.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					arg0.pipeline().addLast(new ProtobufEncoder());
					arg0.pipeline().addLast(new SubReqClientHandler());
				}
			});
			
			//�����첽���Ӳ���
			ChannelFuture f = b.connect(host, port).sync();
			//�ȴ��ͻ�����·�ر�
			f.channel().closeFuture().sync();
		} finally {
			//�����˳����ͷ��߳���
			group.shutdownGracefully();
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
		new SubReqClient().connet(port, "127.0.0.1");
	}
	 
}
