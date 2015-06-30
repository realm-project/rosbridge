package net.realmproject.rosbridge.client.datatypes.geometry;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("geometry_msgs/Quaternion")
public class Quaternion implements Serializable {

    public double x, y, z, w;
}
