package org.example.Vlastne;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;

public class Pokladna
{
    private SimQueue<MessageForm> frontPokladna;

    private boolean obsadena;

    // Statistika
    private Stat statCasFrontPokladna;
    private WStat statVytazeniePokladna;

    public Pokladna(Simulation simulacia)
    {
        this.frontPokladna = new SimQueue<>(new WStat(simulacia));

        this.obsadena = false;

        this.statCasFrontPokladna = new Stat();
        this.statVytazeniePokladna = new WStat(simulacia);
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
}
