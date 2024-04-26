package org.example.agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPRNG.UniformContinuousRNG;
import OSPStat.Stat;
import OSPStat.WStat;
import org.example.Vlastne.Generatory.GeneratorNasad;
import org.example.Vlastne.Generatory.VelkostTovaru;
import org.example.Vlastne.Objekty.ObsluzneMiesto;
import org.example.Vlastne.Objekty.TypOkno;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Zakaznik.TypZakaznik;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

//meta! id="51"
public class AgentObsluzneMiesta extends Agent
{
	// Vlastne
	private SimQueue<MessageForm> frontObsluzneMiesta;
	private ObsluzneMiesto[] obsluzneMiestaObycajniZakaznici;
	private ObsluzneMiesto[] obsluzneMiestaOnlineZakaznici;

	private GeneratorNasad rngGeneratorNasad;
	private UniformContinuousRNG rngVelkostTovaru;

	// Statistiky
	private Stat statCasFrontObluzneMiesta;

	private void customAgentObsluzneMiesta()
	{
		this.addOwnMessage(Mc.holdZaciatokPrestavkaObsluzneMiesta);
		this.addOwnMessage(Mc.holdKoniecPrestavkaObsluzneMiesta);
		this.addOwnMessage(Mc.holdObsluhaObsluzneMiestoObycajnyZakaznik);
		this.addOwnMessage(Mc.holdObsluhaObsluzneMiestoOnlineZakaznik);

		this.rngGeneratorNasad = ((MySimulation)this.mySim()).getRngGeneratorNasad();
		this.rngVelkostTovaru = new UniformContinuousRNG(0.0, 1.0, this.rngGeneratorNasad.generator());
	}

	private void customPrepareReplication()
	{
		MySimulation simulacia = (MySimulation)this.mySim();
		this.frontObsluzneMiesta = new SimQueue<>(new WStat(simulacia));

		this.obsluzneMiestaObycajniZakaznici = new ObsluzneMiesto[simulacia.getPocetObycajnychObsluznychMiest()];
		for (int i = 0; i < this.obsluzneMiestaObycajniZakaznici.length; i++)
		{
			this.obsluzneMiestaObycajniZakaznici[i] = new ObsluzneMiesto(TypOkno.OBYCAJNE, this.mySim());
		}

		this.obsluzneMiestaOnlineZakaznici = new ObsluzneMiesto[simulacia.getPocetOnlineObsluznychMiest()];
		for (int i = 0; i < this.obsluzneMiestaOnlineZakaznici.length; i++)
		{
			this.obsluzneMiestaOnlineZakaznici[i] = new ObsluzneMiesto(TypOkno.ONLINE, this.mySim());
		}

		// Statistiky
		this.statCasFrontObluzneMiesta = new Stat();
	}

	public VelkostTovaru getVelkostTovaru()
	{
		double sample = this.rngVelkostTovaru.sample();
		if (sample < 0.6)
		{
			return VelkostTovaru.VELKY;
		}
		else
		{
			return VelkostTovaru.MALY;
		}
	}

	public ObsluzneMiesto vyberObsluzneMiesto(TypZakaznik typZakaznika)
	{
		ObsluzneMiesto[] prehladavaneObsluzneMiesta =
			(typZakaznika == TypZakaznik.ONLINE) ? this.obsluzneMiestaOnlineZakaznici : this.obsluzneMiestaObycajniZakaznici;

		for (int i = 0; i < prehladavaneObsluzneMiesta.length; i++)
		{
			if (prehladavaneObsluzneMiesta[i].obsluzneMiestoDostupne())
			{
				return prehladavaneObsluzneMiesta[i];
			}
		}

		throw new RuntimeException("Nebolo najdene volne obsluzne miesto na pridelenie!");
	}

	public boolean existujeVolneObsluzneMiesto(TypZakaznik typZakaznika)
	{
		ObsluzneMiesto[] prehladavaneObsluzneMiesta =
			(typZakaznika == TypZakaznik.ONLINE) ? this.obsluzneMiestaOnlineZakaznici : this.obsluzneMiestaObycajniZakaznici;

		for (int i = 0; i < prehladavaneObsluzneMiesta.length; i++)
		{
			if (prehladavaneObsluzneMiesta[i].obsluzneMiestoDostupne())
			{
				return true;
			}
		}

		return false;
	}

	public void kontrolaNeobsluhovanyZakaznik(TypZakaznik typZakaznika)
	{
		if (!this.existujeVolneObsluzneMiesto(typZakaznika))
		{
			return;
		}

		for (MessageForm sprava : this.frontObsluzneMiesta)
		{
			Zakaznik zakaznik = ((MyMessageZakaznik)sprava).getZakaznik();

			if (typZakaznika == TypZakaznik.ONLINE)
			{
				if (zakaznik.getTypZakaznik() == TypZakaznik.ONLINE)
				{
					throw new RuntimeException("Online zakaznik vo fronte pred obsluznymi miestami by mal byt obsluhovany!");
				}
			}
			else if (typZakaznika == TypZakaznik.BEZNY || typZakaznika == TypZakaznik.ZMLUVNY)
			{
				if (zakaznik.getTypZakaznik() == TypZakaznik.BEZNY || zakaznik.getTypZakaznik() == TypZakaznik.ZMLUVNY)
				{
					throw new RuntimeException("Obycajny zakaznik vo fronte pred obsluznymi miestami by mal byt obsluhovany!");
				}
			}
			else
			{
				throw new RuntimeException("Neplatny typ zakaznika!");
			}
		}
	}

