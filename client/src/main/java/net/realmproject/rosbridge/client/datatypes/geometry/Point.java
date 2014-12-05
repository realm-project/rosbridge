package net.realmproject.rosbridge.client.datatypes.geometry;

import net.realmproject.rosbridge.client.datatypes.RosType;

@RosType("geometry_msgs/Point")
public class Point {
	public double x, y, z;
}
