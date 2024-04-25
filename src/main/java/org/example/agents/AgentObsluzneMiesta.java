package org.example.agents;

import OSPABA.*;
import OSPDataStruct.SimQueue;
import OSPStat.WStat;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.simulation.*;
import org.example.managers.*;
import org.example.continualAssistants.*;
import org.example.instantAssistants.*;

//meta! id="51"
public class AgentObsluzneMiesta extends Agent
{
	// Vlastne
	private SimQueue<MessageForm> frontObsluzneMiesta;

	private void customAgentObsluzneMiesta()
	{
		this.addOwnMessage(Mc.holdZaciatokPrestavkaObsluzneMiesta);
		this.addOwnMessage(Mc.holdKoniecPrestavkaObsluzneMiesta);
	}

	private void customPrepareReplication()
	{
		this.frontObsluzneMiesta = new SimQueue<>(new WStat(this.mySim()));
	}

	public void pridajFront(MessageForm message)
	{
		if (this.frontObsluzneMiesta.size() == 9)
		{
			throw new RuntimeException("Nemozno pridat zakaznika do frontu pred obsluznymi miestami, pretoze front je plny!");
		}

		this.frontObsluzneMiesta.add(message);
	}

	public MessageForm odoberFront()
	{
		if (this.frontObsluzneMiesta.isEmpty())
		{
			throw new RuntimeException("Front pred obsluznymi miestami je prazdny!");
		}

		// TODO 1: check na situaciu, kedy vyberam zakaznika z frontu pred obsluznymi miestami, ale vsetky
		// TODO 1: obsluzne miesta su prave pouzivane

		// TODO 2: odoslat notice automatu, ked dojde k uvoleneniu miesta vo fronte pred obsluznymi miestami

		return this.frontObsluzneMiesta.dequeue();
	}

	public boolean frontPlny()
	{
		if (this.frontObsluzneMiesta.size() > Konstanty.KAPACITA_FRONT_OBSLUZNE_MIESTA)
		{
			throw new RuntimeException("Front pred obsluznymi miestami presiahol svoju kapacitu!");
		}

		return this.frontObsluzneMiesta.size() == Konstanty.KAPACITA_FRONT_OBSLUZNE_MIESTA;
    }

	public void zacniPrestavku()
	{
		// TODO: zaciatok presavky obsluzne miesto
	}

	public void ukonciPrestavku()
	{
		// TODO: koniec prestavky obsluzne miesto
	}
	// Vlastne koniec


	public AgentObsluzneMiesta(int id, Simulation mySim, Agent parent)
	{
		super(id, mySim, parent);
		init();

		// Vlastne
		this.customAgentObsluzneMiesta();
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
		new ManagerObsluzneMiesta(Id.managerObsluzneMiesta, mySim(), this);
		new QueryZaciatokPrestavkaObsluzneMiesta(Id.queryZaciatokPrestavkaObsluzneMiesta, mySim(), this);
		new MonitorZaciatokPrestavkaObsluzneMiesta(Id.monitorZaciatokPrestavkaObsluzneMiesta, mySim(), this);
		new ProcessObsluhaObsluzneMiestoOnlineZakaznik(Id.processObsluhaObsluzneMiestoOnlineZakaznik, mySim(), this);
		new MonitorKoniecPrestavkaObsluzneMiesta(Id.monitorKoniecPrestavkaObsluzneMiesta, mySim(), this);
		new QueryKoniecPrestavkaObsluzneMiesta(Id.queryKoniecPrestavkaObsluzneMiesta, mySim(), this);
		new ProcessObsluhaObsluzneMiestoObycajnyZakaznik(Id.processObsluhaObsluzneMiestoObycajnyZakaznik, mySim(), this);
		addOwnMessage(Mc.requestResponseOdovzdanieZamestnanec);
		addOwnMessage(Mc.noticeVnutornaZaciatokPrestavkaObsluzneMiesta);
		addOwnMessage(Mc.requestResponseObsluhaObsluzneMiesto);
		addOwnMessage(Mc.noticeUvolnenieObsluzneMiesto);
		addOwnMessage(Mc.requestResponseNaplnenieFront);
		addOwnMessage(Mc.noticeVnutornaKoniecPrestavkaObsluzneMiesta);
		addOwnMessage(Mc.noticeInicializaciaObsluzneMiesta);
	}
	//meta! tag="end"
}
