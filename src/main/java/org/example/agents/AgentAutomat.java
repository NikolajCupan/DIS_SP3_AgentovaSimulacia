package org.example.agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import org.example.Vlastne.Zakaznik;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.Vlastne.Ostatne.Helper;

//meta! id="39"
public class AgentAutomat extends Agent
{
	// Vlastne
	private SimQueue<MessageForm> frontAutomat;
	private boolean automatObsadeny;
	private boolean automatVypnuty;

	// Statistiky
	private Stat statCasFrontAutomat;
	private WStat wstatVytazenieAutomat;

	private void customAgentAutomat()
	{
		this.addOwnMessage(Mc.holdVyprazdnenieFrontAutomat);
		this.addOwnMessage(Mc.holdObsluhaAutomat);
	}

	private void customPrepareReplication()
	{
		this.frontAutomat = new SimQueue<>(new WStat(this.mySim()));
		this.automatObsadeny = false;
		this.automatVypnuty = false;

		// Statistiky
		this.statCasFrontAutomat = new Stat();
		this.wstatVytazenieAutomat = new WStat(this.mySim());
	}

	public void pridajFront(MessageForm message)
	{
		this.frontAutomat.enqueue(message);
	}

	public MessageForm odoberFront()
	{
		if (this.frontAutomat.isEmpty())
		{
			throw new RuntimeException("Front pred automatom je prazdny!");
		}
		else if (this.automatVypnuty || this.automatObsadeny)
		{
			throw new RuntimeException("Pokus o vybratie zakaznika z frontu, ked je automat vypnuty alebo obsadeny!");
		}

		return this.frontAutomat.dequeue();
	}

	public boolean frontPrazdny()
	{
		return this.frontAutomat.isEmpty();
	}

	public void vyprazdniFront()
	{
		int velkostFront = this.frontAutomat.size();
		for (int i = 0; i < velkostFront; i++)
		{
			MyMessageZakaznik sprava = (MyMessageZakaznik)this.frontAutomat.dequeue();
			Zakaznik zakaznik = sprava.getZakaznik();
			zakaznik.predcasnyOdchod();

			sprava.setAddressee(Id.agentSystem);
			this.manager().response(sprava);
		}
	}

	public boolean getAutomatObsadeny()
	{
		return this.automatObsadeny;
	}

	public boolean getAutomatVypnuty()
	{
		return this.automatVypnuty;
	}

	public void setAutomatObsadeny(boolean automatObsadeny)
	{
		if (automatObsadeny && this.automatObsadeny)
		{
			throw new RuntimeException("Automat je uz obsadeny!");
		}
		else if (!automatObsadeny && !this.automatObsadeny)
		{
			throw new RuntimeException("Automat je uz volny!");
		}
		else if (this.automatVypnuty)
		{
			throw new RuntimeException("Nemozno obsadit/uvolnit automat, ktory je vypnuty!");
		}

		this.automatObsadeny = automatObsadeny;
		this.wstatVytazenieAutomat.addSample(Helper.booleanNaDouble(this.automatObsadeny));
	}

	public void setAutomatVypnuty(boolean automatVypnuty)
	{
		if (automatVypnuty && this.automatVypnuty)
		{
			throw new RuntimeException("Automat je uz vypnuty!");
		}
		else if (!automatVypnuty && !this.automatVypnuty)
		{
			throw new RuntimeException("Automat je uz vypnuty!");
		}
		else if (this.automatObsadeny)
		{
			throw new RuntimeException("Nemozno vypnut automat, ktory je obsadeny!");
		}

		this.automatVypnuty = automatVypnuty;
	}

	public void pridajCasFrontAutomat(double cas)
	{
		this.statCasFrontAutomat.addSample(cas);
	}
	// Vlastne koniec


	public AgentAutomat(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentAutomat();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		// Vlastne
		this.customPrepareReplication();
	}

	//meta! userInfo="Generated code: do not modify", tag="begin"
	private void init()
	{
		new ManagerAutomat(Id.managerAutomat, mySim(), this);
		new ProcessObsluhaAutomat(Id.processObsluhaAutomat, mySim(), this);
		new MonitorVyprazdnenieFrontAutomat(Id.monitorVyprazdnenieFrontAutomat, mySim(), this);
		addOwnMessage(Mc.noticeInicializaciaAutomat);
		addOwnMessage(Mc.requestResponseObsluhaAutomat);
		addOwnMessage(Mc.noticeZapnutieAutomat);
		addOwnMessage(Mc.noticeVnutornaVyprsanieSimulacnyCas);
		addOwnMessage(Mc.requestResponseVypnutieAutomat);
	}
	//meta! tag="end"
}
