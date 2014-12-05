package net.realmproject.rosbridge.actionlibclient.impl.datatypes;

import java.util.List;

import net.realmproject.rosbridge.actionlibclient.datatypes.GoalStatus;
import net.realmproject.rosbridge.client.datatypes.standard.Header;


public class InternalStatusMessage {

	public Header header;
	public List<GoalStatus> status_list;
	
}