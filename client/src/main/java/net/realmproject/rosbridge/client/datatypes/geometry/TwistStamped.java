package net.realmproject.rosbridge.client.datatypes.geometry;

import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.standard.Header;

@RosType("geometry_msgs/TwistStamped")
public class TwistStamped {

	public Header header;
	public Twist twist;
	
	public TwistStamped init() {
		header = new Header().init();
		twist = new Twist().init();
		return this;
	}
	
}
