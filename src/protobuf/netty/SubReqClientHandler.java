package protobuf.netty;

import java.util.ArrayList;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import protobuf.SubscribeReqProto;

public class SubReqClientHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(subReq(i));
		}
	}
	
	private SubscribeReqProto.SubscribeReq subReq(int i){
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(i);
		builder.setUserName("Lilinfeng");
		builder.setPreductName("Netty Book For Protobuf");
		ArrayList<Object> address = new ArrayList<>();
		address.add("NanJing YuHuaTai");
		address.add("BeiJing LiuLiChang");
		address.add("ShenZhen HongShuLin");
		
		builder.setAddress("NanJing YuHuaTai");
		//builder.setAddress("BeiJing LiuLiChang");
		//builder.setAddress("ShenZhen HongShuLin");
		//builder.addAllAddress(address);
		return builder.build();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive server response : ["+msg+" ]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
