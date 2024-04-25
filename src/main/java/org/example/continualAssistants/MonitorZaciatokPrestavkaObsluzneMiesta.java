package org.example.continualAssistants;

import OSPABA.*;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="107"
public class MonitorZaciatokPrestavkaObsluzneMiesta extends Monitor
{
	public MonitorZaciatokPrestavkaObsluzneMiesta(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluzneMiesta", id="108", type="Start"
	public void processStart(MessageForm message)
	{
		double zaciatokPrestavkyZa = Konstanty.ZACIATOK_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY;

		MyMessage zaciatokPrestavky = new MyMessage(this.mySim());
		zaciatokPrestavky.setCode(Mc.holdZaciatokPrestavkaObsluzneMiesta);
		this.hold(zaciatokPrestavkyZa, zaciatokPrestavky);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdZaciatokPrestavkaObsluzneMiesta:
				// Informacia o zaciatku prestavky
				MyMessage zaciatokPrestavka = new MyMessage(this.mySim());
				zaciatokPrestavka.setCode(Mc.noticeVnutornaZaciatokPrestavkaObsluzneMiesta);
				zaciatokPrestavka.setAddressee(this.myAgent().manager());
				this.notice(zaciatokPrestavka);

				// Informacia o ukonceni cinnosti
				MyMessage ukoncenieCinnosti = new MyMessage(this.mySim());
				this.assistantFinished(ukoncenieCinnosti);
				break;
			default:
				throw new RuntimeException("Neznamy kod spravy!");
		}
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	@Override
	public void processMessage(MessageForm message)
	{
		switch (message.code())
		{
		case Mc.start:
			processStart(message);
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
