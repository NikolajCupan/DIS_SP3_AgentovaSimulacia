package org.example.managers;

import OSPABA.*;
import org.example.Vlastne.Zakaznik;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="35"
public class ManagerSystem extends Manager
{
	public ManagerSystem(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentPokladne", id="85", type="Response"
	public void processRequestResponseObsluhaPokladna(MessageForm message)
	{
	}

	//meta! sender="AgentAutomat", id="41", type="Response"
	public void processRequestResponseObsluhaAutomat(MessageForm message)
	{
		MyMessageZakaznik sprava = (MyMessageZakaznik)message;
		Zakaznik zakaznik = sprava.getZakaznik();
		if (zakaznik.getPredcasnyOdchod())
		{
			// Zakaznik odchadza neobsluzeny
			sprava.setCode(Mc.requestResponseSpracovanieZakaznik);
			this.response(sprava);
		}
		else
		{
			message.setCode(Mc.requestResponseObsluhaObsluzneMiesto);
			message.setAddressee(Id.agentObsluzneMiesta);
			this.request(message);
		}
	}

	//meta! sender="AgentPrevzatieTovar", id="94", type="Response"
	public void processRequestResponsePrevzatieTovar(MessageForm message)
	{
	}

	//meta! sender="AgentObsluzneMiesta", id="125", type="Request"
	public void processRequestResponseOdovzdanieZamestnanec(MessageForm message)
	{
	}

	//meta! sender="AgentObsluzneMiesta", id="53", type="Response"
	public void processRequestResponseObsluhaObsluzneMiesto(MessageForm message)
	{
		// TODO: poslat do pokladne
		message.setCode(Mc.requestResponseSpracovanieZakaznik);
		this.response(message);
	}

	//meta! sender="AgentPokladne", id="126", type="Response"
	public void processRequestResponsePrijatieZamestnanec(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="165", type="Notice"
	public void processNoticeInicializaciaSystem(MessageForm message)
	{
		// Automat
		MyMessage inicializaciaAutomat = new MyMessage(this.mySim());
		inicializaciaAutomat.setCode(Mc.noticeInicializaciaAutomat);
		inicializaciaAutomat.setAddressee(Id.agentAutomat);
		this.notice(inicializaciaAutomat);

		// Obsluzne miesta
		MyMessage inicializaciaObluzneMiesta = new MyMessage(this.mySim());
		inicializaciaObluzneMiesta.setCode(Mc.noticeInicializaciaObsluzneMiesta);
		inicializaciaObluzneMiesta.setAddressee(Id.agentObsluzneMiesta);
		this.notice(inicializaciaObluzneMiesta);

		// Pokladne
		MyMessage inicializaciaPokladne = new MyMessage(this.mySim());
		inicializaciaPokladne.setCode(Mc.noticeInicializaciaPokladne);
		inicializaciaPokladne.setAddressee(Id.agentPokladne);
		this.notice(inicializaciaPokladne);
	}

	//meta! sender="AgentObsluzneMiesta", id="71", type="Response"
	public void processRequestResponseNaplnenieFront(MessageForm message)
	{
		message.setCode(Mc.requestResponseVypnutieAutomat);
		this.response(message);
	}

	//meta! sender="AgentModel", id="38", type="Request"
	public void processRequestResponseSpracovanieZakaznik(MessageForm message)
	{
		if (message.deliveryTime() > ((MySimulation)this.mySim()).getTrvanieSimulacie())
		{
			throw new RuntimeException("Ziadost o spracovanie zakaznika po vyprsani simulacneho casu!");
		}

		message.setCode(Mc.requestResponseObsluhaAutomat);
		message.setAddressee(Id.agentAutomat);
		this.request(message);
	}

	//meta! sender="AgentAutomat", id="70", type="Request"
	public void processRequestResponseVypnutieAutomat(MessageForm message)
	{
		message.setCode(Mc.requestResponseNaplnenieFront);
		message.setAddressee(Id.agentObsluzneMiesta);
		this.request(message);
	}

	//meta! sender="AgentObsluzneMiesta", id="72", type="Notice"
	public void processNoticeUvolnenieFront(MessageForm message)
	{
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
		case Mc.requestResponseSpracovanieZakaznik:
			processRequestResponseSpracovanieZakaznik(message);
		break;

		case Mc.requestResponsePrijatieZamestnanec:
			processRequestResponsePrijatieZamestnanec(message);
		break;

		case Mc.noticeInicializaciaSystem:
			processNoticeInicializaciaSystem(message);
		break;

		case Mc.requestResponseOdovzdanieZamestnanec:
			processRequestResponseOdovzdanieZamestnanec(message);
		break;

		case Mc.requestResponseObsluhaPokladna:
			processRequestResponseObsluhaPokladna(message);
		break;

		case Mc.noticeUvolnenieFront:
			processNoticeUvolnenieFront(message);
		break;

		case Mc.requestResponseVypnutieAutomat:
			processRequestResponseVypnutieAutomat(message);
		break;

		case Mc.requestResponseNaplnenieFront:
			processRequestResponseNaplnenieFront(message);
		break;

		case Mc.requestResponseObsluhaObsluzneMiesto:
			processRequestResponseObsluhaObsluzneMiesto(message);
		break;

		case Mc.requestResponseObsluhaAutomat:
			processRequestResponseObsluhaAutomat(message);
		break;

		case Mc.requestResponsePrevzatieTovar:
			processRequestResponsePrevzatieTovar(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentSystem myAgent()
	{
		return (AgentSystem)super.myAgent();
	}

}
