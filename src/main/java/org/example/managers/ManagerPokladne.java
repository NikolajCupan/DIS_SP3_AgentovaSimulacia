package org.example.managers;

import OSPABA.*;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Objekty.Pokladna;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="74"
public class ManagerPokladne extends Manager
{
	public ManagerPokladne(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="MonitorKoniecPrestavkaPokladne", id="106", type="Notice"
	public void processNoticeVnutornaKoniecPrestavkaPokladne(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- koniec prestavky pokladne");
		}

		this.myAgent().ukonciPrestavku();
	}

	//meta! sender="MonitorZaciatokPrestavkaPokladne", id="103", type="Notice"
	public void processNoticeVnutornaZaciatokPrestavkaPokladne(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- zaciatok prestavky pokladne");
		}

		this.myAgent().zacniPrestavku();
	}

	//meta! sender="AgentSystem", id="85", type="Request"
	public void processRequestResponseObsluhaPokladna(MessageForm message)
	{
		AgentPokladne pokladne = this.myAgent();
		Pokladna pokladna = pokladne.vyberPokladnu();

		MyMessageZakaznik sprava = (MyMessageZakaznik)message;
		Zakaznik zakaznik = sprava.getZakaznik();
		zakaznik.setPrichodFrontPokladna(this.mySim().currentTime());

		if (!pokladna.getObsadena() && pokladna.pokladnaDostupna())
		{
			// Zakaznik moze byt obsluhovany pri danej pokladni
			zakaznik.setPokladna(pokladna);
			sprava.setAddressee(pokladne.findAssistant(Id.processObsluhaPokladna));
			this.startContinualAssistant(sprava);
		}
		else
		{
			pokladna.vlozFront(message);
		}
	}

	//meta! sender="AgentSystem", id="126", type="Request"
	public void processRequestResponsePrijatieZamestnanec(MessageForm message)
	{
	}

	//meta! sender="AgentSystem", id="172", type="Notice"
	public void processNoticeInicializaciaPokladne(MessageForm message)
	{
		if (((MySimulation)this.mySim()).getPrestavka())
		{
			MyMessage zaciatokPrestavka = new MyMessage(this.mySim());
			zaciatokPrestavka.setAddressee(this.myAgent().findAssistant(Id.monitorZaciatokPrestavkaPokladne));
			this.startContinualAssistant(zaciatokPrestavka);

			MyMessage koniecPrestavka = new MyMessage(this.mySim());
			koniecPrestavka.setAddressee(this.myAgent().findAssistant(Id.monitorKoniecPrestavkaPokladne));
			this.startContinualAssistant(koniecPrestavka);
		}
	}

	//meta! sender="ProcessObsluhaPokladna", id="88", type="Finish"
	public void processFinishProcessObsluhaPokladna(MessageForm message)
	{
		// Spracovanie obsluzeneho zakaznika
		message.setCode(Mc.requestResponseObsluhaPokladna);
		this.response(message);

		// Pokus o naplanovanie dalsej obsluhy pri pokladni
		AgentPokladne pokladne = this.myAgent();
		Pokladna pokladna = ((MyMessageZakaznik)message).getZakaznik().getPokladna();
		if (!pokladna.frontPrazdny() && pokladna.pokladnaDostupna())
		{
			MyMessageZakaznik dalsiZakaznikSprava = (MyMessageZakaznik)pokladna.vyberFront();
			Zakaznik dalsiZakaznik = dalsiZakaznikSprava.getZakaznik();
			dalsiZakaznik.setPokladna(pokladna);
			dalsiZakaznikSprava.setAddressee(pokladne.findAssistant(Id.processObsluhaPokladna));
			this.startContinualAssistant(dalsiZakaznikSprava);
		}
	}

	//meta! sender="MonitorZaciatokPrestavkaPokladne", id="102", type="Finish"
	public void processFinishMonitorZaciatokPrestavkaPokladne(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor zaciatku prestavky pokladne");
		}
	}

	//meta! sender="MonitorKoniecPrestavkaPokladne", id="105", type="Finish"
	public void processFinishMonitorKoniecPrestavkaPokladne(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor konca prestavky pokladne");
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
		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.processObsluhaPokladna:
				processFinishProcessObsluhaPokladna(message);
			break;

			case Id.monitorZaciatokPrestavkaPokladne:
				processFinishMonitorZaciatokPrestavkaPokladne(message);
			break;

			case Id.monitorKoniecPrestavkaPokladne:
				processFinishMonitorKoniecPrestavkaPokladne(message);
			break;
			}
		break;

		case Mc.requestResponsePrijatieZamestnanec:
			processRequestResponsePrijatieZamestnanec(message);
		break;

		case Mc.noticeVnutornaKoniecPrestavkaPokladne:
			processNoticeVnutornaKoniecPrestavkaPokladne(message);
		break;

		case Mc.noticeVnutornaZaciatokPrestavkaPokladne:
			processNoticeVnutornaZaciatokPrestavkaPokladne(message);
		break;

		case Mc.noticeInicializaciaPokladne:
			processNoticeInicializaciaPokladne(message);
		break;

		case Mc.requestResponseObsluhaPokladna:
			processRequestResponseObsluhaPokladna(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

}
