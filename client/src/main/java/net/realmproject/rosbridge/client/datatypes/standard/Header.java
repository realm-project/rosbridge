package net.realmproject.rosbridge.client.datatypes.standard;


import java.io.Serializable;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("std_msgs/Header")
public class Header implements Serializable {
    // private static int seq_counter = 1;

    public Integer seq;
    public Timestamp stamp;
    public String frame_id;

    public Header() {
        // seq = seq_counter++;
    }

    public Header init() {
        seq = 0;
        stamp = new Timestamp();
        frame_id = "";
        return this;
    }

}
