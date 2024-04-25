package org.example.managers;

import OSPABA.*;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="51"
public class ManagerObsluzneMiesta extends Manager
{
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
	}

	//meta! sender="MonitorZaciatokPrestavkaObsluzneMiesta", id="109", type="Notice"
	public void processNoticeVnutornaZaciatokPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- zaciatok prestavky obsluzne miesto");
		}

		this.myAgent().zacniPrestavku();
	}

	//meta! sender="AgentSystem", id="53", type="Request"
	public void processRequestResponseObsluhaObsluzneMiesto(MessageForm message)
	{
	}

	//meta! sender="AgentSystem", id="98", type="Notice"
	public void processNoticeUvolnenieObsluzneMiesto(MessageForm message)
	{
	}

	//meta! sender="AgentSystem", id="71", type="Request"
	public void processRequestResponseNaplnenieFront(MessageForm message)
	{
	}

	//meta! sender="ProcessObsluhaObsluzneMiestoOnlineZakaznik", id="81", type="Finish"
	public void processFinishProcessObsluhaObsluzneMiestoOnlineZakaznik(MessageForm message)
	{
	}

	//meta! sender="MonitorKoniecPrestavkaObsluzneMiesta", id="111", type="Finish"
	public void processFinishMonitorKoniecPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor konca prestavky obsluzne miesto");
		}
	}

	//meta! sender="MonitorZaciatokPrestavkaObsluzneMiesta", id="108", type="Finish"
	public void processFinishMonitorZaciatokPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor zaciatku prestavky obsluzne miesto");
		}
	}

	//meta! sender="ProcessObsluhaObsluzneMiestoObycajnyZakaznik", id="79", type="Finish"
	public void processFinishProcessObsluhaObsluzneMiestoObycajnyZakaznik(MessageForm message)
	{
	}

	//meta! sender="MonitorKoniecPrestavkaObsluzneMiesta", id="112", type="Notice"
	public void processNoticeVnutornaKoniecPrestavkaObsluzneMiesta(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- koniec prestavky obsluzne miesto");
		}

		this.myAgent().ukonciPrestavku();
	}

	//meta! sender="AgentSystem", id="169", type="Notice"
	public void processNoticeInicializaciaObsluzneMiesta(MessageForm message)
	{
		MyMessage zaciatokPrestavka = new MyMessage(this.mySim());
		zaciatokPrestavka.setAddressee(this.myAgent().findAssistant(Id.monitorZaciatokPrestavkaObsluzneMiesta));
		this.startContinualAssistant(zaciatokPrestavka);

		MyMessage koniecPrestavka = new MyMessage(this.mySim());
		koniecPrestavka.setAddressee(this.myAgent().findAssistant(Id.monitorKoniecPrestavkaObsluzneMiesta));
		this.startContinualAssistant(koniecPrestavka);
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
