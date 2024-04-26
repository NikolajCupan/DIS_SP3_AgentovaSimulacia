package org.example.Vlastne.NewGUI;

import OSPABA.Simulation;

public interface ISimulationDelegate
{
    void aktualizujSa(Simulation simulacneJadro, boolean celkoveStatistiky, boolean priebezneStatistiky);
    void aktualizujSimulacnyCas(Simulation simulacneJadro);
}
