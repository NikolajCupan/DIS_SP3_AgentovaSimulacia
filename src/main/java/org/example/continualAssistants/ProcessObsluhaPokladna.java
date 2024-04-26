package org.example.continualAssistants;

import OSPABA.*;
import OSPRNG.EmpiricPair;
import OSPRNG.EmpiricRNG;
import OSPRNG.UniformDiscreteRNG;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Pokladna;
import org.example.Vlastne.Zakaznik;
import org.example.simulation.*;
import org.example.agents.*;
import OSPABA.Process;

//meta! id="87"
public class ProcessObsluhaPokladna extends Process
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;
	private EmpiricRNG rngDlzkaPlatenia;

	public void customProcessObsluhaPokladna()
	{
		this.rngGeneratorNasad = new GeneratorNasad();
		this.rngDlzkaPlatenia = new EmpiricRNG(this.rngGeneratorNasad.generator(),
			new EmpiricPair(new UniformDiscreteRNG(180, 480, this.rngGeneratorNasad.generator()), 0.4),
			new EmpiricPair(new UniformDiscreteRNG(180, 360, this.rngGeneratorNasad.generator()), 0.6)
		);
	}
	// Vlastne koniec


	public ProcessObsluhaPokladna(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		// Vlastne
		this.customProcessObsluhaPokladna();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPokladne", id="88", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
		zakaznik.setOdchodFrontPokladne(this.mySim().currentTime());

		Pokladna pokladna = zakaznik.getPokladna();
		pokladna.setPokladnaObsadena(true);

		// Samotna obsluha
		double trvanieObsluhy = this.rngDlzkaPlatenia.sample().doubleValue();
		message.setCode(Mc.holdObsluhaPokladna);
		this.hold(trvanieObsluhy, message);

		if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
		{
			System.out.println("(" + zakaznik.getID() + ") "
				+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- zaciatok obsluha pokladna " + zakaznik.getTypZakaznik());
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdObsluhaPokladna:
				Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
				Pokladna pokladna = zakaznik.getPokladna();
				pokladna.setPokladnaObsadena(false);
				this.assistantFinished(message);

				if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
				{
					System.out.println("(" + zakaznik.getID() + ") "
						+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- koniec obsluha pokladna " + zakaznik.getTypZakaznik());
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
	public AgentPokladne myAgent()
	{
		return (AgentPokladne)super.myAgent();
	}

}
