package org.example;

import org.example.simulation.MySimulation;
import org.example.Vlastne.Ostatne.GeneratorNasad;
import org.example.Vlastne.Ostatne.Konstanty;

public class Main
{
    public static void main(String[] args)
    {
        GeneratorNasad.inicializujGeneratorNasad(420, true);

        MySimulation s = new MySimulation(Konstanty.TRVANIE_CAS_SEKUNDY, false);
        s.simulate(1);
    }
}