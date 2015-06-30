package net.realmproject.rosbridge.client.datatypes.sensor;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.standard.Header;


@RosType("sensor_msgs/JointState")
public class JointState implements Serializable {

    public double[] position;
    public String[] name;
    public double[] velocity;
    public double[] effort;
    public Header header;

    public JointState init() {
        position = new double[6];
        name = new String[6];
        velocity = new double[6];
        effort = new double[6];
        header = new Header().init();
        return this;
    }
}
