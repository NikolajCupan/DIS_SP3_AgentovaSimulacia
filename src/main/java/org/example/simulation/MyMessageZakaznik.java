package org.example.simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import org.example.Vlastne.Zakaznik.TypZakaznik;
import org.example.Vlastne.Zakaznik.Zakaznik;

public class MyMessageZakaznik extends MessageForm
{
    private Zakaznik zakaznik;

    public Zakaznik getZakaznik()
    {
        return this.zakaznik;
    }

    public MyMessageZakaznik(Simulation mySim, TypZakaznik typZakaznik)
    {
        super(mySim);

        // Vlastne
        this.zakaznik = new Zakaznik(typZakaznik);
    }

    public MyMessageZakaznik(Simulation mySim, Zakaznik zakaznik)
    {
        super(mySim);

        // Vlastne
        this.zakaznik = zakaznik;
    }

    protected MyMessageZakaznik(MessageForm original)
    {
        super(original);
        // copy() is called in superclass
    }

    @Override
    public MessageForm createCopy()
    {
        return new MyMessageZakaznik(this);
    }
}
