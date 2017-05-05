package netty.service;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerhandler extends ChannelHandlerAdapter {
	
		private int counter;
	
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("服务器读取消息");
		String body = (String)msg;
		
		System.out.println("The time server receive order : "+ body+"The cunter is :"+ ++counter);
		
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(
				System.currentTimeMillis()).toString() : "BAD ORDER";
				
				currentTime = currentTime + System.getProperty("line.separator");
				
				ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
				ctx.writeAndFlush(resp);
		}
		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		}
}
