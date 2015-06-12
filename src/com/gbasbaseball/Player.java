package com.gbasbaseball;

import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;


public class Player {

    // Define stat fields
    private final String name;
    private int singles;
    private int doubles;
    private int triples;
    private int hrs;
    private int outs;
    private int rbis;
    private int runs;
    private int sb;
    private int cs;
    PrintWriter out;
    private int todayHits = 0;
    private int lastTodayHits = 0;
    private int todayOuts = 0;
    private int lastTodayOuts = 0;
    private int currentSong = 0;

    // Constructor to create player with name
    public Player(String name) {
        this.name = name;
    }

    // Adds stats based on outcome
    public void addStat(String outcome) throws Exception {
        lastTodayHits = todayHits;
        updateTotals();
        if (outcome.equals("single")) {
            ++this.singles;
            ++this.todayHits;
        } else if (outcome.equals("double")) {
            ++this.doubles;
            ++this.todayHits;
        } else if (outcome.equals("triple")) {
            ++this.triples;
            ++this.todayHits;
        } else if (outcome.equals("hr")) {
            ++this.hrs;
            ++this.todayHits;
        }
        addToFile();
    }

    // Add out to player stats
    public void addOut() throws Exception {
        lastTodayOuts = todayOuts;
        updateTotals();
        ++this.outs;
        ++this.todayOuts;
        addToFile();
    }

    @Override
    public String toString() {
        return this.name;
    }

    // Return name
    public String getName() {
        return this.name;
    }

    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player p = (Player) o;
            if (p.getName().equals(this.name)) {
                return true;
            }
        }
        return false;
    }

    public void addRunScored() throws Exception {
        updateTotals();
        ++this.runs;
        addToFile();
    }

    public void addRbi() throws Exception {
        updateTotals();
        ++this.rbis;
        addToFile();
    }

    public void addSb() throws Exception {
        updateTotals();
        ++this.sb;
        addToFile();
    }

    public void addCs() throws Exception {
        updateTotals();
        ++this.cs;
        addToFile();
    }

    public void addToFile() throws Exception {
        try {
            out = new PrintWriter(new FileWriter("players\\" + this.name + ".csv", false));
        } catch (IOException e) {
            // DO NOTHING ON CATCH
            e.printStackTrace();
        }
        int hits = this.singles + this.doubles + this.triples + this.hrs;
        int abs = hits + this.outs;
        out.println(hits + "," + this.doubles + "," + this.triples + "," + this.hrs + "," + this.rbis + "," + this.runs + "," + this.sb + "," + this.cs + "," + abs + "," + this.singles);
        out.close();

        File playersDir = new File("players\\");
        File[] allPlayers = playersDir.listFiles();
        try {
            out = new PrintWriter(new FileWriter("allPlayers.csv", false));
        } catch (IOException e) {
            // DO NOTHING ON CATCH
            e.printStackTrace();
        }
        out.println("Name" + "," + "AVG" + "," + "HITS" + "," + "2B" + "," + "3B" + "," + "HR" + "," + "RBIS" + "," + "RUNS" + "," + "SB" + "," + "CS" + "," + "AB" + "," + "1B");
        for (File player : allPlayers) {
            Scanner fileIn = new Scanner(player);
            String line = fileIn.nextLine();
            String[] stats = line.split(",");
            String fileName = player.getName();
            String[] infoParts = fileName.split("\\.");
            String playerName = infoParts[0];

            double thisAvg = Double.parseDouble(stats[0]) / Double.parseDouble(stats[8]);
            DecimalFormat numFormat = new DecimalFormat("#.###");
            String formatAvg = numFormat.format(thisAvg);
            out.println(playerName + "," + formatAvg + "," + stats[0] + "," + stats[1] + "," + stats[2] + "," + stats[3] + "," + stats[4] + "," + stats[5] + "," + stats[6] + "," + stats[7] + "," + stats[8] + "," + stats[9]);
            fileIn.close();
        }
        out.close();
    }

    public void updateTotals() throws Exception {
        File f = new File("players\\" + this.name + ".csv");
        if (f.exists() && !f.isDirectory()) {
            Scanner s = new Scanner(f);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] parts = line.split(",");
                this.singles = Integer.parseInt(parts[9]);
                this.doubles = Integer.parseInt(parts[1]);
                this.triples = Integer.parseInt(parts[2]);
                this.hrs = Integer.parseInt(parts[3]);
                this.rbis = Integer.parseInt(parts[4]);
                this.runs = Integer.parseInt(parts[5]);
                this.sb = Integer.parseInt(parts[6]);
                this.cs = Integer.parseInt(parts[7]);
                this.outs = Integer.parseInt(parts[8]) - Integer.parseInt(parts[0]);
            }
            s.close();
        }
    }

    public int getTodayHits() {
        return this.todayHits;
    }

    public int getTodayOuts() {
        return this.todayOuts;
    }

    public double getAvg() {
        File f = new File("players\\" + this.name + ".csv");
        int hits = 0;
        int ab = 0;
        if (f.exists() && !f.isDirectory()) {
            Scanner s = null;
            try {
                s = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] parts = line.split(",");
                hits = Integer.parseInt(parts[0]);
                ab = Integer.parseInt(parts[8]);
            }
            s.close();
        }
        if (ab == 0) {
            return 0;
        }
        return ((double) hits) / ab;
    }
    public int getHr() {
        File f = new File("players\\" + this.name + ".csv");
        int hr = 0;
        if (f.exists() && !f.isDirectory()) {
            Scanner s = null;
            try {
                s = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] parts = line.split(",");
                hr = Integer.parseInt(parts[3]);
            }
            s.close();
        }
        return hr;
    }

    public int getRbi() {
        File f = new File("players\\" + this.name + ".csv");
        int rbi = 0;
        if (f.exists() && !f.isDirectory()) {
            Scanner s = null;
            try {
                s = new Scanner(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (s.hasNextLine()) {
                String line = s.nextLine();
                String[] parts = line.split(",");
                rbi = Integer.parseInt(parts[4]);
            }
            s.close();
        }
        return rbi;
    }

    public void undoStats() {
        todayOuts = lastTodayOuts;
        todayHits = lastTodayHits;

    }

    public int getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(int song) {
        currentSong = song;
    }
}