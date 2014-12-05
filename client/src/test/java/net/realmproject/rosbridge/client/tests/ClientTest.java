package net.realmproject.rosbridge.client.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.realmproject.rosbridge.client.client.IRosBridgeClient;
import net.realmproject.rosbridge.client.client.RosBridgeClient;

public class ClientTest {


	
	/*
	public void call() throws InterruptedException, IOException{
		StringBuilderRosBridgeConnection conn = new StringBuilderRosBridgeConnection();
		RosBridgeClient client = new IRosBridgeClient(conn);
		
		List<String> args = new ArrayList<>();
		args.add("b");
		args.add("c");
		client.call("a", args);
		
		String expected = "{\"id\":\"0\",\"op\":\"call_service\",\"args\":[\"b\",\"c\"],\"service\":\"a\"}";
		Assert.assertEquals(expected, conn.toString().trim());
	}
	
	
	
	public void publish() throws InterruptedException, IOException {
		StringBuilderRosBridgeConnection conn = new StringBuilderRosBridgeConnection();
		RosBridgeClient client = new IRosBridgeClient(conn);
		
		client.publishOnce("topic", "type", "Hello, World");
		
		String expected = "{\"topic\":\"topic\",\"id\":\"0\",\"op\":\"advertise\",\"type\":\"type\"}\n{\"topic\":\"topic\",\"id\":\"1\",\"op\":\"publish\",\"msg\":\"Hello, World\"}\n{\"topic\":\"topic\",\"id\":\"2\",\"op\":\"unadvertise\"}";
		Assert.assertEquals(expected, conn.toString().trim());
		
	}
	
	
	
	public void advertise() throws InterruptedException, IOException {
		StringBuilderRosBridgeConnection conn = new StringBuilderRosBridgeConnection();
		RosBridgeClient client = new IRosBridgeClient(conn);

		client.advertise("topic", "type");
		
		String expected = "{\"topic\":\"topic\",\"id\":\"0\",\"op\":\"advertise\",\"type\":\"type\"}";
		Assert.assertEquals(expected, conn.toString().trim());
	}
	
	
	
	public void subscribe() throws IOException, InterruptedException {
		StringBuilderRosBridgeConnection conn = new StringBuilderRosBridgeConnection();
		RosBridgeClient client = new IRosBridgeClient(conn);

		client.subscribe("topic", "type_string", String.class).close();
		
		String expected = "{\"topic\":\"topic\",\"id\":\"0\",\"op\":\"subscribe\",\"type\":\"type_string\"}\n{\"topic\":\"topic\",\"id\":\"0\",\"op\":\"unsubscribe\"}";
		Assert.assertEquals(expected, conn.toString().trim());
	}
	*/
}
