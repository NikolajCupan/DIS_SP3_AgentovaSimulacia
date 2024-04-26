package org.example;

import org.example.simulation.MySimulation;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;

public class Main
{
    public static void main(String[] args)
    {
        MySimulation s = new MySimulation(420, true, Konstanty.TRVANIE_CAS_SEKUNDY, false, false, 4, 3);
        s.simulate(1000);
    }
}