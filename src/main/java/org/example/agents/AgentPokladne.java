package org.example.agents;

import OSPABA.*;
import OSPStat.Stat;
import OSPStat.WStat;
import org.example.Vlastne.Generatory.GeneratorNasad;
import org.example.Vlastne.Generatory.GenerovanieVyberFrontu;
import org.example.Vlastne.Objekty.Pokladna;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

//meta! id="74"
public class AgentPokladne extends Agent
{
	// Vlastne
	private Pokladna[] pokladne;

	private GeneratorNasad rngGeneratorNasad;
	private GenerovanieVyberFrontu rngVyberFrontu;

	// Prestavka
	private boolean prestavkaAktivna;
	private MessageForm zamestnanecSprava;

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
		this.prestavkaAktivna = false;
		this.zamestnanecSprava = null;

		this.pokladne = new Pokladna[((MySimulation)this.mySim()).getPocetPokladni()];
		for (int i = 0; i < this.pokladne.length; i++)
		{
			this.pokladne[i] = new Pokladna(this.mySim());
		}
	}

	public void skontrolujVratenieZamestnanca(Pokladna pokladna)
	{
		if (pokladna.getNahrada() && !this.prestavkaAktivna)
		{
			if (this.mySim().currentTime() < Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
			{
				throw new RuntimeException("Prestavka je neaktivna prilis skoro!");
			}

			if (this.zamestnanecSprava != null)
			{
				this.odchodZamestnanec();
			}
		}
	}

	public void prichodZamestnanec(MessageForm zamestnanecSprava)
	{
		this.zamestnanecSprava = zamestnanecSprava;
		this.pokladne[0].setNahrada(true);

		if (!this.pokladne[0].getObsadena() && this.pokladne[0].getPocetFront() != 0)
		{
			if (!this.pokladne[0].pokladnaDostupna())
			{
				throw new RuntimeException("Chyba stavu prvej pokladne!");
			}

			// Pri danej pokladni moze zacat obsluha
			MyMessageZakaznik dalsiZakaznikSprava = (MyMessageZakaznik)this.pokladne[0].vyberFront();
			Zakaznik dalsiZakaznik = dalsiZakaznikSprava.getZakaznik();
			dalsiZakaznik.setPokladna(this.pokladne[0]);
			dalsiZakaznikSprava.setAddressee(this.findAssistant(Id.processObsluhaPokladna));
			this.manager().startContinualAssistant(dalsiZakaznikSprava);
		}
	}

	public void odchodZamestnanec()
	{
		if (this.zamestnanecSprava == null)
		{
			throw new RuntimeException("Agent pokladne neobsahuje spravu o prijati zamestnanca!");
		}
		if (this.pokladne[0].getObsadena())
		{
			throw new RuntimeException("Zamestnanec nemoze odist pocas trvania obsluhy!");
		}

		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(this.mySim().currentTime()) + " <- zamestnanec sa vracia od pokladni");
		}

		this.zamestnanecSprava.setCode(Mc.requestResponsePrijatieZamestnanec);
		this.manager().response(this.zamestnanecSprava);
		this.zamestnanecSprava = null;
	}

	public Pokladna vyberPokladnu()
	{
		Pokladna[] dostupnePokladne = this.getDostupnePokladne();
		if (dostupnePokladne.length == 0 && !this.prestavkaAktivna)
		{
			throw new RuntimeException("Neexistuje dostupna pokladna, hoci neprebieha prestavka!");
		}
		if (dostupnePokladne.length == 0)
		{
			// Ak prebieha prestavka, zakaznik ide k 1. pokladni
			return this.pokladne[0];
		}

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
		this.prestavkaAktivna = true;

		for (int i = 0; i < this.pokladne.length; i++)
		{
			this.pokladne[i].zaciatokPrestavkaPokladna();
		}

		for (int i = 1; i < this.pokladne.length; i++)
		{
			this.pokladne[i].presunSvojFront(this.pokladne[0]);
		}
	}

	public void ukonciPrestavku()
	{
		this.prestavkaAktivna = false;

		for (int i = 0; i < this.pokladne.length; i++)
		{
			this.pokladne[i].koniecPrestavkaPokladna();
		}

		Pokladna prvaPokladna = this.pokladne[0];
		if (!prvaPokladna.getObsadena() && prvaPokladna.getNahrada()
			&& prvaPokladna.getPocetFront() != 0)
		{
			throw new RuntimeException("Pri prvej pokladni by mala prebiehat obsluha!");
		}
		else if (!prvaPokladna.getObsadena() && prvaPokladna.getNahrada()
			&& prvaPokladna.getPocetFront() == 0)
		{
			// Zamestnanec moze odist
			this.odchodZamestnanec();
		}
		else if (!prvaPokladna.getObsadena() && !prvaPokladna.getNahrada()
			&& prvaPokladna.getPocetFront() != 0)
		{
			// Dana pokladna moze zacat obsluhovat dalsieho zakaznika
			MyMessageZakaznik dalsiZakaznikSprava = (MyMessageZakaznik)prvaPokladna.vyberFront();
			Zakaznik dalsiZakaznik = dalsiZakaznikSprava.getZakaznik();
			dalsiZakaznik.setPokladna(prvaPokladna);
			dalsiZakaznikSprava.setAddressee(this.findAssistant(Id.processObsluhaPokladna));
			this.manager().startContinualAssistant(dalsiZakaznikSprava);
		}
	}

	public Collection<Pokladna> getPokladne()
	{
		return Collections.unmodifiableCollection(Arrays.asList(this.pokladne));
	}

	public Stat getStatCasFrontPokladna(int index)
	{
		return this.pokladne[index].getStatCasFrontPokladna();
	}

	public WStat getWstatVytazeniePokladna(int index)
	{
		return this.pokladne[index].getWstatVytazeniePokladna();
	}

	public WStat getWstatDlzkaFrontPokladna(int index)
	{
		return this.pokladne[index].getWstatDlzkaFrontPokladna();
	}

	public void aktualizujStatistikyPoReplikacii()
	{
		for (int i = 0; i < this.pokladne.length; i++)
		{
			this.pokladne[i].aktualizujStatistikyPoReplikacii();
		}
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
