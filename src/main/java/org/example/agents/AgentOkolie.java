package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="4"
public class AgentOkolie extends Agent
{
	public AgentOkolie(int id, Simulation mySim, Agent parent)
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
		new ManagerOkolie(Id.managerOkolie, mySim(), this);
		new SchedulerPrichodOnlineZakaznik(Id.schedulerPrichodOnlineZakaznik, mySim(), this);
		new SchedulerPrichodZmluvnyZakaznik(Id.schedulerPrichodZmluvnyZakaznik, mySim(), this);
		new SchedulerPrichodBeznyZakaznik(Id.schedulerPrichodBeznyZakaznik, mySim(), this);
		addOwnMessage(Mc.noticeVnutornaPrichodOnlineZakaznik);
		addOwnMessage(Mc.noticeVnutornaPrichodBeznyZakaznik);
		addOwnMessage(Mc.requestResponsePrichodZakaznik);
		addOwnMessage(Mc.noticeVnutornaPrichodZmluvnyZakaznik);
		addOwnMessage(Mc.noticeInicializaciaOkolie);
	}
	//meta! tag="end"
}
