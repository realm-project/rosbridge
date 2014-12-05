package net.realmproject.rosbridge.actionlibclient.impl.datatypes;

import net.realmproject.rosbridge.actionlibclient.datatypes.GoalID;
import net.realmproject.rosbridge.client.datatypes.standard.Header;


public class InternalGoalMessage {
	
	//public Header header;
	public GoalID goal_id;
	public Object goal;
	
	public InternalGoalMessage(Object data, GoalID id) {
		//this(data, id, new Header().init());
		this.goal = data;
		this.goal_id = id;
	}
	
	/*
	public InternalGoalMessage(Object data, GoalID id, Header header) {
		this.goal = data;
		this.goal_id = id;
		//this.header = header;
	}
	*/
}