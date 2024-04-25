package org.example.continualAssistants;

import OSPABA.*;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.vlastne.Prezenter;

//meta! id="47"
public class MonitorVyprazdnenieFrontAutomat extends Monitor
{
	public MonitorVyprazdnenieFrontAutomat(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentAutomat", id="48", type="Start"
	public void processStart(MessageForm message)
	{
		double vyprazdnenieZa = ((MySimulation)this.mySim()).getTrvanieSimulacie();

		MyMessage vyprazdnenieFront = new MyMessage(this.mySim());
		vyprazdnenieFront.setCode(Mc.holdVyprazdnenieFrontAutomat);
		hold(vyprazdnenieZa, vyprazdnenieFront);
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdVyprazdnenieFrontAutomat:
				// Informacie o vyprsani simulacneho casu
				MyMessage vyprazdnenieFront = new MyMessage(this.mySim());
				vyprazdnenieFront.setCode(Mc.noticeVnutornaVyprsanieSimulacnyCas);
				vyprazdnenieFront.setAddressee(this.myAgent().manager());
				this.notice(vyprazdnenieFront);

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
	public AgentAutomat myAgent()
	{
		return (AgentAutomat)super.myAgent();
	}

}