	public Collection<MessageForm> getFront()
	{
		return Collections.unmodifiableCollection(this.frontObsluzneMiesta);
	}

	public Collection<ObsluzneMiesto> getObycajneObsluzneMiesta()
	{
		return Collections.unmodifiableCollection(Arrays.asList(this.obsluzneMiestaObycajniZakaznici));
	}

	public Collection<ObsluzneMiesto> getOnlineObsluzneMiesta()
	{
		return Collections.unmodifiableCollection(Arrays.asList(this.obsluzneMiestaOnlineZakaznici));
	}

	public void pridajFront(MessageForm message)
	{
		if (this.frontObsluzneMiesta.size() == 9)
		{
			throw new RuntimeException("Nemozno pridat zakaznika do frontu pred obsluznymi miestami, pretoze front je plny!");
		}
		if (this.frontObsluzneMiesta.contains(message))
		{
			throw new RuntimeException("Front pred obsluznymi mietami uz obsahuje daneho zakaznika!");
		}

		this.frontObsluzneMiesta.add(message);
	}

	public MessageForm vyberZakaznika(TypZakaznik typZakaznik)
	{
		if (this.frontObsluzneMiesta.isEmpty())
		{
			return null;
		}

		return typZakaznik == TypZakaznik.ONLINE
			? this.vyberPrvyOnlineZakaznik() : this.vyberPrvyObycajnyZakaznik();
	}

	private MessageForm vyberPrvyOnlineZakaznik()
	{
		for (MessageForm sprava : this.frontObsluzneMiesta)
		{
			Zakaznik zakaznik = ((MyMessageZakaznik)sprava).getZakaznik();
			if (zakaznik.getTypZakaznik() == TypZakaznik.ONLINE)
			{
				this.frontObsluzneMiesta.remove(sprava);
				return sprava;
			}
		}

		return null;
	}

	private MessageForm vyberPrvyObycajnyZakaznik()
	{
		boolean obsahujeZmluvneho = this.frontOknoObsahujeZmluvnehoZakaznika();
		TypZakaznik vyberanyTyp = (obsahujeZmluvneho ? TypZakaznik.ZMLUVNY : TypZakaznik.BEZNY);

		for (MessageForm sprava : this.frontObsluzneMiesta)
		{
			Zakaznik zakaznik = ((MyMessageZakaznik)sprava).getZakaznik();
			if (zakaznik.getTypZakaznik() == vyberanyTyp)
			{
				this.frontObsluzneMiesta.remove(sprava);
				return sprava;
			}
		}

		return null;
	}

	private boolean frontOknoObsahujeZmluvnehoZakaznika()
	{
		for (MessageForm sprava : this.frontObsluzneMiesta)
		{
			Zakaznik zakaznik = ((MyMessageZakaznik)sprava).getZakaznik();
			if (zakaznik.getTypZakaznik() == TypZakaznik.ZMLUVNY)
			{
				return true;
			}
		}

		return false;
	}

	public boolean frontPlny()
	{
		if (this.frontObsluzneMiesta.size() > Konstanty.KAPACITA_FRONT_OBSLUZNE_MIESTA)
		{
			throw new RuntimeException("Front pred obsluznymi miestami presiahol svoju kapacitu!");
		}

		return this.frontObsluzneMiesta.size() == Konstanty.KAPACITA_FRONT_OBSLUZNE_MIESTA;
    }

    public int frontVelkost()
	{
		return this.frontObsluzneMiesta.size();
	}

	public void zacniPrestavku()
	{
		// TODO: zaciatok presavky obsluzne miesto
	}

	public void ukonciPrestavku()
	{
		// TODO: koniec prestavky obsluzne miesto
	}

	public void pridajCasFrontObsluzneMiesta(double cas)
	{
		this.statCasFrontObluzneMiesta.addSample(cas);
	}

	public Stat getStatCasFrontObluzneMiesta()
	{
		return this.statCasFrontObluzneMiesta;
	}

	public WStat getWstatDlzkaFrontObsluzneMiesta()
	{
		return this.frontObsluzneMiesta.lengthStatistic();
	}

	public WStat getWstatVytazenieObycajneObluzneMiesto(int index)
	{
		return this.obsluzneMiestaObycajniZakaznici[index].getWstatVytazenieObsluzneMiesto();
	}

	public WStat getWstatVytazenieOnlineObluzneMiesto(int index)
	{
		return this.obsluzneMiestaOnlineZakaznici[index].getWstatVytazenieObsluzneMiesto();
	}

	public void aktualizujStatistikyPoReplikacii()
	{
		this.frontObsluzneMiesta.lengthStatistic().updateAfterReplication();

		for (int i = 0; i < this.obsluzneMiestaObycajniZakaznici.length; i++)
		{
			this.obsluzneMiestaObycajniZakaznici[i].aktualizujStatistikyPoReplikacii();
		}

		for (int i = 0; i < this.obsluzneMiestaOnlineZakaznici.length; i++)
		{
			this.obsluzneMiestaOnlineZakaznici[i].aktualizujStatistikyPoReplikacii();
		}
	}
	// Vlastne koniec


	public AgentObsluzneMiesta(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentObsluzneMiesta();
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
