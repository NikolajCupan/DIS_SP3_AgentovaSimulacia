package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Zakaznik;

//meta! id="4"
public class ManagerOkolie extends Manager
{
	// Vlastne
	private void odosliZakaznikaModelu(MyMessageZakaznik prichod)
	{
		Zakaznik zakaznik = prichod.getZakaznik();
		this.myAgent().pridajZakaznikaDoSystemu(zakaznik);

		if (zakaznik.getCasPrichodSystem() == -1 || zakaznik.getCasPrichodSystem() > ((MySimulation)this.mySim()).getTrvanieSimulacie())
		{
			throw new RuntimeException("Chyba prichodu zmluvneho zakaznika!");
		}

		MyMessageZakaznik prichodZakaznika = new MyMessageZakaznik(this.mySim(), zakaznik);
		prichodZakaznika.setCode(Mc.requestResponsePrichodZakaznik);
		prichodZakaznika.setAddressee(Id.agentModel);
		this.request(prichodZakaznika);

		if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
		{
			System.out.println("(" + zakaznik.getID() + ") "
				+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- prichod " + zakaznik.getTypZakaznik());
		}
	}
	// Vlastne koniec


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
		this.odosliZakaznikaModelu((MyMessageZakaznik)message);
	}

	//meta! sender="SchedulerPrichodBeznyZakaznik", id="139", type="Notice"
	public void processNoticeVnutornaPrichodBeznyZakaznik(MessageForm message)
	{
		this.odosliZakaznikaModelu((MyMessageZakaznik)message);
	}

	//meta! sender="AgentModel", id="31", type="Response"
	public void processRequestResponsePrichodZakaznik(MessageForm message)
	{
		MyMessageZakaznik sprava = (MyMessageZakaznik)message;
		Zakaznik zakaznik = sprava.getZakaznik();
		zakaznik.setOdchodSystem(this.mySim().currentTime());

		this.myAgent().odoberZakaznikaZoSystemu(zakaznik);

		if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
		{
			System.out.println("(" + zakaznik.getID() + ") "
				+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- opustenie systemu " + zakaznik.getTypZakaznik());
		}
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
		this.odosliZakaznikaModelu((MyMessageZakaznik)message);
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
