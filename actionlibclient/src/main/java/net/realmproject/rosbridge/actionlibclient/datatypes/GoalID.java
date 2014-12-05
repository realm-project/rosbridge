package net.realmproject.rosbridge.actionlibclient.datatypes;

import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.standard.Timestamp;

@RosType("actionlib_msgs/GoalID")
public class GoalID {

	public String id = "";
	public Timestamp stamp = new Timestamp();

	public GoalID() {}
	
	public GoalID(String id) {
		this.id = id;
		stamp = new Timestamp();
	}
	
	public GoalID(String id, Timestamp stamp) {
		this.id = id;
		this.stamp = stamp;
	}
	
	public GoalID(GoalID other) {
		this.id = other.id;
		this.stamp = other.stamp;
	}
	
	
	
}
