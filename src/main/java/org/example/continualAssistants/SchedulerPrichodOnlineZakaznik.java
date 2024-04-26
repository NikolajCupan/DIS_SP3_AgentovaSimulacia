package org.example.continualAssistants;

import OSPABA.*;
import OSPRNG.ExponentialRNG;
import org.example.simulation.*;
import org.example.agents.*;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.Vlastne.TypZakaznik;
import org.example.Vlastne.Zakaznik;

//meta! id="136"
public class SchedulerPrichodOnlineZakaznik extends Scheduler
{
	// Vlastne
	private GeneratorNasad rngGeneratorNasad;
	private ExponentialRNG rngPrichodOnlineZakaznik;

	public void customSchedulerPrichodOnlineZakaznik()
	{
		this.rngGeneratorNasad = ((MySimulation)this.mySim()).getRngGeneratorNasad();

		double priemer = ((MySimulation)this.mySim()).getZvysenyTokZakaznikov()
			? Konstanty.ZVYSENY_POCET_ONLINE_ZAKAZNIKOV_ZA_MINUTU : Konstanty.POCET_ONLINE_ZAKAZNIKOV_ZA_MINUTU;
		this.rngPrichodOnlineZakaznik = new ExponentialRNG(priemer, this.rngGeneratorNasad.generator());
	}

	private boolean prichodPredZatvorenim(double trvaniePrichodu)
	{
		if (this.mySim().currentTime() + trvaniePrichodu > ((MySimulation)this.mySim()).getTrvanieSimulacie())
		{
			return false;
		}

		return true;
	}

	private void ukonciCinnost()
	{
		MyMessage ukoncenie = new MyMessage(this.mySim());
		ukoncenie.setCode(Mc.finish);
		ukoncenie.setAddressee(this.myAgent().manager());
		this.notice(ukoncenie);
	}
	// Vlastne koniec

	public SchedulerPrichodOnlineZakaznik(int id, Simulation mySim, CommonAgent myAgent)
	{
		super(id, mySim, myAgent);

		// Vlastne
		this.customSchedulerPrichodOnlineZakaznik();
	}

	@Override
	public void prepareReplication()
	{
		super.prepareReplication();
		// Setup component for the next replication
	}

	//meta! sender="AgentOkolie", id="137", type="Start"
	public void processStart(MessageForm message)
	{
		double trvaniePrichodu = this.rngPrichodOnlineZakaznik.sample();
		if (this.prichodPredZatvorenim(trvaniePrichodu))
		{
			// Naplanovanie iba za predpokladu, ze by prichod nenastal po zatvoreni
			MyMessageZakaznik prichod = new MyMessageZakaznik(this.mySim(), TypZakaznik.ONLINE);
			prichod.setCode(Mc.holdPrichodOnlineZakaznik);
			this.hold(trvaniePrichodu, prichod);
		}
		else
		{
			this.ukonciCinnost();
		}
	}

	//meta! userInfo="Process messages defined in code", id="0"
	public void processDefault(MessageForm message)
	{
		switch (message.code())
		{
			case Mc.holdPrichodOnlineZakaznik:
				// Spracovanie prichodu
				MyMessageZakaznik prichod = (MyMessageZakaznik)message;
				Zakaznik zakaznik = prichod.getZakaznik();
				zakaznik.setPrichodSystem(this.mySim().currentTime());

				// Oznamenie prichodu svojmu manazerovi
				prichod.setCode(Mc.noticeVnutornaPrichodOnlineZakaznik);
				prichod.setAddressee(this.myAgent().manager());
				this.notice(message);


				// Dalsi prichod
				double trvaniePrichodu = this.rngPrichodOnlineZakaznik.sample();
				if (this.prichodPredZatvorenim(trvaniePrichodu))
				{
					MyMessageZakaznik dalsiPrichod = new MyMessageZakaznik(this.mySim(), TypZakaznik.ONLINE);
					dalsiPrichod.setCode(Mc.holdPrichodOnlineZakaznik);
					this.hold(trvaniePrichodu, dalsiPrichod);
				}
				else
				{
					this.ukonciCinnost();
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
	public AgentOkolie myAgent()
	{
		return (AgentOkolie)super.myAgent();
	}

}
