package net.realmproject.rosbridge.actionlibclient.impl;

public enum ClientGoalState {

	//WAITING FOR GOAL ACK - Client Initiated
	SENDING,
	
	//PENDING
	PENDING,
	
	//ACTIVE
	ACTIVE,
	
	//WAITING FOR CANCEL ACK - Client Initiated
	CANCELLING,
	
	//RECALLING
	RECALLING,
	
	//PREEMPTING
	PREEMPTING,
	
	//WAITING FOR RESULT
	REJECTED,
	RECALLED,
	PREEMPTED,
	ABORTED,
	SUCCEEDED,
	
}
