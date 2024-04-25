package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Zakaznik;

//meta! id="39"
public class ManagerAutomat extends Manager
{
	public ManagerAutomat(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="AgentSystem", id="167", type="Notice"
	public void processNoticeInicializaciaAutomat(MessageForm message)
	{
		MyMessage vyprazdnenieAutomat = new MyMessage(this.mySim());
		vyprazdnenieAutomat.setAddressee(this.myAgent().findAssistant(Id.monitorVyprazdnenieFrontAutomat));
		this.startContinualAssistant(vyprazdnenieAutomat);
	}

	//meta! sender="AgentSystem", id="41", type="Request"
	public void processRequestResponseObsluhaAutomat(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
		zakaznik.setPrichodFrontAutomat(this.mySim().currentTime());

		AgentAutomat automat = this.myAgent();

		if (automat.getAutomatVypnuty() || automat.getAutomatObsadeny() || !automat.frontPrazdny())
		{
			// Automat nemozno pouzit
			automat.pridajFront(message);
		}
		else
		{
			// Automat mozno pouzit
			message.setAddressee(this.myAgent().findAssistant(Id.processObsluhaAutomat));
			this.startContinualAssistant(message);
		}
	}

	//meta! sender="AgentSystem", id="73", type="Notice"
	public void processNoticeZapnutieAutomat(MessageForm message)
	{
	}

	//meta! sender="MonitorVyprazdnenieFrontAutomat", id="48", type="Finish"
	public void processFinishMonitorVyprazdnenieFrontAutomat(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- oznamenie o ukonceni cinnosti monitor automat");
		}
	}

	//meta! sender="ProcessObsluhaAutomat", id="44", type="Finish"
	public void processFinishProcessObsluhaAutomat(MessageForm message)
	{
		// Spracuj zakaznika, ktoreho obsluha skoncila
		message.setCode(Mc.requestResponseObsluhaAutomat);
		this.response(message);
	}

	//meta! sender="MonitorVyprazdnenieFrontAutomat", id="63", type="Notice"
	public void processNoticeVnutornaVyprsanieSimulacnyCas(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(message.deliveryTime()) + " <- vyprazdnenie front automat");
		}

		this.myAgent().vyprazdniFront();
	}

	//meta! sender="AgentSystem", id="70", type="Response"
	public void processRequestResponseVypnutieAutomat(MessageForm message)
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
		case Mc.requestResponseVypnutieAutomat:
			processRequestResponseVypnutieAutomat(message);
		break;

		case Mc.noticeInicializaciaAutomat:
			processNoticeInicializaciaAutomat(message);
		break;

		case Mc.noticeVnutornaVyprsanieSimulacnyCas:
			processNoticeVnutornaVyprsanieSimulacnyCas(message);
		break;

		case Mc.noticeZapnutieAutomat:
			processNoticeZapnutieAutomat(message);
		break;

		case Mc.finish:
			switch (message.sender().id())
			{
			case Id.monitorVyprazdnenieFrontAutomat:
				processFinishMonitorVyprazdnenieFrontAutomat(message);
			break;

			case Id.processObsluhaAutomat:
				processFinishProcessObsluhaAutomat(message);
			break;
			}
		break;

		case Mc.requestResponseObsluhaAutomat:
			processRequestResponseObsluhaAutomat(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentAutomat myAgent()
	{
		return (AgentAutomat)super.myAgent();
	}

}
