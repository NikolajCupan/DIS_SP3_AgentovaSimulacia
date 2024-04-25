package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;
import org.example.vlastne.Konstanty;
import org.example.vlastne.Prezenter;
import org.example.vlastne.TypZakaznik;
import org.example.vlastne.Zakaznik;

//meta! id="4"
public class ManagerOkolie extends Manager
{
	public ManagerOkolie(int id, Simulation mySim, Agent myAgent)
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

	//meta! sender="SchedulerPrichodOnlineZakaznik", id="141", type="Notice"
	public void processNoticeVnutornaPrichodOnlineZakaznik(MessageForm message)
	{
		MyMessageZakaznik prichod = (MyMessageZakaznik)message;
		Zakaznik zakaznik = prichod.getZakaznik();

		if (zakaznik.getTypZakaznik() != TypZakaznik.ONLINE || zakaznik.getPrichodSystem() == -1
			|| zakaznik.getPrichodSystem() > ((MySimulation)this.mySim()).getTrvanieSimulacie())
		{
			throw new RuntimeException("Chyba prichodu online zakaznika!");
		}

		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(prichod.getZakaznik().getPrichodSystem()) + " <- prichod online");
		}
	}

	//meta! sender="SchedulerPrichodBeznyZakaznik", id="139", type="Notice"
	public void processNoticeVnutornaPrichodBeznyZakaznik(MessageForm message)
	{
		MyMessageZakaznik prichod = (MyMessageZakaznik)message;
		Zakaznik zakaznik = prichod.getZakaznik();

		if (zakaznik.getTypZakaznik() != TypZakaznik.BEZNY || zakaznik.getPrichodSystem() == -1
			|| zakaznik.getPrichodSystem() > ((MySimulation)this.mySim()).getTrvanieSimulacie())
		{
			throw new RuntimeException("Chyba prichodu bezneho zakaznika!");
		}

		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(prichod.getZakaznik().getPrichodSystem()) + " <- prichod bezny");
		}
	}

	//meta! sender="AgentModel", id="31", type="Response"
	public void processRequestResponsePrichodZakaznik(MessageForm message)
	{
	}

	//meta! sender="SchedulerPrichodOnlineZakaznik", id="137", type="Finish"
	public void processFinishSchedulerPrichodOnlineZakaznik(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			MyMessage ukoncenie = (MyMessage)message;
			System.out.println(Prezenter.naformatujCas(ukoncenie.deliveryTime()) + " <- planovac online zakaznik ukoncil cinnost");
		}
	}

	//meta! sender="SchedulerPrichodZmluvnyZakaznik", id="135", type="Finish"
	public void processFinishSchedulerPrichodZmluvnyZakaznik(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			MyMessage ukoncenie = (MyMessage)message;
			System.out.println(Prezenter.naformatujCas(ukoncenie.deliveryTime()) + " <- planovac zmluvny zakaznik ukoncil cinnost");
		}
	}

	//meta! sender="SchedulerPrichodBeznyZakaznik", id="133", type="Finish"
	public void processFinishSchedulerPrichodBeznyZakaznik(MessageForm message)
	{
		if (Konstanty.DEBUG_VYPISY)
		{
			MyMessage ukoncenie = (MyMessage)message;
			System.out.println(Prezenter.naformatujCas(ukoncenie.deliveryTime()) + " <- planovac bezny zakaznik ukoncil cinnost");
		}
	}

	//meta! sender="SchedulerPrichodZmluvnyZakaznik", id="140", type="Notice"
	public void processNoticeVnutornaPrichodZmluvnyZakaznik(MessageForm message)
	{
		MyMessageZakaznik prichod = (MyMessageZakaznik)message;
		Zakaznik zakaznik = prichod.getZakaznik();

		if (zakaznik.getTypZakaznik() != TypZakaznik.ZMLUVNY || zakaznik.getPrichodSystem() == -1
			|| zakaznik.getPrichodSystem() > ((MySimulation)this.mySim()).getTrvanieSimulacie())
		{
			throw new RuntimeException("Chyba prichodu zmluvneho zakaznika!");
		}

		if (Konstanty.DEBUG_VYPISY)
		{
			System.out.println(Prezenter.naformatujCas(prichod.getZakaznik().getPrichodSystem()) + " <- prichod zmluvny");
		}
	}

	//meta! sender="AgentModel", id="32", type="Notice"
	public void processNoticeInicializaciaOkolie(MessageForm message)
	{
		// Spustenie planovacov
		MyMessage spustenieBezniZakaznici = new MyMessage(this.mySim());
		spustenieBezniZakaznici.setAddressee(this.myAgent().findAssistant(Id.schedulerPrichodBeznyZakaznik));
		this.startContinualAssistant(spustenieBezniZakaznici);

		MyMessage spustenieZmluvniZakaznici = new MyMessage(this.mySim());
		spustenieZmluvniZakaznici.setAddressee(this.myAgent().findAssistant(Id.schedulerPrichodZmluvnyZakaznik));
		this.startContinualAssistant(spustenieZmluvniZakaznici);

		MyMessage spustenieOnlineZakaznici = new MyMessage(this.mySim());
		spustenieOnlineZakaznici.setAddressee(this.myAgent().findAssistant(Id.schedulerPrichodOnlineZakaznik));
		this.startContinualAssistant(spustenieOnlineZakaznici);
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
			case Id.schedulerPrichodOnlineZakaznik:
				processFinishSchedulerPrichodOnlineZakaznik(message);
			break;

			case Id.schedulerPrichodZmluvnyZakaznik:
				processFinishSchedulerPrichodZmluvnyZakaznik(message);
			break;

			case Id.schedulerPrichodBeznyZakaznik:
				processFinishSchedulerPrichodBeznyZakaznik(message);
			break;
			}
		break;

		case Mc.noticeVnutornaPrichodOnlineZakaznik:
			processNoticeVnutornaPrichodOnlineZakaznik(message);
		break;

		case Mc.noticeVnutornaPrichodZmluvnyZakaznik:
			processNoticeVnutornaPrichodZmluvnyZakaznik(message);
		break;

		case Mc.requestResponsePrichodZakaznik:
			processRequestResponsePrichodZakaznik(message);
		break;

		case Mc.noticeVnutornaPrichodBeznyZakaznik:
			processNoticeVnutornaPrichodBeznyZakaznik(message);
		break;

		case Mc.noticeInicializaciaOkolie:
			processNoticeInicializaciaOkolie(message);
		break;

		default:
			processDefault(message);
		break;
		}
	}
	//meta! tag="end"

	@Override
	public AgentOkolie myAgent()
	{
		return (AgentOkolie)super.myAgent();
	}

}
