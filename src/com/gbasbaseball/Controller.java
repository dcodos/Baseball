package com.gbasbaseball;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML private Text homeScore;
    @FXML private Text awayScore;
    @FXML private Text currentBatterText;
    @FXML private Text inningText;

    @FXML private VBox vBox1;

    @FXML private Circle out1;
    @FXML private Circle out2;

    @FXML private ImageView baseImg;

    @FXML private Text onFirst;
    @FXML private Text onSecond;
    @FXML private Text onThird;

    @FXML private Text homeGrid;
    @FXML private Text awayGrid;

    @FXML private Text away1;
    @FXML private Text away2;
    @FXML private Text away3;
    @FXML private Text away4;
    @FXML private Text away5;
    @FXML private Text away6;
    @FXML private Text away7;
    @FXML private Text away8;
    @FXML private Text away9;
    @FXML private Text awayR;
    @FXML private Text awayH;

    @FXML private Text home1;
    @FXML private Text home2;
    @FXML private Text home3;
    @FXML private Text home4;
    @FXML private Text home5;
    @FXML private Text home6;
    @FXML private Text home7;
    @FXML private Text home8;
    @FXML private Text home9;
    @FXML private Text homeR;
    @FXML private Text homeH;

    @FXML private Text avgText;
    @FXML private Text hrText;
    @FXML private Text rbiText;
    @FXML private Text todayText;

    public Main window;
    public Team currentTeam;
    public int currentInning;

    public void setHomeScore(String str) {
        homeScore.setText(str);
    }
    public void setAwayScore(String str) {
        awayScore.setText(str);
    }
    public void setCurrentBatter(String str) {
        currentBatterText.setText(str);
    }
    public void setInning(String str) {
        inningText.setText(str);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    public void initKeyEventHandler(Main mainWindow) {
        window = mainWindow;
        final MultiplePressedKeysEventHandler keyHandler =
                new MultiplePressedKeysEventHandler(new MultiplePressedKeysEventHandler.MultiKeyEventHandler() {

                    public void handle(MultiplePressedKeysEventHandler.MultiKeyEvent ke) {
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.DIGIT1)) {
                            try {
                                window.lastOutcome = window.outcome;
                                window.outcome = "single";
                                window.runOutcome();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.DIGIT2)) {
                            try {
                                window.lastOutcome = window.outcome;
                                window.outcome = "double";
                                window.runOutcome();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.DIGIT3)) {
                            try {
                                window.lastOutcome = window.outcome;
                                window.outcome = "triple";
                                window.runOutcome();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.DIGIT4)) {
                            try {
                                window.lastOutcome = window.outcome;
                                window.outcome = "hr";
                                window.runOutcome();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.O)) {
                            try {
                                window.lastOutcome = window.outcome;
                                window.outcome = "out";
                                window.runOutcome();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.F)) {
                            window.setFullScreen(true);
                        }
                        if (ke.isPressed(KeyCode.CONTROL) && ke.isPressed(KeyCode.SHIFT) && ke.isPressed(KeyCode.Z)) {

                            if (window.canUndo) {
                                window.runUndo();
                            }
                        }

                    }
                });

        vBox1.requestFocus();
        vBox1.setFocusTraversable(true);
        vBox1.setOnKeyPressed(keyHandler);
        vBox1.setOnKeyReleased(keyHandler);
    }

    public void setOuts(String s) {
        int outs = Integer.parseInt(s);
        if (outs == 0) {
            out1.setFill(Color.TRANSPARENT);
            out2.setFill(Color.TRANSPARENT);
        }
        if (outs == 1) {
            out1.setFill(Color.BLACK);
            out2.setFill(Color.TRANSPARENT);
        }
        if (outs == 2) {
            out1.setFill(Color.BLACK);
            out2.setFill(Color.BLACK);
        }
    }

    public void setImg(String s) {
        Image img = new Image(new File("img\\" + s).toURI().toString());
        baseImg.setImage(img);
    }

    public void setFirst(String s) {
        onFirst.setText(s);
    }
    public void setSecond(String s) {
        onSecond.setText(s);
    }
    public void setThird(String s) {
        onThird.setText(s);
    }

    public void setHomeGrid(String s) {
        homeGrid.setText(s);
    }
    public void setAwayGrid(String s) {
        awayGrid.setText(s);
    }

    public void updateScoreGrid(Map<Integer, Integer> score, String teamName) {
        if (teamName.equals("away")) {
            if (score.containsKey(1)) {
                away1.setText(score.get(1) + "");
            }
            if (score.containsKey(2)) {
                away2.setText(score.get(2) + "");
            }
            if (score.containsKey(3)) {
                away3.setText(score.get(3) + "");
            }
            if (score.containsKey(4)) {
                away4.setText(score.get(4) + "");
            }
            if (score.containsKey(5)) {
                away5.setText(score.get(5) + "");
            }
            if (score.containsKey(6)) {
                away6.setText(score.get(6) + "");
            }
            if (score.containsKey(7)) {
                away7.setText(score.get(7) + "");
            }
            if (score.containsKey(8)) {
                away8.setText(score.get(8) + "");
            }
            if (score.containsKey(9)) {
                away9.setText(score.get(9) + "");
            }
        }

        if (teamName.equals("home")) {
            if (score.containsKey(1)) {
                home1.setText(score.get(1) + "");
            }
            if (score.containsKey(2)) {
                home2.setText(score.get(2) + "");
            }
            if (score.containsKey(3)) {
                home3.setText(score.get(3) + "");
            }
            if (score.containsKey(4)) {
                home4.setText(score.get(4) + "");
            }
            if (score.containsKey(5)) {
                home5.setText(score.get(5) + "");
            }
            if (score.containsKey(6)) {
                home6.setText(score.get(6) + "");
            }
            if (score.containsKey(7)) {
                home7.setText(score.get(7) + "");
            }
            if (score.containsKey(8)) {
                home8.setText(score.get(8) + "");
            }
            if (score.containsKey(9)) {
                home9.setText(score.get(9) + "");
            }
        }
    }

    public void stealAction(ActionEvent event) {



        //Create the custom dialog.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Steal Options");
        alert.setHeaderText("Who Stole");

        ArrayList<ButtonType> buttons = new ArrayList<ButtonType>();
        ButtonType firstSteal = null;
        ButtonType firstCaught = null;
        ButtonType secondSteal = null;
        ButtonType secondCaught = null;
        ButtonType thirdSteal = null;
        ButtonType thirdCaught = null;
        // Set the button types.
        if (currentTeam.first != null) {
            firstSteal = new ButtonType(currentTeam.first.getName() + " stole second.");
            firstCaught = new ButtonType(currentTeam.first.getName() + " got caught stealing second.");
            buttons.add(firstSteal);
            buttons.add(firstCaught);
        }
        if (currentTeam.second != null) {
            secondSteal = new ButtonType(currentTeam.second.getName() + " stole third.");
            secondCaught = new ButtonType(currentTeam.second.getName() + " got caught stealing third.");
            buttons.add(secondSteal);
            buttons.add(secondCaught);
        }
        if (currentTeam.third != null) {
            thirdSteal = new ButtonType(currentTeam.third.getName() + " stole home.");
            thirdCaught = new ButtonType(currentTeam.third.getName() + " got caught stealing home.");
            buttons.add(thirdSteal);
            buttons.add(thirdCaught);
        }

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        buttons.add(buttonTypeCancel);
        alert.getButtonTypes().setAll(buttons);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == firstSteal) {
            try {
                currentTeam.steal("first", "safe", currentInning);
                window.lastOutcome = window.outcome;
                window.outcome = "steal";
                window.runOutcome();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.currentTeam = currentTeam;
        }
        if (result.get() == secondSteal) {
            try {
                currentTeam.steal("second", "safe", currentInning);
                window.lastOutcome = window.outcome;
                window.outcome = "steal";
                window.runOutcome();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.currentTeam = currentTeam;
        }
        if (result.get() == thirdSteal) {
            try {
                currentTeam.steal("third", "safe", currentInning);
                window.lastOutcome = window.outcome;
                window.outcome = "steal";
                window.runOutcome();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.currentTeam = currentTeam;
        }
        if (result.get() == firstCaught) {
            try {
                currentTeam.steal("first", "out", currentInning);
                window.lastOutcome = window.outcome;
                window.caughtStealing();
                window.outcome = "steal";
                window.runOutcome();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.currentTeam = currentTeam;
        }
        if (result.get() == secondCaught) {
            try {
                currentTeam.steal("second", "out", currentInning);
                window.lastOutcome = window.outcome;
                window.caughtStealing();
                window.outcome = "steal";
                window.runOutcome();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.currentTeam = currentTeam;
        }
        if (result.get() == thirdCaught) {
            try {
                currentTeam.steal("third", "out", currentInning);
                window.lastOutcome = window.outcome;
                window.caughtStealing();
                window.outcome = "steal";
                window.runOutcome();
            } catch (Exception e) {
                e.printStackTrace();
            }
            window.currentTeam = currentTeam;
        }

    }

    public void updateGridScores(int away, int home) {
        awayR.setText(away + "");
        homeR.setText(home + "");
    }

    public void updateGridHits(int away, int home) {
        awayH.setText(away + "");
        homeH.setText(home + "");
    }

    public void updateToday(int hits, int outs) {
        int ab = hits + outs;
        todayText.setText("Today: " + hits + "-" + ab);
    }

    public void updateSeasonStats(double avg, int hr, int rbi) {
        DecimalFormat format = new DecimalFormat("0.000");
        avgText.setText("AVG: " + format.format(avg));
        hrText.setText("HR: " + hr);
        rbiText.setText("RBI: " + rbi);
    }

    public void updateScoreColor(Team currentTeam) {
        if (homeScore.getText().contains(currentTeam.getName())) {
            homeScore.setFill(Color.BLUE);
            awayScore.setFill(Color.BLACK);
        }
        else {
            awayScore.setFill(Color.BLUE);
            homeScore.setFill(Color.BLACK);
        }
    }
}
