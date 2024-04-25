package org.example.continualAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="101"
public class MonitorZaciatokPrestavkaPokladne extends Monitor
{
	public MonitorZaciatokPrestavkaPokladne(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPokladne", id="102", type="Start"
	public void processStart(MessageForm message)
	{
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

}
