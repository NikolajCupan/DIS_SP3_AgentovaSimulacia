package org.example.Vlastne.Ostatne;

import OSPABA.MessageForm;
import OSPStat.Stat;
import org.example.Vlastne.Objekty.ObsluzneMiesto;
import org.example.Vlastne.Objekty.Pokladna;
import org.example.Vlastne.Zakaznik.TypZakaznik;
import org.example.Vlastne.Zakaznik.Zakaznik;
import org.example.agents.AgentAutomat;
import org.example.simulation.MyMessageZakaznik;
import org.example.simulation.MySimulation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;

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
            label.setText(Prezenter.naformatujCas(statistika.mean(), false) + " [" +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[0], false) + ", " +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[1], false) + "]");
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
            label.setText(Prezenter.naformatujCas(statistika.mean(), false) + " [" +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[0], false) + ", " +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[1], false) + "]");
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
            label.setText(Prezenter.naformatujCas(statistika.mean(), true) + " [" +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[0], true) + ", " +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[1], true) + "]");
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
            label.setText(Prezenter.naformatujCas(statistika.mean(), false) + " [" +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[0], false) + ", " +
                Prezenter.naformatujCas(statistika.confidenceInterval_95()[1], false) + "]");
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
                    if (statistika.sampleSize() < 2)
                    {
                        break;
                    }

                    String riadok = Prezenter.zaokruhli(statistika.mean() * 100) + " % [" +
                        Prezenter.zaokruhli(statistika.confidenceInterval_95()[0] * 100) + ", " +
                        Prezenter.zaokruhli(statistika.confidenceInterval_95()[1] * 100) + "]";

                    model.addRow(new Object[]{
                        "Obycajny",
                        riadok
                    });
                }

                Stat[] statOnlineObsluzneMiesta = simulacia.getCelkovaStatistikaVytazenieOnlineObsluzneMiesta();
                for (Stat statistika : statOnlineObsluzneMiesta)
                {
                    if (statistika.sampleSize() < 2)
                    {
                        break;
                    }

                    String riadok = Prezenter.zaokruhli(statistika.mean() * 100) + " % [" +
                        Prezenter.zaokruhli(statistika.confidenceInterval_95()[0] * 100) + ", " +
                        Prezenter.zaokruhli(statistika.confidenceInterval_95()[1] * 100) + "]";

                    model.addRow(new Object[]{
                        "Online",
                        riadok
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
                    if (vytazenie[i].sampleSize() < 2)
                    {
                        break;
                    }

                    String vytazenieString = Prezenter.zaokruhli(vytazenie[i].mean() * 100) + " % [" +
                        Prezenter.zaokruhli(vytazenie[i].confidenceInterval_95()[0] * 100) + ", " +
                        Prezenter.zaokruhli(vytazenie[i].confidenceInterval_95()[1] * 100) + "]";
                    String dlzkaFrontString = Prezenter.zaokruhli(dlzkaFront[i].mean()) + " [" +
                        Prezenter.zaokruhli(dlzkaFront[i].confidenceInterval_95()[0]) + ", " +
                        Prezenter.zaokruhli(dlzkaFront[i].confidenceInterval_95()[1]) + "]";
                    String cakanieString = Prezenter.naformatujCas(cakanie[i].mean(), false) + " [" +
                        Prezenter.naformatujCas(cakanie[i].confidenceInterval_95()[0], false) + ", " +
                        Prezenter.naformatujCas(cakanie[i].confidenceInterval_95()[1], false) + "]";


                    model.addRow(new Object[]{
                        i,
                        vytazenieString,
                        dlzkaFrontString,
                        cakanieString
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii celkovej tabulky okien!");
        }
    }

    public static void casFrontAutomat(MySimulation simulacia, JLabel label)
    {
        label.setText(Prezenter.naformatujCas(simulacia.agentAutomat().getStatCasFrontAutomat().mean(), false));
    }

    public static void dlzkaFrontAutomat(MySimulation simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.agentAutomat().getWstatDlzkaFrontAutomat().mean())));
    }

    public static void aktualnaDlzkaFrontAutomat(MySimulation simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.agentAutomat().getPocetFront()));
    }

    public static void vytazenieAutomat(MySimulation simulacia, JLabel label)
    {
        AgentAutomat automat = simulacia.agentAutomat();
        label.setText(Prezenter.zaokruhli(automat.getWstatVytazenieAutomat().mean() * 100) + " %");
    }

    public static void casFrontOkno(MySimulation simulacia, JLabel label)
    {
        label.setText(Prezenter.naformatujCas(simulacia.agentAutomat().getStatCasFrontAutomat().mean(), false));
    }

    public static void dlzkaFrontObsluzneMiesta(MySimulation simulacia, JLabel label)
    {
        label.setText(String.valueOf(Prezenter.zaokruhli(simulacia.agentObsluzneMiesta().getWstatDlzkaFrontObsluzneMiesta().mean())));
    }

    public static void aktualnaDlzkaFrontOkno(MySimulation simulacia, JLabel label)
    {
        Collection<MessageForm> front = simulacia.agentObsluzneMiesta().getFront();
        StringBuilder stavFront = new StringBuilder("]");

        for (MessageForm sprava : front)
        {
            Zakaznik zakaznik = ((MyMessageZakaznik)sprava).getZakaznik();
            switch (zakaznik.getTypZakaznik())
            {
                case TypZakaznik.ONLINE:
                    stavFront.append("O ");
                    break;
                case TypZakaznik.BEZNY:
                    stavFront.append("B ");
                    break;
                case TypZakaznik.ZMLUVNY:
                    stavFront.append("Z ");
                    break;
            }
        }

        for (int i = front.size(); i < Konstanty.KAPACITA_FRONT_OBSLUZNE_MIESTA; i++)
        {
            stavFront.append("X ");
        }
        stavFront.setLength(stavFront.length() - 1);
        stavFront.append("[");
        stavFront.reverse();

        stavFront.insert(0, front.size() +  ": -> ");
        stavFront.append(" ->");

        label.setText(stavFront.toString());
    }

    public static void tabulkaAgenti(MySimulation simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                ArrayList<Zakaznik> zakaznici = new ArrayList<>(simulacia.agentOkolie().getZakazniciSystem());

                model.setRowCount(0);
                for (Zakaznik zakaznik : zakaznici)
                {
                    String prichodFrontAutomat = Prezenter.naformatujCas(zakaznik.getCasPrichodFrontAutomat(), true);
                    String odchodFrontAutomat = Prezenter.naformatujCas(zakaznik.getCasOdchodFrontAutomat(), true);

                    String prichodFrontObsluzneMiesta = Prezenter.naformatujCas(zakaznik.getCasPrichodFrontObsluzneMiesta(), true);
                    String odchodFrontObsluzneMiesta = Prezenter.naformatujCas(zakaznik.getCasOdchodFrontObsluzneMiesta(), true);

                    String prichodFrontPokladna = Prezenter.naformatujCas(zakaznik.getCasPrichodFrontPokladna(), true);
                    String odchodFrontPokladna = Prezenter.naformatujCas(zakaznik.getCasOdchodFrontPokladna(), true);

                    model.addRow(new Object[]{
                        zakaznik.getID(),
                        zakaznik.getTypZakaznik(),
                        zakaznik.getStav(),
                        Prezenter.naformatujCas(zakaznik.getCasPrichodSystem(), true),
                        prichodFrontAutomat + " - " + odchodFrontAutomat,
                        prichodFrontObsluzneMiesta + " - " + odchodFrontObsluzneMiesta,
                        prichodFrontPokladna + " - " + odchodFrontPokladna,
                        Prezenter.naformatujCas(zakaznik.getZaciatokVyzdvihovaniaTovaru(), true)
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky agentov!");
        }
    }

    public static void tabulkaObsluzneMiesta(MySimulation simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                Collection<ObsluzneMiesto> obsluzneMiestaObycajni = simulacia.agentObsluzneMiesta().getObycajneObsluzneMiesta();
                for (ObsluzneMiesto obsluznemiesto : obsluzneMiestaObycajni)
                {
                    model.addRow(new Object[]{
                        "Obycajne",
                        obsluznemiesto.getObsadene(),
                        Prezenter.zaokruhli(obsluznemiesto.getWstatVytazenieObsluzneMiesto().mean() * 100) + " %"
                    });
                }

                Collection<ObsluzneMiesto> obsluzneMiestaOnline = simulacia.agentObsluzneMiesta().getOnlineObsluzneMiesta();
                for (ObsluzneMiesto obsluzneMiesto : obsluzneMiestaOnline)
                {
                    model.addRow(new Object[]{
                        "Online",
                        obsluzneMiesto.getObsadene(),
                        Prezenter.zaokruhli(obsluzneMiesto.getWstatVytazenieObsluzneMiesto().mean() * 100) + " %"
                    });
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky okien!");
        }
    }

    public static void tabulkaPokladne(MySimulation simulacia, JTable tabulka)
    {
        try
        {
            EventQueue.invokeLater(() -> {
                DefaultTableModel model = (DefaultTableModel)tabulka.getModel();
                model.setRowCount(0);

                int pocitadlo = 0;
                Collection<Pokladna> pokladne = simulacia.agentPokladne().getPokladne();
                for (Pokladna pokladna : pokladne)
                {
                    model.addRow(new Object[]{
                        pocitadlo,
                        pokladna.getObsadena(),
                        Prezenter.zaokruhli(pokladna.getWstatVytazeniePokladna().mean() * 100) + " %",
                        pokladna.getPocetFront(),
                        Prezenter.zaokruhli(pokladna.getWstatDlzkaFrontPokladna().mean()),
                        Prezenter.naformatujCas(pokladna.getStatCasFrontPokladna().mean(), false),
                        pokladna.getPrestavka(),
                        pokladna.getNahrada()
                    });

                    pocitadlo++;
                }
            });
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Chyba pri aktualizacii tabulky pokladni!");
        }
    }

    public static void aktualnaReplikacia(MySimulation simulacia, JLabel label)
    {
        label.setText(String.valueOf(simulacia.getAktualnaReplikacia() + 1));
    }

    public static void simulacnyCas(MySimulation simulacia, JLabel label)
    {
        label.setText(Prezenter.naformatujCas(simulacia.getAktualnySimulacnyCas(), true));
    }

    public static String naformatujCas(double casOdZaciatku)
    {
        return Prezenter.naformatujCas(casOdZaciatku, true);
    }

    public static String naformatujCas(double casOdZaciatku, boolean posun)
    {
        if (casOdZaciatku < 0)
        {
            return "n/a";
        }

        int pocetHodin = (int)Math.floor(casOdZaciatku / 3600);
        int pocetMinut = (int)Math.floor((casOdZaciatku - pocetHodin * 3600) / 60);
        int pocetSekund = (int)Math.floor(casOdZaciatku - pocetHodin * 3600 - pocetMinut * 60);

        final int hodinaOtvorenia = Konstanty.HODINA_OTVORENIA;
        if (posun)
        {
            pocetHodin += hodinaOtvorenia;
        }

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
