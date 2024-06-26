package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="1"
public class ManagerModel extends Manager
{
	public ManagerModel(int id, Simulation mySim, Agent myAgent)
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

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! sender="AgentOkolie", id="31", type="Request"
	public void processRequestResponsePrichodZakaznik(MessageForm message)
	{
		message.setCode(Mc.requestResponseSpracovanieZakaznik);
		message.setAddressee(Id.agentSystem);
		this.request(message);
	}

	//meta! sender="AgentSystem", id="38", type="Response"
	public void processRequestResponseSpracovanieZakaznik(MessageForm message)
	{
		message.setCode(Mc.requestResponsePrichodZakaznik);
		this.response(message);
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
		case Mc.requestResponseSpracovanieZakaznik:
			processRequestResponseSpracovanieZakaznik(message);
		break;

		case Mc.requestResponsePrichodZakaznik:
			processRequestResponsePrichodZakaznik(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentModel myAgent()
	{
		return (AgentModel)super.myAgent();
	}

}
