package net.realmproject.rosbridge.client.connection;

import java.util.Map;

import com.google.common.base.Optional;


/**
 * A struct-like class for containing a RosJson JSON message
 * @author nathaniel
 *
 */

public interface RosBridgeMessage
{

	String getOpcode();
	void setOpcode(String opcode);

	Map<String, Object> getArguments();
	void setArguments(Map<String, Object> arguments);

	Optional<String> getId();
	void setId(String id);
	
	boolean hasId();
	boolean matchesId(String id);
	
}