package net.realmproject.rosbridge.client.client.call;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;


public class IRosBridgeFutureCall<T> extends  AbstractRosBridgeCall<T> implements Callable<T>
{
	
	private T result = null;
	
	public IRosBridgeFutureCall(String service, String id, Class<T> clazz, Consumer<RosBridgeCall<T>> closeCommand) {
		super(service, id, clazz, closeCommand);
		
	}

	
	protected synchronized void setResult(T t)
	{
		this.result = t;
		close();
		this.notifyAll();
	}


	@Override
	public synchronized T call() throws Exception {
				
		//if result already has a value, return it
		if (result != null) return result;
				
		//if result is still null, wait for up to 60 seconds for someone to call notify
		this.wait(60000); //60 second timeout
				
		//if result has a value now, return it
		if (result != null) return result;
		
		//otherwise raise a timeout exception
		throw new TimeoutException();
		
	}



}
