package org.example.Vlastne.Zakaznik;

import org.example.Vlastne.Generatory.VelkostTovaru;
import org.example.Vlastne.Objekty.ObsluzneMiesto;
import org.example.Vlastne.Objekty.Pokladna;
import org.example.Vlastne.Objekty.TypOkno;
import org.example.Vlastne.Ostatne.Identifikator;

public class Zakaznik implements Cloneable
{
    private final long ID;
    private final TypZakaznik typZakaznik;
    private boolean predcasnyOdchod;

    private double prichodSystem;
    private double odchodSystem;

    private double prichodFrontAutomat;
    private double odchodFrontAutomat;

    private ObsluzneMiesto obsluzneMiesto;
    private VelkostTovaru velkostTovaru;
    private double prichodFrontObsluzneMiesta;
    private double odchodFrontObsluzneMiesta;

    private Pokladna pokladna;
    private double prichodFrontPokladna;
    private double odchodFrontPokladna;

    private double zaciatokVyzdvihovaniaTovaru;

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
        this.velkostTovaru = null;
        this.prichodFrontObsluzneMiesta = -1;
        this.odchodFrontObsluzneMiesta = -1;

        this.pokladna = null;
        this.prichodFrontPokladna = -1;
        this.odchodFrontPokladna = -1;

        this.zaciatokVyzdvihovaniaTovaru = -1;
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

    public void setPokladna(Pokladna pokladna)
    {
        if (!pokladna.pokladnaDostupna() || pokladna.getObsadena())
        {
            throw new RuntimeException("Zakaznik nemoze byt obsluhovany pri danej pokladni!");
        }
        if (this.pokladna != null)
        {
            throw new RuntimeException("Zakaznik uz ma nastavenu pokladnu!");
        }

        this.pokladna = pokladna;
    }

    public void setPrichodFrontPokladna(double prichodFrontPokladna)
    {
        this.prichodFrontPokladna = prichodFrontPokladna;
    }

    public void setOdchodFrontPokladna(double odchodFrontPokladna)
    {
        this.odchodFrontPokladna = odchodFrontPokladna;
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

    public void setVelkostTovaru(VelkostTovaru velkostTovaru)
    {
        if (this.velkostTovaru != null)
        {
            throw new RuntimeException("Zakaznik uz ma nastavenu velkost tovaru!");
        }

        this.velkostTovaru = velkostTovaru;
    }

    public void setZaciatokVyzdvihovaniaTovaru(double zaciatokVyzdvihovaniaTovaru)
    {
        if (this.velkostTovaru != VelkostTovaru.VELKY)
        {
            throw new RuntimeException("Zakaznik nemoze vyzdvihovat tovar!");
        }

        this.zaciatokVyzdvihovaniaTovaru = zaciatokVyzdvihovaniaTovaru;
    }

    public Pokladna getPokladna()
    {
        if (this.pokladna == null)
        {
            throw new RuntimeException("Pokladna nie je nastavena!");
        }

        return this.pokladna;
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

    public VelkostTovaru getVelkostTovaru()
    {
        if (this.velkostTovaru == null)
        {
            throw new RuntimeException("Zakaznik nema nastavenu velkost tovaru!");
        }

        return this.velkostTovaru;
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

    public double getCasFrontPokladna()
    {
        if (this.prichodFrontPokladna == -1 || this.odchodFrontPokladna == -1)
        {
            throw new RuntimeException("Nie je nastaveny prichod/odchod pokladna!");
        }

        return this.odchodFrontPokladna - this.prichodFrontPokladna;
    }

    public double getCasPrichodSystem()
    {
        return this.prichodSystem;
    }

    public double getCasOdchodSystem()
    {
        return this.odchodSystem;
    }

    public double getCasPrichodFrontAutomat()
    {
        return this.prichodFrontAutomat;
    }

    public double getCasOdchodFrontAutomat()
    {
        return this.odchodFrontAutomat;
    }

    public double getCasPrichodFrontObsluzneMiesta()
    {
        return this.prichodFrontObsluzneMiesta;
    }

    public double getCasOdchodFrontObsluzneMiesta()
    {
        return this.odchodFrontObsluzneMiesta;
    }

    public double getCasPrichodFrontPokladna()
    {
        return this.prichodFrontPokladna;
    }

    public double getCasOdchodFrontPokladna()
    {
        return this.odchodFrontPokladna;
    }

    public double getZaciatokVyzdvihovaniaTovaru()
    {
        return this.zaciatokVyzdvihovaniaTovaru;
    }

    public String getStav()
    {
        if (this.prichodSystem == -1 || this.prichodFrontAutomat == -1)
        {
            return "Prichod";
        }

        if (this.odchodFrontAutomat == -1)
        {
            return "Front pred automatom";
        }

        if (this.prichodFrontObsluzneMiesta == -1)
        {
            return "Obsluha pri automate";
        }
        if (this.odchodFrontObsluzneMiesta == -1)
        {
            return "Front pred obsluznym miestom";
        }

        if (this.prichodFrontPokladna == -1)
        {
            return "Obsluha pri obsluznom mieste";
        }
        if (this.odchodFrontPokladna == -1)
        {
            return "Front pred pokladnami";
        }

        if (this.zaciatokVyzdvihovaniaTovaru == -1)
        {
            return "Obsluha pri pokladni";
        }

        return "Vyzdvihovanie tovaru";
    }

    @Override
    public Zakaznik clone()
    {
        try
        {
            Zakaznik clone = (Zakaznik)super.clone();
            clone.obsluzneMiesto = null;
            clone.pokladna = null;
            return clone;
        }
        catch (CloneNotSupportedException e)
        {
            throw new AssertionError();
        }
    }
}
