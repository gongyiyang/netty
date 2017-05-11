package marshalling;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeCFactory {
	
	/**
	 * ����jboss marshalling������MarshallingDecoder
	 */
	
	public static MarshallingDecoder buildMarshallingDecoder(){
		  /*
         * ͨ�� Marshalling ������� getProvidedMarshallerFactory
         * ��̬������ȡMarshallerFactory ʵ��, , ���� serial ��ʾ�������� Java ���л���������.������
         * jboss-marshalling-serial ���ṩ
         */
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		DefaultUnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);
		return decoder;
	}
	
	/**
	 * ����jboss marshalling������MarshallingEncoder
	 */
	public static MarshallingEncoder buildMarshallingEncoder(){
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		DefaultMarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
		MarshallingEncoder encoder = new MarshallingEncoder(provider);
		return encoder;
	}
}
