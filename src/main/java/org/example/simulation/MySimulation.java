package org.example.simulation;

import OSPABA.*;
import OSPStat.Stat;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.Identifikator;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.agents.*;

public class MySimulation extends Simulation
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;

	private double trvanieSimulacie;
	private boolean zvysenyTokZakaznikov;
	private boolean prestavka;

	private int pocetObycajnychObsluznychMiest;
	private int pocetOnlineObsluznychMiest;
	private int pocetPokladni;

	// Statistiky
	// System
	private Stat statCasSystem;
	private Stat statCasPoslednyOdchod;
	private Stat statPocetPrislychZakaznikov;
	private Stat statPocetObsluzenychZakaznikov;
	private Stat statPocetNeobsluzenychZakaznikov;

	// Automat
	private Stat statCasFrontAutomat;
	private Stat statDlzkaFrontAutomat;
	private Stat statVytazenieFrontAutomat;

	// Obsluzne miesta
	private Stat statCasFrontObsluzneMiesta;
	private Stat statDlzkaFrontObsluzneMiesta;
	private Stat[] statVytazenieObsluzneMiestaObycajniZakaznici;
	private Stat[] statVytazenieObsluzneMiestaOnlineZakaznici;

	private void customInit(int nasada, boolean pouziNasadu, double trvanieSimulacie,
		boolean zvysenyTokZakaznikov, boolean prestavka, int pocetObsluznychMiest, int pocetPokladni)
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

		GeneratorNasad.inicializujGeneratorNasad(nasada, pouziNasadu);
		this.rngGeneratorNasad = new GeneratorNasad();

		this.trvanieSimulacie = trvanieSimulacie;
		this.zvysenyTokZakaznikov = zvysenyTokZakaznikov;
		this.prestavka = prestavka;

		this.pocetOnlineObsluznychMiest = (int)Math.floor(pocetObsluznychMiest / 3.0);
		this.pocetObycajnychObsluznychMiest = pocetObsluznychMiest - this.pocetOnlineObsluznychMiest;
		this.pocetPokladni = pocetPokladni;
	}

	private void customPrepareSimulation()
	{
		// System
		this.statCasSystem = new Stat();
		this.statCasPoslednyOdchod = new Stat();
		this.statPocetPrislychZakaznikov = new Stat();
		this.statPocetObsluzenychZakaznikov = new Stat();
		this.statPocetNeobsluzenychZakaznikov = new Stat();

		// Automat
		this.statCasFrontAutomat = new Stat();
		this.statDlzkaFrontAutomat = new Stat();
		this.statVytazenieFrontAutomat = new Stat();

		// Obsluzne miesta
		this.statCasFrontObsluzneMiesta = new Stat();
		this.statDlzkaFrontObsluzneMiesta = new Stat();

		this.statVytazenieObsluzneMiestaObycajniZakaznici = new Stat[this.pocetObycajnychObsluznychMiest];
		for (int i = 0; i < this.statVytazenieObsluzneMiestaObycajniZakaznici.length; i++)
		{
			this.statVytazenieObsluzneMiestaObycajniZakaznici[i] = new Stat();
		}

		this.statVytazenieObsluzneMiestaOnlineZakaznici = new Stat[this.pocetOnlineObsluznychMiest];
		for (int i = 0; i < this.statVytazenieObsluzneMiestaOnlineZakaznici.length; i++)
		{
			this.statVytazenieObsluzneMiestaOnlineZakaznici[i] = new Stat();
		}
	}

	private void customPrepareReplication()
	{
		// Spustenie simulacie
		Identifikator.resetujID();
		this.agentModel().inicializaciaSimulacie();
	}

	private void customReplicationFinished()
	{
		int ostaloVSysteme = this.agentOkolie().pocetZakaznikovSystem();
		if (ostaloVSysteme != 0)
		{
			throw new RuntimeException("Zakaznik ostal v systeme!");
		}

		if (this.currentReplication() % 1000 == 0 & this.currentReplication() > 0)
		{
			System.out.println("R: " + this.currentReplication());
		}


		// Statistiky
		// System
		this.statCasSystem.addSample(this.agentOkolie().getStatCasSystem().mean());

		double poslednyOdchod = this.agentOkolie().getPoslednyOdchod();
		if (poslednyOdchod != -1)
		{
			this.statCasPoslednyOdchod.addSample(poslednyOdchod);
		}

		this.statPocetPrislychZakaznikov.addSample(this.agentOkolie().getPocetPrislychZakaznikov());
		this.statPocetObsluzenychZakaznikov.addSample(this.agentOkolie().getPocetObsluzenychZakaznikov());
		this.statPocetNeobsluzenychZakaznikov.addSample(this.agentOkolie().getPocetNeobsluzenychZakaznikov());


		// Automat
		this.statCasFrontAutomat.addSample(this.agentAutomat().getStatCasFrontAutomat().mean());
		this.statDlzkaFrontAutomat.addSample(this.agentAutomat().getWstatDlzkaFrontAutomat().mean());
		this.statVytazenieFrontAutomat.addSample(this.agentAutomat().getWstatVytazenieAutomat().mean());


		// Obsluzne miesta
		this.statCasFrontObsluzneMiesta.addSample(this.agentObsluzneMiesta().getStatCasFrontObluzneMiesta().mean());
		this.statDlzkaFrontObsluzneMiesta.addSample(this.agentObsluzneMiesta().getWstatDlzkaFrontObsluzneMiesta().mean());

		for (int i = 0; i < this.statVytazenieObsluzneMiestaObycajniZakaznici.length; i++)
		{
			this.statVytazenieObsluzneMiestaObycajniZakaznici[i]
				.addSample(this.agentObsluzneMiesta().getStatVytazenieObycajneObluzneMiesto(i).mean());
		}

		for (int i = 0; i < this.statVytazenieObsluzneMiestaOnlineZakaznici.length; i++)
		{
			this.statVytazenieObsluzneMiestaOnlineZakaznici[i]
					.addSample(this.agentObsluzneMiesta().getStatVytazenieOnlineObluzneMiesto(i).mean());
		}
	}

	private void customSimulationFinished()
	{
		// System
		System.out.println("\nSystem:");
		System.out.println("Priemerny cas v systeme: " + this.statCasSystem.mean() / 60.0);
		System.out.println("Priemerny cas posledneho odchodu: " + Prezenter.naformatujCas(this.statCasPoslednyOdchod.mean()));
		System.out.println("Priemerny pocet prislych zakaznikov: " + this.statPocetPrislychZakaznikov.mean());
		System.out.println("Priemerny pocet obsluzenych zakazikov: " + this.statPocetObsluzenychZakaznikov.mean());
		System.out.println("Priemerny pocet neobsluzenych zakaznikov: " + this.statPocetNeobsluzenychZakaznikov.mean());


		// Automat
		System.out.println("\nAutomat:");
		System.out.println("Priemerny cas vo fronte pred automatom: " + this.statCasFrontAutomat.mean());
		System.out.println("Priemerna dlzka frontu pred automatom: " + this.statDlzkaFrontAutomat.mean());
		System.out.println("Priemerne vytazenie automatu: " + this.statVytazenieFrontAutomat.mean());


		// Obsluzne miesta
		System.out.println("\nObsluzne miesta:");
		System.out.println("Priemerny cas vo fronte pred obsluznymi miestami: " + this.statCasFrontObsluzneMiesta.mean());
		System.out.println("Priemerna dlzka frontu pred obsluznymi miestami: " + this.statDlzkaFrontObsluzneMiesta.mean());

		System.out.print("Priemerne vytazenie obycajnych obsluznych miest: ");
		for (int i = 0; i < this.statVytazenieObsluzneMiestaObycajniZakaznici.length; i++)
		{
			System.out.print(this.statVytazenieObsluzneMiestaObycajniZakaznici[i].mean() + " ");
		}

		System.out.print("\nPriemerne vytazenie online obsluznych miest: ");
		for (int i = 0; i < this.statVytazenieObsluzneMiestaOnlineZakaznici.length; i++)
		{
			System.out.print(this.statVytazenieObsluzneMiestaOnlineZakaznici[i].mean() + " ");
		}
	}

	public GeneratorNasad getRngGeneratorNasad()
	{
		return this.rngGeneratorNasad;
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


	public MySimulation(int nasada, boolean pouziNasadu, double trvanieSimulacie,
		boolean zvysenyTokZakaznikov, boolean prestavka, int pocetObsluznychMiest, int pocetPokladni)
	{
		// Vlastne
		this.customInit(nasada, pouziNasadu, trvanieSimulacie, zvysenyTokZakaznikov,
				prestavka, pocetObsluznychMiest, pocetPokladni);

		// Vygenerovane
		init();
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

		// Vlastne
		this.customPrepareSimulation();
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

		// Vlastne
		this.customSimulationFinished();
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
