package org.example.continualAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import OSPABA.Process;

//meta! id="96"
public class ProcessPrevzatieTovar extends Process
{
	public ProcessPrevzatieTovar(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPrevzatieTovar", id="97", type="Start"
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
	public AgentPrevzatieTovar myAgent()
	{
		return (AgentPrevzatieTovar)super.myAgent();
	}

}
