package net.realmproject.rosbridge.actionlibclient.tests;

import java.io.IOException;

import javax.websocket.DeploymentException;

import junit.framework.Assert;
import net.realmproject.rosbridge.actionlibclient.Action;
import net.realmproject.rosbridge.actionlibclient.ActionClient;
import net.realmproject.rosbridge.actionlibclient.Goal;
import net.realmproject.rosbridge.client.client.IRosBridgeClient;
import net.realmproject.rosbridge.client.client.RosBridgeClient;

public class ActionlibTest {

	/*
	public void submit() throws InterruptedException, IOException, Exception {
		
		StringBuilderRosBridgeConnection conn = new StringBuilderRosBridgeConnection();
		RosBridgeClient client = new IRosBridgeClient(conn);
		ActionClient actionclient = new ActionClient(client);
		Action<String> action = actionclient.create("basetopic", "type_string", String.class);
		Goal<String> goal = action.createGoal();
		
		
		goal.submit("Hello");
		
		
		String expected = "{\"topic\":\"basetopic/feedback\",\"id\":\"0\",\"op\":\"subscribe\",\"type\":\"type_string\"}\n{\"topic\":\"basetopic/status\",\"id\":\"1\",\"op\":\"subscribe\"}\n{\"topic\":\"basetopic/result\",\"id\":\"2\",\"op\":\"subscribe\",\"type\":\"type_string\"}\n{\"topic\":\"basetopic/goal\",\"id\":\"3\",\"op\":\"advertise\",\"type\":\"type_string\"}\n{\"topic\":\"basetopic/goal\",\"id\":\"4\",\"op\":\"publish\",\"msg\":{\"goal\":\"Hello\",\"goal_id\":{\"id\":\"GoalID_0_unique_7f7374c6-e87c-46aa-a398-fb2a5027e5d6_\",\"stamp\":{\"nsec\":0,\"sec\":0}},\"header\":{\"frame_id\":\"\",\"seq\":1,\"stamp\":{\"nsec\":0,\"sec\":0}}}}\n{\"topic\":\"basetopic/goal\",\"id\":\"5\",\"op\":\"unadvertise\"}"; 
		String actual = conn.toString().trim();
		//remove unique id
		expected = expected.substring(0, 381) + expected.substring(425);
		actual = actual.substring(0, 381) + actual.substring(425);
		
		System.out.println("Asserting expected");
		System.out.println("==================");
		System.out.println(expected);
		System.out.println("");
		System.out.println("Asserting actual");
		System.out.println("==================");
		System.out.println(actual);
		
		Assert.assertEquals(expected, actual);
		
	}
	*/
}
