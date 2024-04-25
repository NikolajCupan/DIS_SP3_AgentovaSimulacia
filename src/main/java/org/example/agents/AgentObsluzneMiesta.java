package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="51"
public class AgentObsluzneMiesta extends Agent
{
	public AgentObsluzneMiesta(int id, Simulation mySim, Agent parent)
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
		new ManagerObsluzneMiesta(Id.managerObsluzneMiesta, mySim(), this);
		new QueryZaciatokPrestavkaObsluzneMiesta(Id.queryZaciatokPrestavkaObsluzneMiesta, mySim(), this);
		new MonitorZaciatokPrestavkaObsluzneMiesta(Id.monitorZaciatokPrestavkaObsluzneMiesta, mySim(), this);
		new ProcessObsluhaObsluzneMiestoOnlineZakaznik(Id.processObsluhaObsluzneMiestoOnlineZakaznik, mySim(), this);
		new MonitorKoniecPrestavkaObsluzneMiesta(Id.monitorKoniecPrestavkaObsluzneMiesta, mySim(), this);
		new QueryKoniecPrestavkaObsluzneMiesta(Id.queryKoniecPrestavkaObsluzneMiesta, mySim(), this);
		new ProcessObsluhaObsluzneMiestoObycajnyZakaznik(Id.processObsluhaObsluzneMiestoObycajnyZakaznik, mySim(), this);
		addOwnMessage(Mc.requestResponseOdovzdanieZamestnanec);
		addOwnMessage(Mc.noticeVnutornaZaciatokPrestavkaObsluzneMiesta);
		addOwnMessage(Mc.requestResponseObsluhaObsluzneMiesto);
		addOwnMessage(Mc.noticeUvolnenieObsluzneMiesto);
		addOwnMessage(Mc.requestResponseNaplnenieFront);
		addOwnMessage(Mc.noticeVnutornaKoniecPrestavkaObsluzneMiesta);
		addOwnMessage(Mc.noticeInicializaciaObsluzneMiesta);
	}
	//meta! tag="end"
}
