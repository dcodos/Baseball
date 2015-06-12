package com.gbasbaseball;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

public class Main extends Application {

    private static Controller controller;
    private Stage primaryStage;
    private MediaPlayer mediaPlayer;
    private WebDriver driver;
    private final String playClass = "miniplayer-volume-icon";

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("baseball.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.initKeyEventHandler(this);
        primaryStage.setTitle("Play Ball!");
        primaryStage.setScene(new Scene(root, 600, 600));

        controller.setHomeGrid(home.getName());
        controller.setAwayGrid(away.getName());

        controller.setHomeScore(home.getName() + ": " + home.getRuns());
        controller.setAwayScore(away.getName() + ": " + away.getRuns());
        controller.updateScoreColor(currentTeam);
        controller.setOuts(inningOuts + "");
        controller.setCurrentBatter("Now Batting: " + currentBatter);
        controller.setInning("Inning: " + currentInning);
        controller.setImg(currentTeam.getImg());

        if (currentTeam.first == null) {
            controller.setFirst("");
        } else {
            controller.setFirst(currentTeam.first.toString());
        }
        if (currentTeam.second == null) {
            controller.setSecond("");
        } else {
            controller.setSecond(currentTeam.second.toString());
        }
        if (currentTeam.third == null) {
            controller.setThird("");
        } else {
            controller.setThird(currentTeam.third.toString());
        }

        controller.updateScoreGrid(away.getMap(), "away");
        controller.updateScoreGrid(home.getMap(), "home");
        controller.updateGridScores(away.getRuns(), home.getRuns());
        controller.updateGridHits(away.getHits(), home.getHits());
        controller.updateToday(currentBatter.getTodayHits(), currentBatter.getTodayOuts());
        controller.updateSeasonStats(currentBatter.getAvg(), currentBatter.getHr(), currentBatter.getRbi());
        controller.currentTeam = currentTeam;
        controller.currentInning = currentInning;
        controller.window = this;
//        File audio = new File("audio\\Pecker.wav");
//        Media hit = new Media(audio.toURI().toString());
//        mediaPlayer = new MediaPlayer(hit);

        driver = new FirefoxDriver();

        // Go to the Google Suggest home page
        driver.get("http://songza.com/listen/00s-club-bangers-songza/");


        mediaPlayer = null;
        updateDisplay();

        primaryStage.show();
        this.setup();
    }


    public static void main(String[] args) {
        Main mainWindow = new Main();
        select1 = new TeamNames(mainWindow);
        select1.players();
    }

    static String outcome = null;
    String lastOutcome = null;
    static Team away;
    static Team home;
    static Player currentBatter;
    Player lastBatter;
    static Team currentTeam;
    Team lastTeam;
    static int inningOuts = 0;
    int lastOuts = 0;
    static int currentInning = 1;
    int lastInning;
    static boolean isTop = true;
    boolean lastHalf = true;
    PrintWriter out;
    boolean canUndo = false;

    static TeamNames select1;



