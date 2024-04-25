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

	private GeneratorNasad generatorNasad;
	private GenerovanieVyberFrontu rngVyberFrontu;

	private void customAgentPokladne()
	{
		this.addOwnMessage(Mc.holdZaciatokPrestavkaPokladne);
		this.addOwnMessage(Mc.holdKoniecPrestavkaPokladne);

		this.generatorNasad = new GeneratorNasad();
		this.rngVyberFrontu = new GenerovanieVyberFrontu(this.generatorNasad);
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
		Pokladna[] fungujucePokladne = this.getFungujucePokladne();

		boolean existujeNeobsadenaPokladna = this.existujeNeobsadenaPokladna(fungujucePokladne);
		if (existujeNeobsadenaPokladna)
		{
			return this.getNeobsadenaPokladna(fungujucePokladne);
		}
		else
		{
			return this.getObsadenaPokladna(fungujucePokladne);
		}
	}

	private Pokladna getObsadenaPokladna(Pokladna[] fungujucePokladne)
	{
		int najmensiFront = this.getNajmensiPocetFrontPokladne(fungujucePokladne);

		ArrayList<Pokladna> pokladneNajmensiFront = new ArrayList<>();
		for (int i = 0; i < fungujucePokladne.length; i++)
		{
			if (fungujucePokladne[i].getPocetFront() < najmensiFront)
			{
				throw new RuntimeException("Bol najdeny mensi front pred pokladnami!");
			}
			else if (fungujucePokladne[i].getPocetFront() == najmensiFront)
			{
				pokladneNajmensiFront.add(fungujucePokladne[i]);
			}
		}

		if (pokladneNajmensiFront.isEmpty())
		{
			throw new RuntimeException("Neexistuje pokladna s najmensim frontom!");
		}

		int vygenerovanyIndex = this.rngVyberFrontu.getIndexFrontu(pokladneNajmensiFront.size());
		return pokladneNajmensiFront.get(vygenerovanyIndex);
	}

	private int getNajmensiPocetFrontPokladne(Pokladna[] fungujucePokladne)
	{
		int najmensiFront = Integer.MAX_VALUE;
		for (int i = 0; i < fungujucePokladne.length; i++)
		{
			if (fungujucePokladne[i].getPocetFront() < najmensiFront)
			{
				najmensiFront = fungujucePokladne[i].getPocetFront();
			}
		}

		return najmensiFront;
	}

	private Pokladna getNeobsadenaPokladna(Pokladna[] fungujucePokladne)
	{
		ArrayList<Pokladna> neobsadenePokladne = new ArrayList<>();
		for (int i = 0; i < fungujucePokladne.length; i++)
		{
			if (!fungujucePokladne[i].getObsadena())
			{
				neobsadenePokladne.add(fungujucePokladne[i]);
			}
		}

		if (neobsadenePokladne.isEmpty())
		{
			throw new RuntimeException("Neexistuje ziadna neobsadena pokladna!");
		}

		int vygenerovanyIndex = this.rngVyberFrontu.getIndexFrontu(neobsadenePokladne.size());
		return neobsadenePokladne.get(vygenerovanyIndex);
	}

	private boolean existujeNeobsadenaPokladna(Pokladna[] fungujucePokladne)
	{
		for (int i = 0; i < fungujucePokladne.length; i++)
		{
			if (!fungujucePokladne[i].getObsadena())
			{
				if (fungujucePokladne[i].getPocetFront() != 0)
				{
					throw new RuntimeException("Existuje pokladna, ktora nie je obsadena a front pred nou nie je prazdny!");
				}

				return true;
			}
		}

		return false;
	}

	private Pokladna[] getFungujucePokladne()
	{
		ArrayList<Pokladna> fungujucePokladne = new ArrayList<>();
		for (int i = 0; i < this.pokladne.length; i++)
		{
			if (!this.pokladne[i].getPrestavka())
			{
				fungujucePokladne.add(this.pokladne[i]);
			}
		}

		Pokladna[] pole = new Pokladna[fungujucePokladne.size()];
		return fungujucePokladne.toArray(pole);
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
