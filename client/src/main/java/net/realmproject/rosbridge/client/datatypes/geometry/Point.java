package net.realmproject.rosbridge.client.datatypes.geometry;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("geometry_msgs/Point")
public class Point implements Serializable {

    public double x, y, z;
}
