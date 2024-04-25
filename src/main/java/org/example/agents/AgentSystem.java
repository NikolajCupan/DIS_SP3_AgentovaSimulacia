package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="35"
public class AgentSystem extends Agent
{
	public AgentSystem(int id, Simulation mySim, Agent parent)
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
		new ManagerSystem(Id.managerSystem, mySim(), this);
		addOwnMessage(Mc.requestResponseObsluhaPokladna);
		addOwnMessage(Mc.requestResponseObsluhaAutomat);
		addOwnMessage(Mc.requestResponsePrevzatieTovar);
		addOwnMessage(Mc.requestResponseOdovzdanieZamestnanec);
		addOwnMessage(Mc.requestResponseObsluhaObsluzneMiesto);
		addOwnMessage(Mc.requestResponsePrijatieZamestnanec);
		addOwnMessage(Mc.noticeInicializaciaSystem);
		addOwnMessage(Mc.requestResponseNaplnenieFront);
		addOwnMessage(Mc.requestResponseSpracovanieZakaznik);
		addOwnMessage(Mc.requestResponseVypnutieAutomat);
		addOwnMessage(Mc.noticeUvolnenieFront);
	}
	//meta! tag="end"
}
