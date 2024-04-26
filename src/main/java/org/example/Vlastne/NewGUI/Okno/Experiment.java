package org.example.Vlastne.NewGUI.Okno;

import OSPABA.Simulation;
import org.example.Vlastne.NewGUI.Graf;
import org.example.Vlastne.NewGUI.ISimulationDelegate;
import org.example.Vlastne.Ostatne.Konstanty;
import org.example.simulation.MySimulation;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;

public class Experiment implements ISimulationDelegate
{
    private final JLabel labelAktualnaReplikacia;
    private final JPanel panelGraf;
    private ChartPanel chartPanel;

    private final int minPocetPokladni;
    private final int maxPocetPokladni;

    private Graf grafExperiment;
    private Thread experimentVlakno;

    private volatile boolean experimentStopnuty;

    private MySimulation aktualnaSimulacia;

    public Experiment(JLabel labelAktualnaReplikacia, JPanel panelGraf, int minPocetPokladni, int maxPocetPokladni)
    {
        this.validujVstupy(minPocetPokladni, maxPocetPokladni);
        this.minPocetPokladni = minPocetPokladni;
        this.maxPocetPokladni = maxPocetPokladni;

        this.labelAktualnaReplikacia = labelAktualnaReplikacia;
        this.panelGraf = panelGraf;

        this.experimentStopnuty = false;

        this.inicializujGraf();
    }

    private void inicializujGraf()
    {
        if (this.grafExperiment != null)
        {
            this.grafExperiment.resetujGraf();
        }
        this.grafExperiment = new Graf(Konstanty.MIN_POCET_POKLADNI, Konstanty.MAX_POCET_POKLADNI, Konstanty.HORNA_HRANICA_GRAF);

        int panelSirka = this.panelGraf.getWidth();
        int panelVyska = this.panelGraf.getHeight();
        this.chartPanel = new ChartPanel(this.grafExperiment.getGraf());
        this.chartPanel.setBounds(0, 0, panelSirka, panelVyska);

        this.panelGraf.add(this.chartPanel);
        this.panelGraf.repaint();
        this.panelGraf.revalidate();
    }

    public void spustiExperiment(int pocetReplikacii, double dlzkaTrvaniaSimulacie, int pocetObsluznychMiest,
        int nasada, boolean pouziNasadu, boolean zvysenyTok, boolean prestavka)
    {
        this.experimentVlakno = new Thread(() -> {
            for (int i = this.minPocetPokladni; i <= this.maxPocetPokladni; i++)
            {
                if (this.experimentStopnuty)
                {
                    break;
                }

                this.aktualnaSimulacia = new MySimulation(nasada, pouziNasadu, dlzkaTrvaniaSimulacie,
                    zvysenyTok, prestavka, pocetObsluznychMiest, i, Konstanty.MAX_RYCHLOST);
                this.aktualnaSimulacia.pridajDelegata(this);
                this.aktualnaSimulacia.simulate(pocetReplikacii);
            }
        });
        this.experimentVlakno.setName("Experiment");
        this.experimentVlakno.setDaemon(true);
        this.experimentVlakno.setPriority(Thread.MAX_PRIORITY);
        this.experimentVlakno.start();

    }

    private void validujVstupy(int minHodnota, int maxHodnota)
    {
        if (minHodnota < 1)
        {
            throw new RuntimeException("Minimalna hodnota nemoze byt mensia ako 1!");
        }

        if (minHodnota >= maxHodnota)
        {
            throw new RuntimeException("Minimalna hodnota musi byt mensia ako maximalna hodnota!");
        }
    }

    public void ukonciExperiment()
    {
        if (this.aktualnaSimulacia != null)
        {
            this.experimentStopnuty = true;
            this.aktualnaSimulacia.stopSimulation();
            this.aktualnaSimulacia.odoberDelegata(this);
            this.aktualnaSimulacia = null;
        }

        try
        {
            this.experimentVlakno.join();
        }
        catch (Exception exception)
        {
            throw new RuntimeException("Chyba pri zastavovani vlakna experimentu!");
        }

        this.panelGraf.remove(this.chartPanel);
        this.panelGraf.revalidate();
        this.panelGraf.repaint();
    }

    @Override
    public void aktualizujSa(Simulation simulacneJadro, boolean celkoveStatistiky, boolean priebezneStatistiky)
    {
        if (priebezneStatistiky)
        {
            // Okno experimentu neobsahuje priebezne statistiky
        }

        if (celkoveStatistiky)
        {
            try
            {
                MySimulation simulacia = (MySimulation)simulacneJadro;

                EventQueue.invokeLater(() -> {
                    this.grafExperiment.aktualizujGraf(simulacia.getPocetPokladni(), simulacia.getCelkovaStatistikaDlzkaFrontAutomat().mean());
                    this.labelAktualnaReplikacia.setText(simulacia.getAktualnaReplikacia() + 1 + "/" + simulacia.replicationCount());
                });
            }
            catch (Exception ex)
            {
                throw new RuntimeException("Chyba pri aktualizacia grafov!");
            }
        }
    }

    @Override
    public void aktualizujSimulacnyCas(Simulation simulacneJadro)
    {
        throw new RuntimeException("Okno experimentu neobsahuje simulacny cas!");
    }
}
