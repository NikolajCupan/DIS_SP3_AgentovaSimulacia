package org.example.managers;

import OSPABA.*;
import org.example.Vlastne.Objekty.ObsluzneMiesto;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Zakaznik.TypZakaznik;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="51"
public class ManagerObsluzneMiesta extends Manager
{
	// Vlastne
	public void odosliZamestnanca(ObsluzneMiesto obsluzneMiesto)
	{
		if (this.mySim().currentTime() > Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
		{
			throw new RuntimeException("Zamestnanec nemoze odist po skonceni prestavky!");
		}
		if (!obsluzneMiesto.getOdchodZamestnanec())
		{
			throw new RuntimeException("Zamestnanec nema nastaveny odchod!");
		}
		if (obsluzneMiesto.getObsadene())
		{
			throw new RuntimeException("Obsluzne miesto je obsadene, zamestnanec nemoze odist!");
		}

		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(this.mySim().currentTime()) + " <- zamestnanec odchadza ku pokladniam");
		}

		MyMessage odovzdanie = new MyMessage(this.mySim());
		odovzdanie.setCode(Mc.requestResponseOdovzdanieZamestnanec);
		odovzdanie.setAddressee(Id.agentSystem);
		this.request(odovzdanie);
	}

	private void spustiObsluhu(Zakaznik zakaznik, MessageForm sprava)
	{
		AgentObsluzneMiesta obsluzneMiesta = this.myAgent();
		if (zakaznik.getTypZakaznik() == TypZakaznik.ONLINE)
		{
			sprava.setAddressee(obsluzneMiesta.findAssistant(Id.processObsluhaObsluzneMiestoOnlineZakaznik));
		}
		else
		{
			sprava.setAddressee(obsluzneMiesta.findAssistant(Id.processObsluhaObsluzneMiestoObycajnyZakaznik));
		}

		this.startContinualAssistant(sprava);
	}

	private void spracujObsluzenehoZakaznika(MessageForm message)
	{
		boolean frontPlnyPred = this.myAgent().frontPlny();

		message.setCode(Mc.requestResponseObsluhaObsluzneMiesto);
		this.response(message);

		// Pokus o naplanovanie dalsej obsluhy pri obsluznom mieste
		Zakaznik obsluzenyZakaznik = ((MyMessageZakaznik)message).getZakaznik();
		TypZakaznik typZakaznik = obsluzenyZakaznik.getTypZakaznik();
		AgentObsluzneMiesta obsluzneMiesta = this.myAgent();

		ObsluzneMiesto pouziteObsluzneMiesto = obsluzenyZakaznik.getObsluzneMiesto();
		if (pouziteObsluzneMiesto.getOdchodZamestnanec()
			&& this.mySim().currentTime() > Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
		{
			// Obsluha zakaznika skoncila po prestavke, zamestnanec nie je odoslany pokladni
			pouziteObsluzneMiesto.prichodZamestnanec();
		}

		if (pouziteObsluzneMiesto.getOdchodZamestnanec())
		{
			this.odosliZamestnanca(pouziteObsluzneMiesto);
		}
		else if (pouziteObsluzneMiesto.obsluzneMiestoDostupne())
		{
			// Moze byt obsluhovany dalsi zakaznik
			MyMessageZakaznik dalsiZakaznikSprava = (MyMessageZakaznik)obsluzneMiesta.vyberZakaznika(typZakaznik);
			if (dalsiZakaznikSprava != null)
			{
				Zakaznik dalsiZakaznik = dalsiZakaznikSprava.getZakaznik();
				dalsiZakaznik.setObsluzneMiesto(pouziteObsluzneMiesto);
				this.spustiObsluhu(dalsiZakaznik, dalsiZakaznikSprava);
			}
		}

		boolean frontPlnyPo = this.myAgent().frontPlny();
		if (frontPlnyPred && !frontPlnyPo)
		{
			// Doslo k uvolneniu miesta vo fronte
			this.zapniAutomat();
		}
	}

	private void zapniAutomat()
	{
		if (this.myAgent().frontVelkost() != Konstanty.KAPACITA_FRONT_OBSLUZNE_MIESTA - 1)
		{
			throw new RuntimeException("Automat je zapinany pri nespravnej velkosti frontu!");
		}

		MyMessage uvolnenieFront = new MyMessage(this.mySim());
		uvolnenieFront.setCode(Mc.noticeUvolnenieFront);
		uvolnenieFront.setAddressee(Id.agentSystem);
		this.notice(uvolnenieFront);
	}
	// Vlastne koniec


	public ManagerObsluzneMiesta(int id, Simulation mySim, Agent myAgent)
	{
		super(id, mySim, myAgent);
		init();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		if (petriNet() != null)
		{
			petriNet().clear();
		}
	}

	//meta! sender="AgentSystem", id="125", type="Response"
	public void processRequestResponseOdovzdanieZamestnanec(MessageForm message)
	{
		AgentObsluzneMiesta obsluzneMiesta = this.myAgent();
		ObsluzneMiesto prveObsluzneMiesto = obsluzneMiesta.getPrveObsluzneMiesto();
		prveObsluzneMiesto.prichodZamestnanec();

		// Pokus o naplanovanie dalsej obsluhy pri obsluznom mieste
		boolean frontPlnyPred = this.myAgent().frontPlny();

		if (prveObsluzneMiesto.obsluzneMiestoDostupne())
		{
			// Moze byt obsluhovany dalsi zakaznik
			MyMessageZakaznik dalsiZakaznikSprava = (MyMessageZakaznik)obsluzneMiesta.vyberZakaznika(TypZakaznik.BEZNY);
			if (dalsiZakaznikSprava != null)
			{
				Zakaznik dalsiZakaznik = dalsiZakaznikSprava.getZakaznik();
				dalsiZakaznik.setObsluzneMiesto(prveObsluzneMiesto);
				this.spustiObsluhu(dalsiZakaznik, dalsiZakaznikSprava);
			}
		}

		boolean frontPlnyPo = this.myAgent().frontPlny();
		if (frontPlnyPred && !frontPlnyPo)
		{
			// Doslo k uvolneniu miesta vo fronte
			this.zapniAutomat();
		}
	}

	//meta! sender="MonitorZaciatokPrestavkaObsluzneMiesta", id="109", type="Notice"
	public void processNoticeVnutornaZaciatokPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- zaciatok prestavky obsluzne miesta");
		}

		this.myAgent().zacniPrestavku();
	}

	//meta! sender="AgentSystem", id="53", type="Request"
	public void processRequestResponseObsluhaObsluzneMiesto(MessageForm message)
	{
		AgentObsluzneMiesta obsluzneMiesta = this.myAgent();
		MyMessageZakaznik sprava = (MyMessageZakaznik)message;
		Zakaznik zakaznik = sprava.getZakaznik();
		zakaznik.setPrichodFrontObsluzneMiesta(this.mySim().currentTime());

		obsluzneMiesta.kontrolaNeobsluhovanyZakaznik(zakaznik.getTypZakaznik());
		boolean existujeVolneObsluzneMiesto = obsluzneMiesta.existujeVolneObsluzneMiesto(zakaznik.getTypZakaznik());
		if (existujeVolneObsluzneMiesto)
		{
			// Zakaznik moze byt obsluhovany
			ObsluzneMiesto obsluzneMiesto = obsluzneMiesta.vyberObsluzneMiesto(zakaznik.getTypZakaznik());
			zakaznik.setObsluzneMiesto(obsluzneMiesto);
			this.spustiObsluhu(zakaznik, sprava);
		}
		else
		{
			obsluzneMiesta.pridajFront(message);
		}
	}

	//meta! sender="AgentSystem", id="98", type="Notice"
	public void processNoticeUvolnenieObsluzneMiesto(MessageForm message)
	{
		// Uvolnenie obsluzneho miesta
		MyMessageZakaznik sprava = (MyMessageZakaznik)message;

		Zakaznik zakaznik = sprava.getZakaznik();
		TypZakaznik typZakaznik = zakaznik.getTypZakaznik();

		ObsluzneMiesto uvolneneObsluzneMiesto = zakaznik.getObsluzneMiesto();
		uvolneneObsluzneMiesto.setOdlozenyTovar(false);


		// Pokus o naplanovanie dalsej obsluhy pri danom obsluznom mieste
		if (uvolneneObsluzneMiesto.getOdchodZamestnanec())
		{
			// Obsluzne miesto nepracuje, nemozno naplanovat dalsiu obsluhu
			return;
		}

		boolean frontPlnyPred = this.myAgent().frontPlny();
		AgentObsluzneMiesta obsluzneMiesta = this.myAgent();

		MyMessageZakaznik dalsiZakaznikSprava = (MyMessageZakaznik)obsluzneMiesta.vyberZakaznika(typZakaznik);
		if (dalsiZakaznikSprava != null)
		{
			Zakaznik dalsiZakaznik = dalsiZakaznikSprava.getZakaznik();
			dalsiZakaznik.setObsluzneMiesto(uvolneneObsluzneMiesto);
			this.spustiObsluhu(dalsiZakaznik, dalsiZakaznikSprava);
		}

		boolean frontPlnyPo = this.myAgent().frontPlny();
		if (frontPlnyPred && !frontPlnyPo)
		{
			// Doslo k uvolneniu miesta vo fronte
			this.zapniAutomat();
		}
	}

	//meta! sender="AgentSystem", id="71", type="Request"
	public void processRequestResponseNaplnenieFront(MessageForm message)
	{
		MyMessage sprava = (MyMessage)message;
		sprava.setVypnutieAutomat(this.myAgent().frontPlny());
		this.response(message);
	}

	//meta! sender="ProcessObsluhaObsluzneMiestoOnlineZakaznik", id="81", type="Finish"
	public void processFinishProcessObsluhaObsluzneMiestoOnlineZakaznik(MessageForm message)
	{
		this.spracujObsluzenehoZakaznika(message);
	}

	//meta! sender="MonitorKoniecPrestavkaObsluzneMiesta", id="111", type="Finish"
	public void processFinishMonitorKoniecPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor konca prestavky obsluzne miesta");
		}
	}

	//meta! sender="MonitorZaciatokPrestavkaObsluzneMiesta", id="108", type="Finish"
	public void processFinishMonitorZaciatokPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor zaciatku prestavky obsluzne miesta");
		}
	}

	//meta! sender="ProcessObsluhaObsluzneMiestoObycajnyZakaznik", id="79", type="Finish"
	public void processFinishProcessObsluhaObsluzneMiestoObycajnyZakaznik(MessageForm message)
	{
		this.spracujObsluzenehoZakaznika(message);
	}

	//meta! sender="MonitorKoniecPrestavkaObsluzneMiesta", id="112", type="Notice"
	public void processNoticeVnutornaKoniecPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- koniec prestavky obsluzne miesta");
		}
	}

	//meta! sender="AgentSystem", id="169", type="Notice"
	public void processNoticeInicializaciaObsluzneMiesta(MessageForm message)
	{
		if (((MySimulation)this.mySim()).getPrestavka())
		{
			MyMessage zaciatokPrestavka = new MyMessage(this.mySim());
			zaciatokPrestavka.setAddressee(this.myAgent().findAssistant(Id.monitorZaciatokPrestavkaObsluzneMiesta));
			this.startContinualAssistant(zaciatokPrestavka);

			MyMessage koniecPrestavka = new MyMessage(this.mySim());
			koniecPrestavka.setAddressee(this.myAgent().findAssistant(Id.monitorKoniecPrestavkaObsluzneMiesta));
			this.startContinualAssistant(koniecPrestavka);
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	public void init()
	{
	}

	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.noticeUvolnenieObsluzneMiesto:
			processNoticeUvolnenieObsluzneMiesto(message);
		break;

		case Mc.noticeVnutornaZaciatokPrestavkaObsluzneMiesta:
			processNoticeVnutornaZaciatokPrestavkaObsluzneMiesta(message);
		break;

		case Mc.requestResponseOdovzdanieZamestnanec:
			processRequestResponseOdovzdanieZamestnanec(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processObsluhaObsluzneMiestoOnlineZakaznik:
				processFinishProcessObsluhaObsluzneMiestoOnlineZakaznik(message);
			break;

			case Id.monitorKoniecPrestavkaObsluzneMiesta:
				processFinishMonitorKoniecPrestavkaObsluzneMiesta(message);
			break;

			case Id.monitorZaciatokPrestavkaObsluzneMiesta:
				processFinishMonitorZaciatokPrestavkaObsluzneMiesta(message);
			break;

			case Id.processObsluhaObsluzneMiestoObycajnyZakaznik:
				processFinishProcessObsluhaObsluzneMiestoObycajnyZakaznik(message);
			break;
			}
		break;

		case Mc.noticeInicializaciaObsluzneMiesta:
			processNoticeInicializaciaObsluzneMiesta(message);
		break;

		case Mc.requestResponseNaplnenieFront:
			processRequestResponseNaplnenieFront(message);
		break;

		case Mc.requestResponseObsluhaObsluzneMiesto:
			processRequestResponseObsluhaObsluzneMiesto(message);
		break;

		case Mc.noticeVnutornaKoniecPrestavkaObsluzneMiesta:
			processNoticeVnutornaKoniecPrestavkaObsluzneMiesta(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentObsluzneMiesta myAgent()
	{
		return (AgentObsluzneMiesta)super.myAgent();
	}

}
