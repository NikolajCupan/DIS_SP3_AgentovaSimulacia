package org.example.Vlastne.Ostatne;

import OSPStat.Stat;
import org.example.simulation.MySimulation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class Prezenter
{
    public static DecimalFormat FORMATOVAC = new DecimalFormat("#.##");

    public static void celkovyCasSystem(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaCasSystem();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean() / 60.0) + " min [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0] / 60.0) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1] / 60.0) + "]");
        }
    }

    public static void celkovyCasFrontAutomat(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaCasFrontAutomat();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean()) + " sec [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0]) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1]) + "]");
        }
    }

    public static void celkovaDlzkaFrontAutomat(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaDlzkaFrontAutomat();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean())+ " [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0]) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1]) + "]");
        }
    }

    public static void celkoveVytazenieAutomat(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaVytazenieAutomat();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean() * 100) + " % [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0] * 100) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1] * 100) + "]");
        }
    }

    public static void poslednyOdchod(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaCasPoslednyOdchod();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.naformatujCas(statistika.mean()) + " [" +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[0]) + ", " +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[1]) + "]");
        }
    }

    public static void pocetObsluzenych(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaPocetObsluzenychZakaznikov();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean()) + " [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0]) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1]) + "]");
        }
    }

    public static void celkovyCasFrontObsluzneMiesta(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaCasFrontObsluzneMiesta();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean()) + " sec [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0]) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1]) + "]");
        }
    }

    public static void celkovaDlzkaFrontObsluzneMiesta(MySimulation simulacia, JLabel label)
    {
        Stat statistika = simulacia.getCelkovaStatistikaDlzkaFrontObsluzneMiesta();

        if (statistika.sampleSize() < 2.0)
        {
            label.setText("n/a");
        }
        else
        {
            label.setText(Prezenter.zaokruhli(statistika.mean()) + " [" +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[0]) + ", " +
                Prezenter.zaokruhli(statistika.confidenceInterval_95()[1]) + "]");
        }
    }

    public static void tabulkaCelkoveObsluzneMiesta(MySimulation simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);
                Stat[] statObycajneObsluzneMiesta = simulacia.getCelkovaStatistikaVytazenieObycajneObsluzneMiesta();
                for (Stat statistika : statObycajneObsluzneMiesta)
                {
                    model.addRow(new Object[]{
                        "Obycajny",
                        Prezenter.zaokruhli(statistika.mean() * 100) + " %"
                    });
                }

                Stat[] statOnlineObsluzneMiesta = simulacia.getCelkovaStatistikaVytazenieOnlineObsluzneMiesta();
                for (Stat statistika : statOnlineObsluzneMiesta)
                {
                    model.addRow(new Object[]{
                        "Online",
                        Prezenter.zaokruhli(statistika.mean() * 100) + " %"
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii celkovej tabulky okien!");
        }
    }

    public static void tabulkaCelkovePokladne(MySimulation simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                Stat[] vytazenie = simulacia.getCelkovaStatistikaVytazeniePokladne();
                Stat[] cakanie = simulacia.getCelkovaStatistikaCakanieFrontPokladne();
                Stat[] dlzkaFront = simulacia.getCelkovaStatistikaDlzkaFrontPokladne();

                for (int i = 0; i < vytazenie.length; i++)
                {
                    model.addRow(new Object[]{
                        i,
                        Prezenter.zaokruhli(vytazenie[i].mean() * 100) + " %",
                        Prezenter.zaokruhli(dlzkaFront[i].mean()),
                        Prezenter.zaokruhli(cakanie[i].mean()) + " sec"
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii celkovej tabulky okien!");
        }
    }

    public static void aktualnaReplikacia(MySimulation simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.getAktualnaReplikacia() + 1));
    }

    public static void simulacnyCas(MySimulation simulacia, JLabel label)
    {
        label.setText(Prezenter.naformatujCas(simulacia.getAktualnySimulacnyCas()));
    }

    public static String naformatujCas(double casOdZaciatku)
    {
        if (casOdZaciatku < 0)
        {
            return "n/a";
        }

        int pocetHodin = (int)Math.floor(casOdZaciatku / 3600);
        int pocetMinut = (int)Math.floor((casOdZaciatku - pocetHodin * 3600) / 60);
        int pocetSekund = (int)Math.floor(casOdZaciatku - pocetHodin * 3600 - pocetMinut * 60);

        final int hodinaOtvorenia = Konstanty.HODINA_OTVORENIA;
        pocetHodin += hodinaOtvorenia;

        return Prezenter.casNaString(pocetHodin) + ":" + Prezenter.casNaString(pocetMinut) + ":" + Prezenter.casNaString(pocetSekund);
    }

    private static String casNaString(int cas)
    {
        String casString = String.valueOf(cas);

        if (casString.length() == 1)
        {
            return '0' + casString;
        }
        else
        {
            return casString;
        }
    }

    private static String zaokruhli(double cislo)
    {
        if (cislo < 0)
        {
            return "n/a";
        }

        return Prezenter.FORMATOVAC.format(cislo);
    }
}
