package echo.client;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import messagePack.UserInfo;

public class EchoClientHandler extends ChannelHandlerAdapter {

	private int counter;
	
	private final int sendNumber;
	
	
	public EchoClientHandler(int sendNumber) {
		this.sendNumber = sendNumber;
	}

	static final String ECHO_REQ = "Hi, Laowang. welcome to netty.$_";
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		/*for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}*/
		UserInfo[] userInfos = UserInfo();
		for (UserInfo userInfo : userInfos) {
			ctx.writeAndFlush(userInfo);
		}
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
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Client receive the msgpack message :" + msg);
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
