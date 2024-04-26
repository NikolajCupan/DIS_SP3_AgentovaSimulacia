package org.example.continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import org.example.simulation.*;
import org.example.agents.*;
import OSPABA.Process;
import org.example.Vlastne.Generatory.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Zakaznik.Zakaznik;

//meta! id="43"
public class ProcessObsluhaAutomat extends Process
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;
	private UniformContinuousRNG rngObsluhaAutomat;

	public void customProcessObsluhaAutomat()
	{
		this.rngGeneratorNasad = ((MySimulation)this.mySim()).getRngGeneratorNasad();
		this.rngObsluhaAutomat = new UniformContinuousRNG(30.0, 120.0, this.rngGeneratorNasad.generator());
	}
	// Vlastne koniec


	public ProcessObsluhaAutomat(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		// Vlastne
		this.customProcessObsluhaAutomat();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentAutomat", id="44", type="Start"
	public void processStart(MessageForm message)
	{
		Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
		zakaznik.setOdchodFrontAutomat(this.mySim().currentTime());

		AgentAutomat automat = this.myAgent();
		automat.setAutomatObsadeny(true);
		automat.pridajCasFrontAutomat(zakaznik.getCasFrontAutomat());

		// Samotna obsluha
		double trvanieObsluhy = this.rngObsluhaAutomat.sample();
		message.setCode(Mc.holdObsluhaAutomat);
		this.hold(trvanieObsluhy, message);

		if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
		{
			System.out.println("(" + zakaznik.getID() + ") "
				+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- zaciatok obsluha automat " + zakaznik.getTypZakaznik());
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdObsluhaAutomat:
				AgentAutomat automat = this.myAgent();
				automat.setAutomatObsadeny(false);
				this.assistantFinished(message);

				Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
				if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
				{
					System.out.println("(" + zakaznik.getID() + ") "
						+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- koniec obsluha automat " + zakaznik.getTypZakaznik());
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
	public AgentAutomat myAgent()
	{
		return (AgentAutomat)super.myAgent();
	}

}
