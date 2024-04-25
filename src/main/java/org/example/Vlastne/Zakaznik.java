package org.example.Vlastne;

import org.example.Vlastne.Ostatne.Identifikator;

public class Zakaznik
{
    private final long ID;
    private final TypZakaznik typZakaznik;
    private boolean predcasnyOdchod;

    private double prichodSystem;
    private double odchodSystem;

    private double prichodFrontAutomat;
    private double odchodFrontAutomat;

    private ObsluzneMiesto obsluzneMiesto;
    private double prichodFrontObsluzneMiesta;
    private double odchodFrontObsluzneMiesta;

    public Zakaznik(TypZakaznik typZakaznik)
    {
        this.ID = Identifikator.getID();
        this.typZakaznik = typZakaznik;
        this.predcasnyOdchod = false;

        this.prichodSystem = -1;
        this.odchodSystem = -1;

        this.prichodFrontAutomat = -1;
        this.odchodFrontAutomat = -1;

        this.obsluzneMiesto = null;
        this.prichodFrontObsluzneMiesta = -1;
        this.odchodFrontObsluzneMiesta = -1;
    }

    public void predcasnyOdchod()
    {
        if (this.predcasnyOdchod)
        {
            throw new RuntimeException("Predcasny odchod je uz nastaveny!");
        }

        this.predcasnyOdchod = true;
    }

    public void setPrichodSystem(double prichodSystem)
    {
        this.prichodSystem = prichodSystem;
    }

    public void setOdchodSystem(double odchodSystem)
    {
        this.odchodSystem = odchodSystem;
    }

    public void setPrichodFrontAutomat(double prichodFrontAutomat)
    {
        this.prichodFrontAutomat = prichodFrontAutomat;
    }

    public void setOdchodFrontAutomat(double odchodFrontAutomat)
    {
        this.odchodFrontAutomat = odchodFrontAutomat;
    }

    public void setPrichodFrontObsluzneMiesta(double prichodFrontObsluzneMiesta)
    {
        this.prichodFrontObsluzneMiesta = prichodFrontObsluzneMiesta;
    }

    public void setOdchodFrontObsluzneMiesta(double odchodFrontObsluzneMiesta)
    {
        this.odchodFrontObsluzneMiesta = odchodFrontObsluzneMiesta;
    }

    public void vynulujObsluzneMiesto()
    {
        if (this.obsluzneMiesto == null)
        {
            throw new RuntimeException("Obsluzne miesto nie je nastavene!");
        }

        this.obsluzneMiesto = null;
    }

    public void setObsluzneMiesto(ObsluzneMiesto obsluzneMiesto)
    {
        TypOkno typOkno = obsluzneMiesto.getTypOkna();
        if (typOkno == TypOkno.ONLINE && this.typZakaznik != TypZakaznik.ONLINE)
        {
            throw new RuntimeException("Nespravny typ zakaznika!");
        }
        else if (typOkno == TypOkno.OBYCAJNE && this.typZakaznik == TypZakaznik.ONLINE)
        {
            throw new RuntimeException("Nespravny typ zakaznika!");
        }
        if (this.obsluzneMiesto != null)
        {
            throw new RuntimeException("Zakaznik uz ma nastavene obsluzne miesto!");
        }
        if (!obsluzneMiesto.obsluzneMiestoDostupne())
        {
            throw new RuntimeException("Zakaznik nemoze byt priradeny k danemu obsluznemu miestu, pretoze nie je dostupne!");
        }

        this.obsluzneMiesto = obsluzneMiesto;
    }

    public ObsluzneMiesto getObsluzneMiesto()
    {
        if (this.obsluzneMiesto == null)
        {
            throw new RuntimeException("Obsluzne miesto nie je nastavene!");
        }

        return this.obsluzneMiesto;
    }

    public boolean getPredcasnyOdchod()
    {
        return this.predcasnyOdchod;
    }

    public long getID()
    {
        return this.ID;
    }

    public TypZakaznik getTypZakaznik()
    {
        return this.typZakaznik;
    }

    public double getCasPrichodSystem()
    {
        if (this.prichodSystem == -1)
        {
            throw new RuntimeException("Prichod system nie je nastaveny!");
        }

        return this.prichodSystem;
    }

    public double getCasOdchodSystem()
    {
        if (this.odchodSystem == -1)
        {
            throw new RuntimeException("Odchod system nie je nastaveny!");
        }

        return this.odchodSystem;
    }

    public double getCasSystem()
    {
        if (this.prichodSystem == -1 || this.odchodSystem == -1)
        {
            throw new RuntimeException("Nie je nastaveny prichod/odchod system!");
        }

        return this.odchodSystem - this.prichodSystem;
    }

    public double getCasFrontAutomat()
    {
        if (this.prichodFrontAutomat == -1 || this.odchodFrontAutomat == -1)
        {
            throw new RuntimeException("Nie je nastaveny prichod/odchod automat!");
        }

        return this.odchodFrontAutomat - this.prichodFrontAutomat;
    }

    public double getCasFrontObsluzneMiesta()
    {
        if (this.prichodFrontObsluzneMiesta == -1 || this.odchodFrontObsluzneMiesta == -1)
        {
            throw new RuntimeException("Nie je nastaveny prichod/odchod obsluzne miesta!");
        }

        return this.odchodFrontObsluzneMiesta - this.prichodFrontObsluzneMiesta;
    }
}
