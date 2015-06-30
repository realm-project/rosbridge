package net.realmproject.rosbridge.client.datatypes.geometry;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("geometry_msgs/Vector3")
public class Vector3 implements Serializable {

    public double x, y, z;

}
