package org.example.continualAssistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
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

//meta! id="78"
public class ProcessObsluhaObsluzneMiestoObycajnyZakaznik extends Process
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;
	private UniformContinuousRNG rngObsluhaObsluzneMiesto;
	private EmpiricRNG rngZlozitostObjednavky;

	public void customProcessObsluhaObsluzneMiestoObycajnyZakaznik()
	{
		this.rngGeneratorNasad = new GeneratorNasad();
		this.rngObsluhaObsluzneMiesto = new UniformContinuousRNG(60.0, 900.0, this.rngGeneratorNasad.generator());

		EmpiricRNG jednoducha = new EmpiricRNG(this.rngGeneratorNasad.generator(),
			new EmpiricPair(new UniformContinuousRNG(240.0, 300.0, this.rngGeneratorNasad.generator()), 0.6),
			new EmpiricPair(new UniformContinuousRNG(300.0, 540.0, this.rngGeneratorNasad.generator()), 0.4)
		);
		UniformContinuousRNG mierneZlozita = new UniformContinuousRNG(540.0, 660.0, this.rngGeneratorNasad.generator());
		EmpiricRNG zlozita = new EmpiricRNG(this.rngGeneratorNasad.generator(),
			new EmpiricPair(new UniformContinuousRNG(660.0, 720.0, this.rngGeneratorNasad.generator()), 0.1),
			new EmpiricPair(new UniformContinuousRNG(720.0, 1200.0, this.rngGeneratorNasad.generator()), 0.6),
			new EmpiricPair(new UniformContinuousRNG(1200.0, 1500.0, this.rngGeneratorNasad.generator()), 0.3)
		);
		this.rngZlozitostObjednavky = new EmpiricRNG(this.rngGeneratorNasad.generator(),
			new EmpiricPair(jednoducha, 0.3),
			new EmpiricPair(mierneZlozita, 0.4),
			new EmpiricPair(zlozita, 0.3)
		);
	}
	// Vlastne koniec


	public ProcessObsluhaObsluzneMiestoObycajnyZakaznik(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		// Vlastne
		this.customProcessObsluhaObsluzneMiestoObycajnyZakaznik();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentObsluzneMiesta", id="79", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
		zakaznik.setOdchodFrontObsluzneMiesta(this.mySim().currentTime());

		ObsluzneMiesto obsluzneMiesto = zakaznik.getObsluzneMiesto();
		obsluzneMiesto.setObsadene(true);

		// Samotna obsluha
		double trvanieObsluhy =
			this.rngObsluhaObsluzneMiesto.sample() + this.rngZlozitostObjednavky.sample().doubleValue();
		message.setCode(Mc.holdObsluhaObsluzneMiestoObycajnyZakaznik);
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
			case Mc.holdObsluhaObsluzneMiestoObycajnyZakaznik:
				Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
				ObsluzneMiesto obsluzneMiesto = zakaznik.getObsluzneMiesto();

				VelkostTovaru velkostTovaru = this.myAgent().getVelkostTovaru();
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
