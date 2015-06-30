package net.realmproject.rosbridge.client.datatypes.sensor;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.standard.Header;


@RosType("sensor_msgs/CompressedImage")
public class CompressedImage implements Serializable {

    public Header header;
    public String format;
    // public byte[] data;
    public String data;

}
