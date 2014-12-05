package net.realmproject.rosbridge.actionlibclient;

import java.io.IOException;
import java.util.Date;

public interface Action<T> {

	/**
	 * Create a new {@link Goal} for this Action
	 * @return
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	Goal<T> createGoal() throws InterruptedException, IOException;
	
	
	/**
	 * Cancels all goals for this action
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	void cancelAll() throws InterruptedException, IOException;
	
	
	/**
	 * Cancels all goals for this action which were submitted before a given time, as time-stamped by the ActionServer.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	void cancelBefore(Date before) throws InterruptedException, IOException;
	

}