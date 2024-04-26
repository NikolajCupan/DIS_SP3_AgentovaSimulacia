package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="91"
public class AgentPrevzatieTovar extends Agent
{
	// Vlastne
	private void customAgentPrevzatieTovar()
	{
		this.addOwnMessage(Mc.holdPrevzatieTovar);
	}
	// Vlastne koniec


	public AgentPrevzatieTovar(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentPrevzatieTovar();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerPrevzatieTovar(Id.managerPrevzatieTovar, mySim(), this);
		new ProcessPrevzatieTovar(Id.processPrevzatieTovar, mySim(), this);
		addOwnMessage(Mc.requestResponsePrevzatieTovar);
	}
	//meta! tag="end"
}
