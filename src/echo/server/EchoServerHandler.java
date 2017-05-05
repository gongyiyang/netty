package echo.server;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import messagePack.UserInfo;

public class EchoServerHandler extends ChannelHandlerAdapter {
	
	int counter = 0;
	private final int sendNumber;
	
	public EchoServerHandler(int sendNumber) {
		this.sendNumber = sendNumber;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		UserInfo[] userInfos = UserInfo();
		for (UserInfo userInfo : userInfos) {
			ctx.writeAndFlush(userInfo);
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	/*String body =	(String)msg;
	System.out.println("This is"+ ++counter +"times receive client : ["+body+"]");
	body+="$_";
	ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
	ctx.writeAndFlush(echo);*/
		@SuppressWarnings("unchecked")
        List<UserInfo> userInfos =	(List<UserInfo>)msg;
		System.out.println("Client receive the msgpack message :" + userInfos);
		
		
	}
	private UserInfo [] UserInfo(){
	    UserInfo []	userInfos=new UserInfo[sendNumber];
	    UserInfo userInfo = null;
	    for (int i = 0; i < sendNumber; i++) {
			 userInfo = new UserInfo();
			 userInfo.setUserID(i);
			 userInfo.setUserName("ABCDEFG --->"+ i);
			 userInfos[i] = userInfo;
		}
	    return userInfos;
		}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();//发生异常，关闭链路
	}
}
