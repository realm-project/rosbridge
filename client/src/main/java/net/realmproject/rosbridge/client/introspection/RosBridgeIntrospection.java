package net.realmproject.rosbridge.client.introspection;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;



public interface RosBridgeIntrospection
{

	public List<String> getServices() throws Exception;


	public List<String> getTopics() throws Exception;


	public List<String> getNodes() throws Exception;


	public String getServiceNode(String service) throws Exception;


	public String getServiceType(String service) throws Exception;


	public String getServiceHost(String service) throws Exception;


	public List<String> getSubscribers(String topic) throws Exception;


	public List<String> getTopicsForType(String type) throws Exception, IOException;

}