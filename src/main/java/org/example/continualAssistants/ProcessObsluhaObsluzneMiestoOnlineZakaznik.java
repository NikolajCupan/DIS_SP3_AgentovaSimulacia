package org.example.continualAssistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.TriangularRNG;
import OSPRNG.UniformContinuousRNG;
import org.example.Vlastne.ObsluzneMiesto;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.VelkostTovaru;
import org.example.Vlastne.Zakaznik;
import org.example.simulation.*;
import org.example.agents.*;
import OSPABA.Process;

//meta! id="80"
public class ProcessObsluhaObsluzneMiestoOnlineZakaznik extends Process
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;
	private TriangularRNG rngObsluhaObsluzneMiesto;

	public void customProcessObsluhaObsluzneMiestoObycajnyZakaznik()
	{
		this.rngGeneratorNasad = ((MySimulation)this.mySim()).getRngGeneratorNasad();
		this.rngObsluhaObsluzneMiesto = new TriangularRNG(60.0, 120.0, 480.0, this.rngGeneratorNasad.generator());
	}
	// Vlastne koniec


	public ProcessObsluhaObsluzneMiestoOnlineZakaznik(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication

		// Vlastne
		this.customProcessObsluhaObsluzneMiestoObycajnyZakaznik();
	}

	//meta! sender="AgentObsluzneMiesta", id="81", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
		zakaznik.setOdchodFrontObsluzneMiesta(this.mySim().currentTime());
		this.myAgent().pridajCasFrontObsluzneMiesta(zakaznik.getCasFrontObsluzneMiesta());


		ObsluzneMiesto obsluzneMiesto = zakaznik.getObsluzneMiesto();
		obsluzneMiesto.setObsadene(true);

		// Samotna obsluha
		double trvanieObsluhy =
			this.rngObsluhaObsluzneMiesto.sample();
		message.setCode(Mc.holdObsluhaObsluzneMiestoOnlineZakaznik);
		this.hold(trvanieObsluhy, message);

		if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
		{
			System.out.println("(" + zakaznik.getID() + ") "
				+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- zaciatok obsluha obsluzne miesto " + zakaznik.getTypZakaznik());
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdObsluhaObsluzneMiestoOnlineZakaznik:
				Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
				ObsluzneMiesto obsluzneMiesto = zakaznik.getObsluzneMiesto();

				VelkostTovaru velkostTovaru = this.myAgent().getVelkostTovaru();
				zakaznik.setVelkostTovaru(velkostTovaru);
				if (velkostTovaru == VelkostTovaru.MALY)
				{
					// Tovar je maly, preto dochadza k uvolneniu obsluzneho miesta
					obsluzneMiesto.setObsadene(false);
				}

				this.assistantFinished(message);

				if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
				{
					System.out.println("(" + zakaznik.getID() + ") "
						+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- koniec obsluha obsluzne miesto " + zakaznik.getTypZakaznik());
				}
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
