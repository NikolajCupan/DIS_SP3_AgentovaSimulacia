package org.example.agents;

import OSPABA.*;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.GenerovanieVyberFrontu;
import org.example.Vlastne.Pokladna;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

import java.util.ArrayList;

//meta! id="74"
public class AgentPokladne extends Agent
{
	// Vlastne
	private Pokladna[] pokladne;

	private GeneratorNasad rngGeneratorNasad;
	private GenerovanieVyberFrontu rngVyberFrontu;

	private void customAgentPokladne()
	{
		this.addOwnMessage(Mc.holdZaciatokPrestavkaPokladne);
		this.addOwnMessage(Mc.holdKoniecPrestavkaPokladne);
		this.addOwnMessage(Mc.holdObsluhaPokladna);

		this.rngGeneratorNasad = ((MySimulation)this.mySim()).getRngGeneratorNasad();
		this.rngVyberFrontu = new GenerovanieVyberFrontu(this.rngGeneratorNasad);
	}

	private void customPrepareReplication()
	{
		this.pokladne = new Pokladna[((MySimulation)this.mySim()).getPocetPokladni()];
		for (int i = 0; i < this.pokladne.length; i++)
		{
			this.pokladne[i] = new Pokladna(this.mySim());
		}
	}

	public Pokladna vyberPokladnu()
	{
		Pokladna[] dostupnePokladne = this.getDostupnePokladne();

		boolean existujeNeobsadenaPokladna = this.existujeNeobsadenaPokladna(dostupnePokladne);
		if (existujeNeobsadenaPokladna)
		{
			return this.getNeobsadenaPokladna(dostupnePokladne);
		}
		else
		{
			return this.getObsadenaPokladna(dostupnePokladne);
		}
	}

	private Pokladna getObsadenaPokladna(Pokladna[] dostupnePokladne)
	{
		int najmensiFront = this.getNajmensiPocetFrontPokladne(dostupnePokladne);

		ArrayList<Pokladna> pokladneNajmensiFront = new ArrayList<>();
		for (int i = 0; i < dostupnePokladne.length; i++)
		{
			if (dostupnePokladne[i].getPocetFront() < najmensiFront)
			{
				throw new RuntimeException("Bol najdeny mensi front pred pokladnami!");
			}
			else if (dostupnePokladne[i].getPocetFront() == najmensiFront)
			{
				pokladneNajmensiFront.add(dostupnePokladne[i]);
			}
		}

		if (pokladneNajmensiFront.isEmpty())
		{
			throw new RuntimeException("Neexistuje pokladna s najmensim frontom!");
		}

		int vygenerovanyIndex = this.rngVyberFrontu.getIndexFrontu(pokladneNajmensiFront.size());
		return pokladneNajmensiFront.get(vygenerovanyIndex);
	}

	private int getNajmensiPocetFrontPokladne(Pokladna[] dostupnePokladne)
	{
		int najmensiFront = Integer.MAX_VALUE;
		for (int i = 0; i < dostupnePokladne.length; i++)
		{
			if (dostupnePokladne[i].getPocetFront() < najmensiFront)
			{
				najmensiFront = dostupnePokladne[i].getPocetFront();
			}
		}

		return najmensiFront;
	}

	private Pokladna getNeobsadenaPokladna(Pokladna[] dostupnePokladne)
	{
		ArrayList<Pokladna> neobsadenePokladne = new ArrayList<>();
		for (int i = 0; i < dostupnePokladne.length; i++)
		{
			if (!dostupnePokladne[i].getObsadena())
			{
				neobsadenePokladne.add(dostupnePokladne[i]);
			}
		}

		if (neobsadenePokladne.isEmpty())
		{
			throw new RuntimeException("Neexistuje ziadna neobsadena pokladna!");
		}

		int vygenerovanyIndex = this.rngVyberFrontu.getIndexFrontu(neobsadenePokladne.size());
		return neobsadenePokladne.get(vygenerovanyIndex);
	}

	private boolean existujeNeobsadenaPokladna(Pokladna[] dostupnePokladne)
	{
		for (int i = 0; i < dostupnePokladne.length; i++)
		{
			if (!dostupnePokladne[i].getObsadena())
			{
				if (dostupnePokladne[i].getPocetFront() != 0)
				{
					throw new RuntimeException("Existuje pokladna, ktora nie je obsadena a front pred nou nie je prazdny!");
				}

				return true;
			}
		}

		return false;
	}

	private Pokladna[] getDostupnePokladne()
	{
		ArrayList<Pokladna> dostupnePokladne = new ArrayList<>();
		for (int i = 0; i < this.pokladne.length; i++)
		{
			if (this.pokladne[i].pokladnaDostupna())
			{
				dostupnePokladne.add(this.pokladne[i]);
			}
		}

		Pokladna[] polePokladne = new Pokladna[dostupnePokladne.size()];
		return dostupnePokladne.toArray(polePokladne);
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

		this.customPrepareReplication();
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
