package net.realmproject.rosbridge.client.datatypes.sensor;

import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.standard.Header;

@RosType("sensor_msgs/CompressedImage")
public class CompressedImage {
	
	public Header header;
	public String format;
	//public byte[] data;
	public String data;
	
}
