package net.realmproject.rosbridge.client.datatypes.geometry;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("geometry_msgs/Twist")
public class Twist implements Serializable {

    public Vector3 linear, angular;

    public Twist init() {
        linear = new Vector3();
        angular = new Vector3();
        return this;
    }

}
