package net.realmproject.rosbridge.client.client.call;

import java.util.function.Consumer;



public class IRosBridgeCallbackCall<T> extends AbstractRosBridgeCall<T>
{
	
	private Consumer<T> handler;
	
	public IRosBridgeCallbackCall(String service, String id, Class<T> clazz, Consumer<T> handler, Consumer<RosBridgeCall<T>> closeCommand) {
		super(service, id, clazz, closeCommand);
		this.handler = handler;
	}

	@Override
	protected synchronized void setResult(T t)
	{
		close();
		handler.accept(t);
	}

}
