package net.realmproject.rosbridge.client.datatypes.geometry;

import net.realmproject.rosbridge.client.datatypes.RosType;

@RosType("geometry_msgs/Quaternion")
public class Quaternion {
	public double x, y, z, w;
}
