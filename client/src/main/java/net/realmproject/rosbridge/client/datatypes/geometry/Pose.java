package net.realmproject.rosbridge.client.datatypes.geometry;

import net.realmproject.rosbridge.client.datatypes.RosType;

@RosType("geometry_msgs/Pose")
public class Pose {
	public Point position;
	public Quaternion orientation;
	
	public Pose init() {
		position = new Point();
		orientation = new Quaternion();
		return this;
	}
	
}
