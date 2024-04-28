package org.example.Vlastne.Objekty;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import org.example.Vlastne.Ostatne.Helper;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.simulation.MySimulation;

public class Pokladna
{
    private SimQueue<MessageForm> frontPokladna;

    private boolean obsadena;
    private boolean prestavka;
    private boolean nahrada;

    // Statistika
    private Stat statCasFrontPokladna;
    private WStat wstatVytazeniePokladna;

    // Referencia na simulaciu za ucelom kontroly prestavky
    private MySimulation simulacia;

    public Pokladna(Simulation simulacia)
    {
        this.frontPokladna = new SimQueue<>(new WStat(simulacia));

        this.obsadena = false;
        this.prestavka = false;
        this.nahrada = false;

        this.statCasFrontPokladna = new Stat();
        this.wstatVytazeniePokladna = new WStat(simulacia);

        this.simulacia = (MySimulation)simulacia;
    }

    public MessageForm vyberFront()
    {
        if (this.frontPokladna.isEmpty())
        {
            throw new RuntimeException("Front pred pokladnou je prazdny, nemozeno vybrat zakaznika!");
        }

        return this.frontPokladna.dequeue();
    }

    public void vlozFront(MessageForm sprava)
    {
        if (!this.obsadena)
        {
            if (this.prestavka && !this.nahrada)
            {
                // Situacia je v poriadku
            }
            else
            {
                throw new RuntimeException("Pokus o vlozenie zakaznika do frontu pred pokladnou, ktora nie je obsadena!");
            }
        }
        if (this.frontPokladna.contains(sprava))
        {
            throw new RuntimeException("Front pred pokladnou uz obsahuje daneho zakaznika!");
        }

        this.frontPokladna.add(sprava);
    }

    public void setPokladnaObsadena(boolean obsadena)
    {
        if (obsadena && this.obsadena)
        {
            throw new RuntimeException("Pokladne je uz obsadena!");
        }
        if (!obsadena && !this.obsadena)
        {
            throw new RuntimeException("Pokladna je uz volna!");
        }

        this.obsadena = obsadena;
        this.wstatVytazeniePokladna.addSample(Helper.booleanNaDouble(this.obsadena));
    }

    public void setNahrada(boolean nahrada)
    {
        if (nahrada && this.nahrada)
        {
            throw new RuntimeException("Pokladna uz ma nahradu");
        }
        if (!nahrada && !this.nahrada)
        {
            throw new RuntimeException("Nahrada od danej pokladne uz odisla!");
        }
        if (nahrada && !this.prestavka)
        {
            throw new RuntimeException("Nemozno prijat nahradu po skonceni prestavky!");
        }
        if (!nahrada && this.prestavka)
        {
            throw new RuntimeException("Nemozno zrusit nahradu pocas trvania prestavky!");
        }

        this.nahrada = nahrada;
    }

    public boolean frontPrazdny()
    {
        return this.frontPokladna.isEmpty();
    }

    public void presunSvojFront(Pokladna presunDo)
    {
        int pocetZakaznikovVoFronte = this.frontPokladna.size();
        for (int i = 0; i < pocetZakaznikovVoFronte; i++)
        {
            presunDo.vlozFront(this.frontPokladna.dequeue());
        }
    }

    public boolean pokladnaDostupna()
    {
        if (this.prestavka && this.nahrada)
        {
            return true;
        }
        else if (this.prestavka && !this.nahrada)
        {
            return false;
        }

        return true;
    }

    public synchronized int getPocetFront()
    {
        return this.frontPokladna.size();
    }

    public synchronized boolean getObsadena()
    {
        return this.obsadena;
    }

    public synchronized boolean getNahrada()
    {
        return this.nahrada;
    }

    public synchronized boolean getPrestavka()
    {
        return this.prestavka;
    }

    public void zaciatokPrestavkaPokladna()
    {
        if (this.prestavka)
        {
            throw new RuntimeException("Prestavka pri pokladni uz prebieha!");
        }
        if (this.simulacia.currentTime() < Konstanty.ZACIATOK_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
        {
            throw new RuntimeException("Zaciatok prestavky nastal prilis skoro!");
        }

        this.prestavka = true;
    }

    public void koniecPrestavkaPokladna()
    {
        if (!this.prestavka)
        {
            throw new RuntimeException("Prestavka pri pokladni uz skoncila!");
        }
        if (this.simulacia.currentTime() < Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
        {
            throw new RuntimeException("Koniec prestavky nastal prilis skoro!");
        }

        this.prestavka = false;
    }

    public void pridajCasFrontPokladna(double cas)
    {
        this.statCasFrontPokladna.addSample(cas);
    }

    public synchronized Stat getStatCasFrontPokladna()
    {
        return this.statCasFrontPokladna;
    }

    public synchronized WStat getWstatVytazeniePokladna()
    {
        return this.wstatVytazeniePokladna;
    }

    public synchronized WStat getWstatDlzkaFrontPokladna()
    {
        return this.frontPokladna.lengthStatistic();
    }

    public void aktualizujStatistikyPoReplikacii()
    {
        this.frontPokladna.lengthStatistic().updateAfterReplication();
        this.wstatVytazeniePokladna.updateAfterReplication();
    }
}
