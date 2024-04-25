package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

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
	}

	//meta! sender="SchedulerPrichodBeznyZakaznik", id="139", type="Notice"
	public void processNoticeVnutornaPrichodBeznyZakaznik(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="31", type="Response"
	public void processRequestResponsePrichodZakaznik(MessageForm message)
	{
	}

	//meta! sender="SchedulerPrichodOnlineZakaznik", id="137", type="Finish"
	public void processFinishSchedulerPrichodOnlineZakaznik(MessageForm message)
	{
	}

	//meta! sender="SchedulerPrichodZmluvnyZakaznik", id="135", type="Finish"
	public void processFinishSchedulerPrichodZmluvnyZakaznik(MessageForm message)
	{
	}

	//meta! sender="SchedulerPrichodBeznyZakaznik", id="133", type="Finish"
	public void processFinishSchedulerPrichodBeznyZakaznik(MessageForm message)
	{
	}

	//meta! sender="SchedulerPrichodZmluvnyZakaznik", id="140", type="Notice"
	public void processNoticeVnutornaPrichodZmluvnyZakaznik(MessageForm message)
	{
	}

	//meta! sender="AgentModel", id="32", type="Notice"
	public void processNoticeInicializaciaOkolie(MessageForm message)
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
