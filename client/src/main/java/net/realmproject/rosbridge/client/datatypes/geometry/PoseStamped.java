package net.realmproject.rosbridge.client.datatypes.geometry;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.standard.Header;


@RosType("geometry_msgs/PoseStamped")
public class PoseStamped implements Serializable {

    public Header header;
    public Pose pose;

    public PoseStamped init() {
        header = new Header().init();
        pose = new Pose().init();
        return this;
    }

}
