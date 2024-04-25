package org.example.simulation;

import OSPABA.*;
import org.example.Vlastne.Ostatne.Identifikator;
import org.example.agents.*;

public class MySimulation extends Simulation
{
	// Vlastne
	private double trvanieSimulacie;
	private boolean zvysenyTokZakaznikov;
	private boolean prestavka;

	private int pocetObycajnychObsluznychMiest;
	private int pocetOnlineObsluznychMiest;
	private int pocetPokladni;

	private void customInit(double trvanieSimulacie, boolean zvysenyTokZakaznikov, boolean prestavka,
		int pocetObsluznychMiest, int pocetPokladni)
	{
		if (trvanieSimulacie <= 0)
		{
			throw new RuntimeException("Trvanie simulacie musi byt vacsie ako 0!");
		}
		if (pocetObsluznychMiest < 3)
		{
			throw new RuntimeException("Pocet obsluznych miest musi byt aspon 3!");
		}
		if (pocetPokladni < 1)
		{
			throw new RuntimeException("Pocet pokladni miest musi byt aspon 1!");
		}

		this.trvanieSimulacie = trvanieSimulacie;
		this.zvysenyTokZakaznikov = zvysenyTokZakaznikov;
		this.prestavka = prestavka;

		this.pocetOnlineObsluznychMiest = (int)Math.floor(pocetObsluznychMiest / 3.0);
		this.pocetObycajnychObsluznychMiest = pocetObsluznychMiest - this.pocetOnlineObsluznychMiest;
		this.pocetPokladni = pocetPokladni;
	}

	private void customPrepareReplication()
	{
		// Spustenie simulacie
		Identifikator.resetujID();
		this.agentModel().inicializaciaSimulacie();
	}

	private void customReplicationFinished()
	{
		double cas = this.currentTime();
		int i = this.agentOkolie().pocetZakaznikovSystem();
		int ii = 100;
	}

	public double getTrvanieSimulacie()
	{
		return this.trvanieSimulacie;
	}

	public boolean getZvysenyTokZakaznikov()
	{
		return this.zvysenyTokZakaznikov;
	}

	public boolean getPrestavka()
	{
		return this.prestavka;
	}

	public int getPocetObycajnychObsluznychMiest()
	{
		return this.pocetObycajnychObsluznychMiest;
	}

	public int getPocetOnlineObsluznychMiest()
	{
		return this.pocetOnlineObsluznychMiest;
	}

	public int getPocetPokladni()
	{
		return this.pocetPokladni;
	}
	// Vlastne koniec


	public MySimulation(double trvanieSimulacie, boolean zvysenyTokZakaznikov, boolean prestavka,
		int pocetObsluznychMiest, int pocetPokladni)
	{
		init();

		// Vlastne
		this.customInit(trvanieSimulacie, zvysenyTokZakaznikov, prestavka, pocetObsluznychMiest, pocetPokladni);
	}

	public MySimulation()
	{
		init();

		// Vlastne
		throw new RuntimeException("Pouzity neplatny konstruktor!");
	}

	@Override
	public void prepareSimulation()
	{
		super.prepareSimulation();
		// Create global statistcis
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Reset entities, queues, local statistics, etc...

		// Vlastne
		this.customPrepareReplication();
	}

	@Override
	public void replicationFinished()
	{
		// Collect local statistics into global, update UI, etc...
		super.replicationFinished();

		// Vlastne
		this.customReplicationFinished();
	}

	@Override
	public void simulationFinished()
	{
		// Dysplay simulation results
		super.simulationFinished();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		setAgentModel(new AgentModel(Id.agentModel, this, null));
		setAgentOkolie(new AgentOkolie(Id.agentOkolie, this, agentModel()));
		setAgentSystem(new AgentSystem(Id.agentSystem, this, agentModel()));
		setAgentAutomat(new AgentAutomat(Id.agentAutomat, this, agentSystem()));
		setAgentObsluzneMiesta(new AgentObsluzneMiesta(Id.agentObsluzneMiesta, this, agentSystem()));
		setAgentPokladne(new AgentPokladne(Id.agentPokladne, this, agentSystem()));
		setAgentPrevzatieTovar(new AgentPrevzatieTovar(Id.agentPrevzatieTovar, this, agentSystem()));
	}

	private AgentModel _agentModel;

public AgentModel agentModel()
	{ return _agentModel; }

	public void setAgentModel(AgentModel agentModel)
	{_agentModel = agentModel; }

	private AgentOkolie _agentOkolie;

public AgentOkolie agentOkolie()
	{ return _agentOkolie; }

	public void setAgentOkolie(AgentOkolie agentOkolie)
	{_agentOkolie = agentOkolie; }

	private AgentSystem _agentSystem;

public AgentSystem agentSystem()
	{ return _agentSystem; }

	public void setAgentSystem(AgentSystem agentSystem)
	{_agentSystem = agentSystem; }

	private AgentAutomat _agentAutomat;

public AgentAutomat agentAutomat()
	{ return _agentAutomat; }

	public void setAgentAutomat(AgentAutomat agentAutomat)
	{_agentAutomat = agentAutomat; }

	private AgentObsluzneMiesta _agentObsluzneMiesta;

public AgentObsluzneMiesta agentObsluzneMiesta()
	{ return _agentObsluzneMiesta; }

	public void setAgentObsluzneMiesta(AgentObsluzneMiesta agentObsluzneMiesta)
	{_agentObsluzneMiesta = agentObsluzneMiesta; }

	private AgentPokladne _agentPokladne;

public AgentPokladne agentPokladne()
	{ return _agentPokladne; }

	public void setAgentPokladne(AgentPokladne agentPokladne)
	{_agentPokladne = agentPokladne; }

	private AgentPrevzatieTovar _agentPrevzatieTovar;

public AgentPrevzatieTovar agentPrevzatieTovar()
	{ return _agentPrevzatieTovar; }

	public void setAgentPrevzatieTovar(AgentPrevzatieTovar agentPrevzatieTovar)
	{_agentPrevzatieTovar = agentPrevzatieTovar; }
	//meta! tag="end"
}
