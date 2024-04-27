package org.example.Vlastne.Objekty;

import OSPABA.Simulation;
import OSPStat.WStat;
import org.example.Vlastne.Ostatne.Helper;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.simulation.MySimulation;

public class ObsluzneMiesto
{
    private final TypOkno typOkno;
    private WStat wstatVytazenieObsluzneMiesto;

    private boolean obsadene;
    private boolean odlozenyTovar;
    private boolean odchodZamestnanec;

    // Referencia na simulaciu za ucelom kontroly prestavky
    private MySimulation simulacia;

    public ObsluzneMiesto(TypOkno typOkno, Simulation simulacia)
    {
        this.typOkno = typOkno;
        this.wstatVytazenieObsluzneMiesto = new WStat(simulacia);

        this.obsadene = false;
        this.odlozenyTovar = false;
        this.odchodZamestnanec = false;

        this.simulacia = (MySimulation)simulacia;
    }

    public boolean obsluzneMiestoDostupne()
    {
//        if (this.odchodZamestnanec && this.obsadene)
//        {
//            throw new RuntimeException("Existuje obsadene obsluzne miesto, pri ktorom nie je zamestnanec!");
//        }

        if (!this.obsadene && !this.odlozenyTovar && !this.odchodZamestnanec)
        {
            return true;
        }

        return false;
    }

    public boolean getOdchodZamestnanec()
    {
        return this.odchodZamestnanec;
    }

    public boolean getObsadene()
    {
        return this.obsadene;
    }

    public void odchodZamestnanec()
    {
        if (this.odchodZamestnanec)
        {
            throw new RuntimeException("Zamestnanec uz odisiel!");
        }
        if (this.simulacia.currentTime() < Konstanty.ZACIATOK_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
        {
            throw new RuntimeException("Zamestnanec nemoze odist pred zaciatkom prestavky!");
        }
        if (this.simulacia.currentTime() > Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
        {
            throw new RuntimeException("Zamestnanec nemoze odist po konci prestavky!");
        }

        this.odchodZamestnanec = true;
    }

    public void prichodZamestnanec()
    {
        if (!this.odchodZamestnanec)
        {
            throw new RuntimeException("Zamestnanec sa uz vratil!");
        }
        if (this.simulacia.currentTime() < Konstanty.KONIEC_PRESTAVKA_OD_OTVORENIA_CAS_SEKUNDY)
        {
            throw new RuntimeException("Zamestnanec sa nemoze vratit pred koncom prestavky!");
        }

        this.odchodZamestnanec = false;
    }

    public boolean zamestnanecMozeOdist()
    {
        if (this.obsadene)
        {
            return false;
        }

        return true;
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
        if (obsadene && this.odchodZamestnanec)
        {
            throw new RuntimeException("Nemozno obsadit obsluzne miesto, pri ktorom nie je zamestnanec!");
        }
        if (obsadene && this.odlozenyTovar)
        {
            throw new RuntimeException("Nemozno obsadit obsluzne miesto, ak je tam odlozeny tovar!");
        }

        this.obsadene = obsadene;
        this.aktualizaciaVytazenia();
    }

    public void setOdlozenyTovar(boolean odlozenyTovar)
    {
//        if (this.odchodZamestnanec)
//        {
//            throw new RuntimeException("Nemozno odlozit tovar ak zamestnanec odisiel!");
//        }
        if (odlozenyTovar && this.odlozenyTovar)
        {
            throw new RuntimeException("Obsluzne miesto uz obsahuje odlozeny tovar!");
        }
        if (!odlozenyTovar && !this.odlozenyTovar)
        {
            throw new RuntimeException("Obsluzne miesto neobsahuje ziadny odlozeny tovar!");
        }

        this.odlozenyTovar = odlozenyTovar;
        this.aktualizaciaVytazenia();
    }

    private void aktualizaciaVytazenia()
    {
        if (!this.obsadene && !this.odlozenyTovar)
        {
            this.wstatVytazenieObsluzneMiesto.addSample(0);
        }
        else
        {
            this.wstatVytazenieObsluzneMiesto.addSample(1);
        }
    }

    public TypOkno getTypOkna()
    {
        return this.typOkno;
    }

    public WStat getWstatVytazenieObsluzneMiesto()
    {
        return this.wstatVytazenieObsluzneMiesto;
    }

    public void aktualizujStatistikyPoReplikacii()
    {
        this.wstatVytazenieObsluzneMiesto.updateAfterReplication();
    }
}
