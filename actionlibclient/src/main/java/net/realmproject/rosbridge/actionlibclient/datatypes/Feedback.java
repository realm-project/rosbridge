package net.realmproject.rosbridge.actionlibclient.datatypes;

import net.realmproject.rosbridge.client.datatypes.standard.Header;


public class Feedback<T> {

	public GoalStatus status;
	public Header header;
	public T feedback;
}
