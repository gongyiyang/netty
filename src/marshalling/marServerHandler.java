package marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import messagePack.UserInfo;
import protobuf.SubscribeRespProto;

public class marServerHandler extends ChannelHandlerAdapter {

	  @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        cause.printStackTrace();
	        ctx.close();
	        super.exceptionCaught(ctx, cause);
	    }

	    @Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg)
	            throws Exception {

	        System.out.println(msg);
	        SubScriptReq req = (SubScriptReq)msg;
	        SubscriptResp sub = new SubscriptResp();
	        sub.setDesc("desc");
	        sub.setSubScriptID(req.getSubReq());
	        sub.setRespCode("0");
	        ctx.writeAndFlush(sub);
	    }

	    @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	        ctx.flush();
	    }
}
