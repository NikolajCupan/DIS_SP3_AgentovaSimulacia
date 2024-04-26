package org.example.Vlastne.Objekty;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import org.example.Vlastne.Ostatne.Helper;

public class Pokladna
{
    private SimQueue<MessageForm> frontPokladna;

    private boolean obsadena;

    // Statistika
    private Stat statCasFrontPokladna;
    private WStat wstatVytazeniePokladna;

    public Pokladna(Simulation simulacia)
    {
        this.frontPokladna = new SimQueue<>(new WStat(simulacia));

        this.obsadena = false;

        this.statCasFrontPokladna = new Stat();
        this.wstatVytazeniePokladna = new WStat(simulacia);
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
            throw new RuntimeException("Pokus o vlozenie zakaznika do frontu pred pokladnou, ktora nie je obsadena!");
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

    public boolean frontPrazdny()
    {
        return this.frontPokladna.isEmpty();
    }

    public boolean pokladnaDostupna()
    {
        return true;
    }

    public int getPocetFront()
    {
        return this.frontPokladna.size();
    }

    public boolean getObsadena()
    {
        return this.obsadena;
    }

    public void pridajCasFrontPokladna(double cas)
    {
        this.statCasFrontPokladna.addSample(cas);
    }

    public Stat getStatCasFrontPokladna()
    {
        return this.statCasFrontPokladna;
    }

    public WStat getWstatVytazeniePokladna()
    {
        return this.wstatVytazeniePokladna;
    }

    public WStat getWstatDlzkaFrontPokladna()
    {
        return this.frontPokladna.lengthStatistic();
    }

    public void aktualizujStatistikyPoReplikacii()
    {
        this.frontPokladna.lengthStatistic().updateAfterReplication();
        this.wstatVytazeniePokladna.updateAfterReplication();
    }
}