    // Constructor for the main screen
    public void setup() {

        // Copy existing files to undo Folders
        File playersDir = new File("players\\");
        File playsDir = new File("plays\\");
        File[] allPlayers = playersDir.listFiles();
        File[] allPlays = playsDir.listFiles();
        if (allPlayers != null && allPlays != null) {
            for (File player : allPlayers) {
                try {
                    Files.copy(Paths.get("players\\" + player.getName()), Paths.get("undoPlayers\\" + player.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // DO NOTHING ON CATCH
                }
            }
            for (File plays : allPlays) {
                try {
                    Files.copy(Paths.get("plays\\" + plays.getName()), Paths.get("undoPlays\\" + plays.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // DO NOTHING ON CATCH
                }
            }
        }
        // End copy process


    }

    // Method to update info on display
    private void updateDisplay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        File audio = new File("audio\\" + currentBatter.getName() + currentBatter.getCurrentSong() + ".wav");
        currentBatter.setCurrentSong(currentBatter.getCurrentSong() + 1);

        if (audio.exists()) {
            if (driver.findElements(By.className("muted")).isEmpty()) {
                WebElement query = driver.findElement(By.className(playClass));
                query.click();
            }
            final Media hit = new Media(audio.toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.volumeProperty().unbind();
            final double fadeDuration = 7000;
            final Timeline fadeOut = new Timeline(new KeyFrame(javafx.util.Duration.millis(fadeDuration), new KeyValue(mediaPlayer.volumeProperty(), 0)));
            mediaPlayer.play();


            final long fadeTime = System.currentTimeMillis() + 12000;

            Task<Void> sleeper = new Task<Void>() {
                @Override
                protected Void call() {
                    while (System.currentTimeMillis() < fadeTime + fadeDuration) {
                        if (System.currentTimeMillis() > fadeTime) {
                            fadeOut.play();
                        }
                    }
                    if (!driver.findElements(By.className("muted")).isEmpty()) {
                        WebElement query = driver.findElement(By.className(playClass));
                        query.click();
                    }
                    return null;
                }
            };
            new Thread(sleeper).start();
            select1.readName(currentBatter, currentTeam);

        } else {
            if (driver.findElements(By.className("muted")).isEmpty()) {
                WebElement query = driver.findElement(By.className(playClass));
                query.click();
            }
            select1.readName(currentBatter, currentTeam);
            if (!driver.findElements(By.className("muted")).isEmpty()) {
                WebElement query = driver.findElement(By.className(playClass));
                query.click();
            }
        }

        audio = new File("audio\\" + currentBatter.getName() + currentBatter.getCurrentSong() + ".wav");
        if (!audio.exists()) {
            currentBatter.setCurrentSong(0);
        }
        // Read who is coming to bat
        //select1.readName(currentBatter, currentTeam);

    }

    // Method run once teams are selected
    public void startMain() throws IOException {
        away = select1.getAway();
        home = select1.getHome();
        currentTeam = away;
        currentBatter = away.dueUp();

        launch();
    }

    // Game update after outcome is selected
    public void runOutcome() throws Exception {

        // Backup all info to undo folders before changing anything
        File playersDir = new File("players\\");
        File playsDir = new File("plays\\");
        File[] allPlayers = playersDir.listFiles();
        File[] allPlays = playsDir.listFiles();
        if (allPlayers != null && allPlays != null) {
            for (File player : allPlayers) {
                try {
                    Files.copy(Paths.get("players\\" + player.getName()), Paths.get("undoPlayers\\" + player.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // DO NOTHING ON CATCH
                }
            }
            for (File plays : allPlays) {
                try {
                    Files.copy(Paths.get("plays\\" + plays.getName()), Paths.get("undoPlays\\" + plays.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // DO NOTHING ON CATCH
                }
            }
        }
        // End undo backup

        // Backup last batter, inning, team, half, and outs
        lastBatter = currentBatter;
        lastInning = currentInning;
        lastTeam = currentTeam;
        lastHalf = isTop;
        lastOuts = inningOuts;
        canUndo = true;

        // Write outcome to plays file and update accordingly
        out = new PrintWriter(new FileWriter("plays\\" + currentTeam + ".csv", true));
        if (outcome.equals("out")) {
            out.println(currentInning + "," + currentBatter + "," + "out" + "," + inningOuts + "," + currentTeam.getRuns() + "," + currentTeam.first + "," + currentTeam.second + "," + currentTeam.third);
            ++inningOuts;
            currentBatter.addOut();
        } else if (!outcome.equals("steal")) {
            currentBatter.addStat(outcome);
            out.println(currentInning + "," + currentBatter + "," + outcome + "," + inningOuts + "," + currentTeam.getRuns() + "," + currentTeam.first + "," + currentTeam.second + "," + currentTeam.third);
        }
        out.close();
        if (!outcome.equals("steal")) {
            currentTeam.updateBases(currentBatter, outcome, currentInning);
            currentTeam.nextBatter(currentBatter);

            if (inningOuts > 2) {
                this.switchInning();
            }
            currentBatter = currentTeam.dueUp();

        }

        controller.setHomeScore(home.getName() + ": " + home.getRuns());
        controller.setAwayScore(away.getName() + ": " + away.getRuns());
        controller.updateScoreColor(currentTeam);
        controller.setOuts(inningOuts + "");
        controller.setCurrentBatter("Now Batting: " + currentBatter);
        controller.setInning("Inning: " + currentInning);
        controller.setImg(currentTeam.getImg());

        if (currentTeam.first == null) {
            controller.setFirst("");
        } else {
            controller.setFirst(currentTeam.first.toString());
        }
        if (currentTeam.second == null) {
            controller.setSecond("");
        } else {
            controller.setSecond(currentTeam.second.toString());
        }
        if (currentTeam.third == null) {
            controller.setThird("");
        } else {
            controller.setThird(currentTeam.third.toString());
        }

        controller.updateScoreGrid(away.getMap(), "away");
        controller.updateScoreGrid(home.getMap(), "home");
        controller.updateGridScores(away.getRuns(), home.getRuns());
        controller.updateGridHits(away.getHits(), home.getHits());
        controller.updateToday(currentBatter.getTodayHits(), currentBatter.getTodayOuts());
        controller.updateSeasonStats(currentBatter.getAvg(), currentBatter.getHr(), currentBatter.getRbi());
        controller.currentTeam = currentTeam;
        controller.currentInning = currentInning;
        controller.window = this;
        updateDisplay();
    }

    // Method to undo last play
    public void runUndo() {

        // Restore last batter, team, half, inning, and outs
        currentBatter = lastBatter;
        currentTeam = lastTeam;
        isTop = lastHalf;
        currentInning = lastInning;
        inningOuts = lastOuts;

        // Restore bases and runs
        currentTeam.undoBases();
        currentTeam.undoRuns();
        currentBatter.undoStats();
        canUndo = false;

        // Restore info from undo folders
        File playersDir = new File("undoPlayers\\");
        File playsDir = new File("undoPlays\\");
        File[] allPlayers = playersDir.listFiles();
        File[] allPlays = playsDir.listFiles();
        if (allPlayers != null && allPlays != null) {
            for (File player : allPlayers) {
                try {
                    Files.copy(Paths.get("undoPlayers\\" + player.getName()), Paths.get("players\\" + player.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // DO NOTHING ON CATCH
                    e.printStackTrace();
                }
            }
            for (File plays : allPlays) {
                try {
                    Files.copy(Paths.get("undoPlays\\" + plays.getName()), Paths.get("plays\\" + plays.getName()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    // DO NOTHING ON CATCH
                    e.printStackTrace();
                }
            }
        }
        // End restore

        // Update display with restored info
        controller.setHomeScore(home.getName() + ": " + home.getRuns());
        controller.setAwayScore(away.getName() + ": " + away.getRuns());
        controller.updateScoreColor(currentTeam);
        controller.setOuts(inningOuts + "");
        controller.setCurrentBatter("Now Batting: " + currentBatter);
        controller.setInning("Inning: " + currentInning);
        controller.setImg(currentTeam.getImg());

        if (currentTeam.first == null) {
            controller.setFirst("");
        } else {
            controller.setFirst(currentTeam.first.toString());
        }
        if (currentTeam.second == null) {
            controller.setSecond("");
        } else {
            controller.setSecond(currentTeam.second.toString());
        }
        if (currentTeam.third == null) {
            controller.setThird("");
        } else {
            controller.setThird(currentTeam.third.toString());
        }

        controller.updateScoreGrid(away.getMap(), "away");
        controller.updateScoreGrid(home.getMap(), "home");
        controller.updateGridScores(away.getRuns(), home.getRuns());
        controller.updateGridHits(away.getHits(), home.getHits());
        controller.updateToday(currentBatter.getTodayHits(), currentBatter.getTodayOuts());
        controller.updateSeasonStats(currentBatter.getAvg(), currentBatter.getHr(), currentBatter.getRbi());
        controller.currentTeam = currentTeam;
        controller.currentInning = currentInning;
        controller.window = this;
        updateDisplay();
    }

    // Method to process new half inning
    public void switchInning() {

        currentTeam.finishedInning(currentInning);

        // Create string to read inning status aloud
        String read = "After the ";
        String inningString;
        if (currentInning == 1) {
            inningString = "1st";
        } else if (currentInning == 2) {
            inningString = "2nd";
        } else if (currentInning == 3) {
            inningString = "3rd";
        } else {
            inningString = currentInning + "th";
        }
        if (isTop) {
            read = read + "top half of the " + inningString + " inning, ";
        } else {
            read = read + "bottom half of the " + inningString + " inning, ";
        }
        if (away.getRuns() > home.getRuns()) {
            read = read + away.getName() + " leads " + home.getName() + ", " + away.getRuns() + " to " + home.getRuns();
        } else if (home.getRuns() > away.getRuns()) {
            read = read + home.getName() + " leads " + away.getName() + ", " + home.getRuns() + " to " + away.getRuns();
        } else {
            read = read + "the game is tied, " + home.getRuns() + " to " + away.getRuns();
        }

        WebElement query = driver.findElement(By.className(playClass));
        query.click();
        select1.readSummary(read);
        query.click();
        query.click();
        // End read aloud process

        // Reset inningOuts and bases
        inningOuts = 0;
        currentTeam.resetBases();

        // Switch Team at bat
        if (currentTeam.equals(home)) {
            currentTeam = away;
        } else {
            currentTeam = home;
        }

        if (!isTop) {
            ++currentInning;
            isTop = true;
        } else {
            isTop = false;
        }
    }

    public void setFullScreen(boolean b) {
        primaryStage.setFullScreen(b);
    }

    public void caughtStealing() {
        try {
            ++inningOuts;
            if (inningOuts > 2) {
                switchInning();
            }
            currentBatter = currentTeam.dueUp();
        } catch (Exception e1) {
            // DO NOTHING ON CATCH
            e1.printStackTrace();
        }
    }



}
