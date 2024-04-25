package org.example.simulation;

import OSPABA.MessageForm;
import OSPABA.Simulation;
import org.example.vlastne.TypZakaznik;
import org.example.vlastne.Zakaznik;

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
