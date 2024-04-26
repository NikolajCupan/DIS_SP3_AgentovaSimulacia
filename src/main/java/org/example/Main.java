package org.example;

import org.example.simulation.MySimulation;
import org.example.Vlastne.Ostatne.Konstanty;

public class Main
{
    public static void main(String[] args)
    {
        MySimulation s = new MySimulation(420, false, Konstanty.TRVANIE_CAS_SEKUNDY,
            false, false, 13, 4, Konstanty.MAX_RYCHLOST);
        s.simulate(25000);
    }
}