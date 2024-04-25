package org.example.vlastne;

public class Zakaznik
{
    private final TypZakaznik typZakaznik;

    private double prichodSystem;
    private double odchodSystem;

    private double prichodFrontAutomat;
    private double odchodFrontAutomat;

    public Zakaznik(TypZakaznik typZakaznik)
    {
        this.typZakaznik = typZakaznik;

        this.prichodSystem = -1;
        this.odchodSystem = -1;

        this.prichodFrontAutomat = -1;
        this.odchodFrontAutomat = -1;
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

    public TypZakaznik getTypZakaznik()
    {
        return this.typZakaznik;
    }

    public double getPrichodSystem()
    {
        if (this.prichodSystem == -1)
        {
            throw new RuntimeException("Prichod system nie je nastaveny!");
        }

        return this.prichodSystem;
    }

    public double getOdchodSystem()
    {
        if (this.odchodSystem == -1)
        {
            throw new RuntimeException("Odchod system nie je nastaveny!");
        }

        return this.odchodSystem;
    }

    public double getPrichodFrontAutomat()
    {

        if (this.prichodFrontAutomat == -1)
        {
            throw new RuntimeException("Prichod front automat nie je nastaveny!");
        }

        return this.prichodFrontAutomat;
    }

    public double getOdchodFrontAutomat()
    {
        if (this.odchodFrontAutomat == -1)
        {
            throw new RuntimeException("Odchod front automat nie je nastaveny!");
        }

        return this.odchodFrontAutomat;
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
}
