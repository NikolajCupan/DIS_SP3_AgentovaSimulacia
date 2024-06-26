package org.example.agents;

import OSPABA.*;
import OSPStat.Stat;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;

import java.util.*;

//meta! id="4"
public class AgentOkolie extends Agent
{
	// Vlastne
	private List<Zakaznik> zakazniciSystem;

	// Statistiky
	private Stat statCasSystem;
	private double casPoslednyOdchod;

	private int celkovyPocetZakaznikov;
	private int pocetObsluzenychZakaznikov;
	private int pocetNeobsluzenychZakaznikov;

	private void customAgentOkolie()
	{
		this.addOwnMessage(Mc.holdPrichodBeznyZakaznik);
		this.addOwnMessage(Mc.holdPrichodZmluvnyZakaznik);
		this.addOwnMessage(Mc.holdPrichodOnlineZakaznik);
	}

	private void customPrepareReplication()
	{
		this.zakazniciSystem = Collections.synchronizedList(new ArrayList<>());

		// Statistiky
		this.statCasSystem = new Stat();
		this.casPoslednyOdchod = Double.MIN_VALUE;

		this.celkovyPocetZakaznikov = 0;
		this.pocetObsluzenychZakaznikov = 0;
		this.pocetNeobsluzenychZakaznikov = 0;
	}

	public Collection<Zakaznik> getZakazniciSystem()
	{
		ArrayList<Zakaznik> kopieZakaznikov = new ArrayList<>();
		this.zakazniciSystem.forEach(e -> kopieZakaznikov.add(e.clone()));
		return Collections.unmodifiableCollection(kopieZakaznikov);
	}

	public int pocetZakaznikovSystem()
	{
		return this.zakazniciSystem.size();
	}

	public void pridajZakaznikaDoSystemu(Zakaznik zakaznik)
	{
		this.celkovyPocetZakaznikov++;
		this.zakazniciSystem.add(zakaznik);
	}

	public void odoberZakaznikaZoSystemu(Zakaznik zakaznik)
	{
		if (!this.zakazniciSystem.contains(zakaznik))
		{
			throw new RuntimeException("System neobsahuje daneho zakaznika!");
		}

		if (!zakaznik.getPredcasnyOdchod())
		{
			// Statistiky zakaznika sa beru do uvahy
			this.pocetObsluzenychZakaznikov++;
			this.statCasSystem.addSample(zakaznik.getCasSystem());
		}
		else
		{
			this.pocetNeobsluzenychZakaznikov++;
		}

		if (zakaznik.getCasOdchodSystem() > this.casPoslednyOdchod)
		{
			this.casPoslednyOdchod = zakaznik.getCasOdchodSystem();
		}

		this.zakazniciSystem.remove(zakaznik);

		// V pripade ak simulacia bezi v spomalenom rezime, je nutne manualne zastavit replikaciu
		((MySimulation)this.mySim()).skontrolujUkoncenieReplikacie();
	}

	public Stat getStatCasSystem()
	{
		return this.statCasSystem;
	}

	public double getPoslednyOdchod()
	{
		if (this.casPoslednyOdchod < 0)
		{
			return -1;
		}

		return this.casPoslednyOdchod;
	}

	public int getPocetPrislychZakaznikov()
	{
		return this.celkovyPocetZakaznikov;
	}

	public int getPocetObsluzenychZakaznikov()
	{
		return this.pocetObsluzenychZakaznikov;
	}

	public int getPocetNeobsluzenychZakaznikov()
	{
		return this.pocetNeobsluzenychZakaznikov;
	}
	// Vlastne koniec


	public AgentOkolie(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentOkolie();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		// Vlastne
		this.customPrepareReplication();
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
