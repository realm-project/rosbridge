package net.realmproject.rosbridge.client.datatypes.standard;


import java.io.Serializable;
import java.util.Date;

import net.realmproject.rosbridge.client.datatypes.RosType;


@RosType("std_msgs/Timestamp")
public class Timestamp implements Serializable {

    public long nsecs, secs;

    public static Timestamp now() {
        return fromDate(new Date());
    }

    public static Timestamp fromDate(Date d) {
        Timestamp ts = new Timestamp();
        long ms = d.getTime();

        ts.secs = ms / 1000;
        ts.nsecs = (ms % 1000) * 1000000;

        return ts;
    }

    public long getNsecs() {
        return nsecs;
    }

    public void setNsecs(long nsec) {
        this.nsecs = nsec;
    }

    public long getSecs() {
        return secs;
    }

    public void setSecs(long sec) {
        this.secs = sec;
    }

}
