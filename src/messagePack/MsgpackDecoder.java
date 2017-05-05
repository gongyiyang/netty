package messagePack;

import java.util.List;

import org.msgpack.MessagePack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * messagepack������
 * @author <font color="red"><b>Gong.YiYang</b></font>
 * @Date 2017��5��5��
 * @Version 
 * @Description
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext arg0, ByteBuf arg1, List<Object> arg2) throws Exception {

		final byte[] array;
		final int length = arg1.readableBytes();
		array = new byte[length];
		arg1.getBytes(arg1.readerIndex(),array,0,length);
		MessagePack messagePack = new MessagePack();
		arg2.add(messagePack.read(array));
	}

}
