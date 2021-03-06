package marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import messagePack.UserInfo;
import protobuf.SubscribeReqProto;

public class marClientHandler extends ChannelHandlerAdapter {
		
	  @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        cause.printStackTrace();
	        ctx.close();
	    }

	    @Override
	    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	    	SubScriptReq req = new SubScriptReq();
	        for (int i = 0; i < 100; i++) {

	            req.setSubReq(i);
	            req.setProductName("productName");
	            req.setUserName("userName");
	            req.setAddress("address");
	            ctx.writeAndFlush(req);
	        }
	    }

	    @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg)
	            throws Exception {
	        System.out.println(msg);
	    }

	    @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	        ctx.flush();
	    }
}
