package net.realmproject.rosbridge.client.datatypes.geometry;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("geometry_msgs/Pose")
public class Pose implements Serializable {

    public Point position;
    public Quaternion orientation;

    public Pose init() {
        position = new Point();
        orientation = new Quaternion();
        return this;
    }

}
