package org.example.continualAssistants;

import OSPABA.*;
import OSPRNG.UniformContinuousRNG;
import org.example.Vlastne.Generatory.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.Ostatne.Prezenter;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.simulation.*;
import org.example.agents.*;
import OSPABA.Process;

//meta! id="96"
public class ProcessPrevzatieTovar extends Process
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;
	private UniformContinuousRNG rngPrevzatieTovar;

	private void customProcessPrevzatieTovar()
	{
		this.rngGeneratorNasad = ((MySimulation)this.mySim()).getRngGeneratorNasad();
		this.rngPrevzatieTovar = new UniformContinuousRNG(30.0, 70.0, this.rngGeneratorNasad.generator());
	}
	// Vlastne koniec


	public ProcessPrevzatieTovar(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		// Vlastne
		this.customProcessPrevzatieTovar();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentPrevzatieTovar", id="97", type="Start"
	public void processStart(MessageForm message)
	{
		// Samotna obsluha
		double trvaniePrevzatia = this.rngPrevzatieTovar.sample();
		message.setCode(Mc.holdPrevzatieTovar);
		this.hold(trvaniePrevzatia, message);

		Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
		if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
		{
			System.out.println("(" + zakaznik.getID() + ") "
				+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- zaciatok prevzatie tovar " + zakaznik.getTypZakaznik());
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdPrevzatieTovar:
				this.assistantFinished(message);

				Zakaznik zakaznik = ((MyMessageZakaznik)message).getZakaznik();
				if (Konstanty.DEBUG_VYPISY_ZAKAZNIK)
				{
					System.out.println("(" + zakaznik.getID() + ") "
						+ Prezenter.naformatujCas(this.mySim().currentTime()) + " <- koniec prevzatie tovar " + zakaznik.getTypZakaznik());
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
	public AgentPrevzatieTovar myAgent()
	{
		return (AgentPrevzatieTovar)super.myAgent();
	}

}
