package org.example.continualAssistants;

import OSPABA.*;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.simulation.*;
import org.example.agents.*;

//meta! id="110"
public class MonitorKoniecPrestavkaObsluzneMiesta extends Monitor
{
	public MonitorKoniecPrestavkaObsluzneMiesta(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluzneMiesta", id="111", type="Start"
	public void processStart(MessageForm message)
	{
		double koniecPrestavkyZa = Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY;

		MyMessage koniecPrestavky = new MyMessage(this.mySim());
		koniecPrestavky.setCode(Mc.holdKoniecPrestavkaObsluzneMiesta);
		this.hold(koniecPrestavkyZa, koniecPrestavky);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdKoniecPrestavkaObsluzneMiesta:
				// Informacia o konci prestavky
				MyMessage koniecPrestavka = new MyMessage(this.mySim());
				koniecPrestavka.setCode(Mc.noticeVnutornaKoniecPrestavkaObsluzneMiesta);
				koniecPrestavka.setAddressee(this.myAgent().manager());
				this.notice(koniecPrestavka);

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
