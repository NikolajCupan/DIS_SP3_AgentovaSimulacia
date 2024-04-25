package org.example.managers;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

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
	}

	//meta! sender="MonitorZaciatokPrestavkaPokladne", id="103", type="Notice"
	public void processNoticeVnutornaZaciatokPrestavkaPokladne(MessageForm message)
	{
	}

	//meta! sender="AgentSystem", id="85", type="Request"
	public void processRequestResponseObsluhaPokladna(MessageForm message)
	{
	}

	//meta! sender="AgentSystem", id="126", type="Request"
	public void processRequestResponsePrijatieZamestnanec(MessageForm message)
	{
	}

	//meta! sender="AgentSystem", id="172", type="Notice"
	public void processNoticeInicializaciaPokladne(MessageForm message)
	{
	}

	//meta! sender="ProcessObsluhaPokladna", id="88", type="Finish"
	public void processFinishProcessObsluhaPokladna(MessageForm message)
	{
	}

	//meta! sender="MonitorZaciatokPrestavkaPokladne", id="102", type="Finish"
	public void processFinishMonitorZaciatokPrestavkaPokladne(MessageForm message)
	{
	}

	//meta! sender="MonitorKoniecPrestavkaPokladne", id="105", type="Finish"
	public void processFinishMonitorKoniecPrestavkaPokladne(MessageForm message)
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
