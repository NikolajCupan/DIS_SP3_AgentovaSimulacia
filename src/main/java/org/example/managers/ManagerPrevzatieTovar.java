package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="91"
public class ManagerPrevzatieTovar extends Manager
{
	public ManagerPrevzatieTovar(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentSystem", id="94", type="Request"
	public void processRequestResponsePrevzatieTovar(MessageForm message)
	{
	}

	//meta! sender="ProcessPrevzatieTovar", id="97", type="Finish"
	public void processFinish(MessageForm message)
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
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.finish:
			processFinish(message);
		break;

		case Mc.requestResponsePrevzatieTovar:
			processRequestResponsePrevzatieTovar(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPrevzatieTovar myAgent()
	{
		return (AgentPrevzatieTovar)super.myAgent();
	}

}
