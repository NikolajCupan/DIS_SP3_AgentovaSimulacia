package org.example.Vlastne;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import OSPDataStruct.SimQueue;
import OSPStat.Stat;
import OSPStat.WStat;
import org.example.simulation.MySimulation;

public class Pokladna
{
    private SimQueue<MessageForm> frontPokladna;

    private boolean obsadena;
    private boolean prestavka;

    // Statistika
    private Stat statCasFrontPokladna;
    private WStat statVytazeniePokladna;

    public Pokladna(Simulation simulacia)
    {
        this.frontPokladna = new SimQueue<>(new WStat(simulacia));

        this.obsadena = false;
        this.prestavka = false;

        this.statCasFrontPokladna = new Stat();
        this.statVytazeniePokladna = new WStat(simulacia);
    }

    public int getPocetFront()
    {
        return this.frontPokladna.size();
    }

    public boolean getObsadena()
    {
        return this.obsadena;
    }

    public boolean getPrestavka()
    {
        return this.prestavka;
    }
}
