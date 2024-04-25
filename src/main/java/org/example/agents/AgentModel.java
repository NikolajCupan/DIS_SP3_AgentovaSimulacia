package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="1"
public class AgentModel extends Agent
{
	public void inicializaciaSimulacie()
	{
		// Okolie
		MyMessage inicializaciaOkolie = new MyMessage(this.mySim());
		inicializaciaOkolie.setCode(Mc.noticeInicializaciaOkolie);
		inicializaciaOkolie.setAddressee(Id.agentOkolie);
		this.manager().notice(inicializaciaOkolie);

		// System
		MyMessage inicializaciaSystem = new MyMessage(this.mySim());
		inicializaciaSystem.setCode(Mc.noticeInicializaciaSystem);
		inicializaciaSystem.setAddressee(Id.agentSystem);
		this.manager().notice(inicializaciaSystem);
	}

	public AgentModel(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();
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
		new ManagerModel(Id.managerModel, mySim(), this);
		addOwnMessage(Mc.requestResponsePrichodZakaznik);
		addOwnMessage(Mc.requestResponseSpracovanieZakaznik);
	}
	//meta! tag="end"
}
