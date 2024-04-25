package org.example.agents;

import OSPABA.*;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="74"
public class AgentPokladne extends Agent
{
	// Vlastne
	private void customAgentPokladne()
	{
		this.addOwnMessage(Mc.holdZaciatokPrestavkaPokladne);
		this.addOwnMessage(Mc.holdKoniecPrestavkaPokladne);
	}

	public void zacniPrestavku()
	{
		// TODO: zaciatok presavky pokladne
	}

	public void ukonciPrestavku()
	{
		// TODO: koniec prestavky pokladne
	}
	// Vlastne koniec


	public AgentPokladne(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentPokladne();
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
		new ManagerPokladne(Id.managerPokladne, mySim(), this);
		new MonitorZaciatokPrestavkaPokladne(Id.monitorZaciatokPrestavkaPokladne, mySim(), this);
		new ProcessObsluhaPokladna(Id.processObsluhaPokladna, mySim(), this);
		new QueryKoniecPrestavkaPokladne(Id.queryKoniecPrestavkaPokladne, mySim(), this);
		new MonitorKoniecPrestavkaPokladne(Id.monitorKoniecPrestavkaPokladne, mySim(), this);
		new QueryZaciatokPrestavkaPokladne(Id.queryZaciatokPrestavkaPokladne, mySim(), this);
		addOwnMessage(Mc.noticeVnutornaKoniecPrestavkaPokladne);
		addOwnMessage(Mc.noticeVnutornaZaciatokPrestavkaPokladne);
		addOwnMessage(Mc.requestResponseObsluhaPokladna);
		addOwnMessage(Mc.requestResponsePrijatieZamestnanec);
		addOwnMessage(Mc.noticeInicializaciaPokladne);
	}
	//meta! tag="end"
}
