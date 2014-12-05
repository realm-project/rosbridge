package net.realmproject.rosbridge.client.introspection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.realmproject.rosbridge.client.client.RosBridgeClient;

public class IRosBridgeIntrospection implements RosBridgeIntrospection
{

	private RosBridgeClient client;
	private long timeout = 60;
		
	public IRosBridgeIntrospection(RosBridgeClient client) {
		this.client = client;
	}
	
	private List<String> asStrings(List<Object> objects) {
		ArrayList<String> strings = new ArrayList<>();
		for (Object object : objects) { strings.add((String) object); }
		return strings;
	}

	private List<String> listStringService(String serviceName, Object... args) throws Exception {
		return (List<String>)client.call("/rosapi/" + serviceName, Arrays.asList(args)).get(timeout, TimeUnit.SECONDS).get(serviceName);
	}
	
	private String stringService(String serviceName, Object... args) throws Exception {
		Map<String, Object> results = client.call("/rosapi/" + serviceName, Arrays.asList(args)).get(timeout, TimeUnit.SECONDS);
		List<Entry<String, Object>> resultList = new ArrayList<>(results.entrySet());
		return (String)resultList.get(0).getValue();
	}
	

	@Override
	public List<String> getServices() throws Exception {
		return listStringService("services");
	}

	@Override
	public List<String> getTopics() throws Exception {
		return listStringService("topics");
	}

	@Override
	public List<String> getNodes() throws Exception {
		return listStringService("nodes");
	}
	
	@Override
	public String getServiceNode(String service) throws Exception {
		return stringService("service_node", service);
	}
	
	@Override
	public String getServiceType(String service) throws Exception {
		return stringService("service_type", service);
	}

	@Override
	public String getServiceHost(String service) throws Exception {
		return stringService("service_host", service);
	}
	
	@Override
	public List<String> getSubscribers(String topic) throws Exception {
		return listStringService("subscribers", topic);
	}
	
	@Override
	public List<String> getTopicsForType(String type) throws Exception {
		return listStringService("topics_for_type", type);
	}

}
