package org.example.Vlastne.Objekty;

import OSPABA.Simulation;
import OSPStat.WStat;
import org.example.Vlastne.Ostatne.Helper;

public class ObsluzneMiesto
{
    private final TypOkno typOkno;
    private WStat wstatVytazenieObsluzneMiesto;

    private boolean obsadene;

    public ObsluzneMiesto(TypOkno typOkno, Simulation simulacia)
    {
        this.typOkno = typOkno;
        this.wstatVytazenieObsluzneMiesto = new WStat(simulacia);

        this.obsadene = false;
    }

    public boolean obsluzneMiestoDostupne()
    {
        if (!this.obsadene)
        {
            return true;
        }

        return false;
    }

    public void setObsadene(boolean obsadene)
    {
        if (obsadene && this.obsadene)
        {
            throw new RuntimeException("Dane obsluzne miesto je uz obsadene!");
        }
        if (!obsadene && !this.obsadene)
        {
            throw new RuntimeException("Dane obsluzne miesto je uz uvolnene!");
        }

        this.obsadene = obsadene;
        this.wstatVytazenieObsluzneMiesto.addSample(Helper.booleanNaDouble(this.obsadene));
    }

    public TypOkno getTypOkna()
    {
        return this.typOkno;
    }

    public WStat getWstatVytazenieObsluzneMiesto()
    {
        return this.wstatVytazenieObsluzneMiesto;
    }
}
