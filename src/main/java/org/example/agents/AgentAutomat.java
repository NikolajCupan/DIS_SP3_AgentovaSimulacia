package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="39"
public class AgentAutomat extends Agent
{
	private void customAgentAutomat()
	{
		this.addOwnMessage(Mc.holdVyprazdnenieFrontAutomat);
	}

	public AgentAutomat(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentAutomat();
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
		new ManagerAutomat(Id.managerAutomat, mySim(), this);
		new ProcessObsluhaAutomat(Id.processObsluhaAutomat, mySim(), this);
		new MonitorVyprazdnenieFrontAutomat(Id.monitorVyprazdnenieFrontAutomat, mySim(), this);
		new QueryVyprazdnenieFrontAutomat(Id.queryVyprazdnenieFrontAutomat, mySim(), this);
		addOwnMessage(Mc.noticeInicializaciaAutomat);
		addOwnMessage(Mc.requestResponseObsluhaAutomat);
		addOwnMessage(Mc.noticeZapnutieAutomat);
		addOwnMessage(Mc.noticeVnutornaVyprsanieSimulacnyCas);
		addOwnMessage(Mc.requestResponseVypnutieAutomat);
	}
	//meta! tag="end"
}
