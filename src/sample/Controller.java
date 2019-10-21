package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.print.PrinterException;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller extends Application implements Initializable {

    Double energA, energB;
    Double FWHM, fwhmA, fwhmB;

    double Ara, Ath, Ak, Raeq, D, Hex, Hin, Iy, Ialpha; //RadFactors Variables

    @FXML
    MenuItem encal, fwhmcal, effcal;

    @FXML
    VBox energyBox, fwhmBox, mainBox, rightBox, effBox, enrgyChartBox;
    @FXML
    LineChart<Number, Number> lineChart;
    @FXML
    LineChart<Number, Number> lineChart2;
    @FXML
    NumberAxis lineXaxis, lineYaxis, lineXaxis1, lineYaxis1;
    @FXML
    Label tooltip, startPoint, endPoint;
    @FXML
    AnchorPane chartsAnchor;
    @FXML
    TitledPane title;
    @FXML
    TextArea report;
    @FXML
    ImageView save, result, minimize, show, enlargIcon;
    @FXML
    Label fhm, fwhm;
    @FXML
    TitledPane repPane, orgPane;
    @FXML
    Button done, reset;
    @FXML
    Label centroid;
    @FXML
    Label energycal;
    @FXML
    ToolBar toolBar;
    @FXML
    Pane bottom;
    @FXML
    ScrollPane mainScrol, reportParent;
    NumberAxis lineXaxis2 = new NumberAxis();
    NumberAxis lineYaxis2 = new NumberAxis();
    LineChart lineChart3 = new LineChart(lineXaxis2, lineYaxis2);

    NumberAxis lineXaxis3 = new NumberAxis();
    NumberAxis lineYaxis3 = new NumberAxis();
    LineChart lineChart4 = new LineChart(lineXaxis3, lineYaxis3);
    Double effeciency;
    String testName, LT, ST;
    XYChart.Series series = new XYChart.Series();
    XYChart.Series series2 = new XYChart.Series();
    XYChart.Series series3 = new XYChart.Series();
    XYChart.Series series4 = new XYChart.Series();
    XYChart.Series abnormalSeries = new XYChart.Series();
    XYChart.Series abnormalSeries2 = new XYChart.Series();
    XYChart.Series abnormalSeries3 = new XYChart.Series();
    ArrayList<Number> channels = new ArrayList<>();
    ArrayList<Number> counts = new ArrayList<>();
    ArrayList<Number> backgrounds  = new ArrayList<>();
    ArrayList<Number> nums = new ArrayList<>();
    ArrayList<Number> countsSums = new ArrayList<>();
    ArrayList<Number> peakEnergies = new ArrayList<>();
    ArrayList<Number> peakChannels = new ArrayList<>();
    ArrayList<Number> peakCounts = new ArrayList<>();
    ArrayList<Number> peak2Counts = new ArrayList<>();
    ArrayList<Number> peak2Channels = new ArrayList<>();
    ArrayList<Number> startChannels = new ArrayList<>();
    ArrayList<Number> endChannels = new ArrayList<>();
    ArrayList<Number> startCounts = new ArrayList<>();
    ArrayList<Number> endCounts = new ArrayList<>();
    ArrayList<Number> cents = new ArrayList<>();
    ArrayList<Number> areas = new ArrayList<>();
    ArrayList<Number> integrals = new ArrayList<>();
//    ArrayList<Number> peakEnergies = new ArrayList<>();
    ArrayList<Number> peakFWHM = new ArrayList<>();
    ArrayList<Number> fwhmAs = new ArrayList<>();
    ArrayList<Number> peakEFF = new ArrayList<>();
    Double eneCal = 0.0;
    double meanValue = 0;
    int setPoint = 0;
    double max = 0;
    XYChart.Series endLine;
    XYChart.Series startLine;
    XYChart.Series endLine2;
    XYChart.Series startLine2;
    double startCount = 0;
    double startEnergyCal = 0;
    double startChannel = 0;
    double endChannel = 0;
    double endEnergyCal = 0;
    double endCount = 0;
    double peakCount = 0;
    double peackEnergyCal = 0;
    double peakChannel = 0;
    double peak2Count = 0;
    double peack2EnergyCal = 0;
    double peak2Channel = 0;
    Boolean setPeak = false;
    double midCounts = 0;
    double chanel = 0;
    double background = 0;
    double countsSum = 0;
    double Area = 0;
    double cent = 0;
    double Num = 0;
    int n = 0;
    double SegXX, SegXY, SegXX2, SegX2Y, SegX2X2;
    double EnergyCalibration = 0;
    int iPeak = 0;
    Stage window;
    @FXML
    private AnchorPane Content;

    public static void main(String[] args) {
        launch(args);
    }

    public static String centerString(int width, String s) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setMinWidth(800);
        window.setMinHeight(600);
        window.heightProperty().addListener((observableValue, number, t1) -> {
            resize();
        });

        window.widthProperty().addListener((observableValue, number, t1) -> {
            resize();
        });
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        encal.setDisable(true);
        fwhmcal.setDisable(true);
        effcal.setDisable(true);

        energyBox.setOpacity(0.4);
        energyBox.setDisable(true);

        fwhmBox.setOpacity(0.4);
        fwhmBox.setDisable(true);
        enrgyChartBox.setOpacity(0.4);
        enrgyChartBox.setDisable(true);
        effBox.setOpacity(0.4);
        effBox.setDisable(true);
        
        

        tooltip.setContentDisplay(ContentDisplay.CENTER);
        tooltip.setAlignment(Pos.CENTER);


        lineXaxis.setLabel("Channel");
        lineYaxis.setLabel("Counts");
        lineXaxis1.setLabel(" ");
        lineYaxis1.setLabel(" ");
        lineXaxis2.setLabel("Channel");
        lineYaxis2.setLabel("Counts");

        lineXaxis3.setLabel("Energy Cal.");
        lineYaxis3.setLabel("Counts");

        lineChart.setLegendVisible(false);
        lineChart2.setLegendVisible(false);
        lineChart3.setLegendVisible(false);
        lineChart4.setLegendVisible(false);

        Zoom zoom = new Zoom(lineChart, chartsAnchor);


        show.setOnMouseClicked(mousePressed -> {

            show();
        });

        //done button
        done.setOnMouseClicked(mousePressed -> {

            if (startCount == 0 || endChannel == 0) {

            } else {
                if (!setPeak) {
                    peakCount = 0;
                    peakChannel = 0;
                    peak2Count = 0;
                    peak2Channel = 0;
                    countsSum = 0.0;
                    Area = 0.0;
                    Num = 0.0;
                    if (startChannel > endChannel) {
                        double tmp = startChannel;
                        startChannel = endChannel;
                        endChannel = tmp;
                    }
                    int pos = 0;
                    for (int i = (int) startChannel; i < (int) endChannel; i++) {
                        if (counts.get(i).doubleValue() > peakCount) {
                            peakCount = counts.get(i).doubleValue();
                            peakChannel = channels.get(i).doubleValue();

                            pos = i;
                        }
                    }
                    for (int i = (int) startChannel; i < (int) endChannel; i++) {
                        if (counts.get(i).doubleValue() > peak2Count && counts.get(i).doubleValue() != peakCount) {
                            peak2Count = counts.get(i).doubleValue();
                            peak2Channel = channels.get(i).doubleValue();

                        }
                    }
                    peakChannels.add(peakChannel);
                    peak2Channels.add(peak2Channel);
                    peakCounts.add(peakCount);
                    peak2Counts.add(peak2Count);
                    startChannels.add(startChannel);
                    endChannels.add(endChannel);
                    startCounts.add(startCount);
                    endCounts.add(endCount);
                    encal.setDisable(false);
                    energyBox.setOpacity(1);
                    energyBox.setDisable(false);


                    XYChart.Data data1 = new XYChart.Data(channels.get(pos - 1), counts.get(pos - 1));

                    XYChart.Data data2 = new XYChart.Data(channels.get(pos), counts.get(pos));

                    XYChart.Data data3 = new XYChart.Data(channels.get(pos + 1), counts.get(pos + 1));


                    abnormalSeries = new XYChart.Series();
                    abnormalSeries.getData().add(data1);
                    abnormalSeries.getData().add(data2);
                    abnormalSeries.getData().add(data3);

                    abnormalSeries2 = new XYChart.Series();
                    abnormalSeries2.getData().add(data1);
                    abnormalSeries2.getData().add(data2);
                    abnormalSeries2.getData().add(data3);

                    abnormalSeries3 = new XYChart.Series();
                    abnormalSeries3.getData().add(data1);
                    abnormalSeries3.getData().add(data2);
                    abnormalSeries3.getData().add(data3);


                    lineChart2.getData().add(abnormalSeries2);
                    lineChart3.getData().add(abnormalSeries3);
                    lineChart.getData().add(abnormalSeries);

                    abnormalSeries.getNode().setStyle("-fx-stroke: #7F0000; -fx-stroke-width: 3.5px;");
                    abnormalSeries2.getNode().setStyle("-fx-stroke: #7F0000; -fx-stroke-width: 3.5px;");
                    abnormalSeries3.getNode().setStyle("-fx-stroke: #7F0000; -fx-stroke-width: 3.5px;");

                    setPeak = true;
                } else {

                }
            }

            double segX = 0.0, segX2 = 0.0, segXY = 0.0, segY = 0.0, segX3 = 0.0, segX2Y = 0.0, segX4 = 0.0;
            System.out.println(startChannel + "           " + endChannel);
            for (int j = (int) startChannel; j < (int) endChannel + 1; j++) {
                countsSum += counts.get(j).doubleValue();
                Num += counts.get(j).doubleValue() * channels.get(j).doubleValue();


                segX += channels.get(j).doubleValue();
                segX2 += Math.pow(channels.get(j).doubleValue(), 2);

                segXY += channels.get(j).doubleValue() * counts.get(j).doubleValue();
                segY += counts.get(j).doubleValue();

                segX3 += Math.pow(channels.get(j).doubleValue(), 3);

                segX2Y += Math.pow(channels.get(j).doubleValue(), 2) * counts.get(j).doubleValue();

                segX4 += Math.pow(channels.get(j).doubleValue(), 4);
                n++;
            }


            nums.add(Num);
            countsSums.add(countsSum);
            calculation();

            SegXX = segX2 - (Math.pow(segX, 2) / n);
            SegXY = segXY - (segX * segY / n);
            SegXX2 = segX3 - (segX * segX2 / n);
            SegX2Y = segX2Y - (segX2 * segY / n);
            SegX2X2 = segX4 - (Math.pow(segX2, 2) / n);

            System.out.println("البسط: " + ((segX2Y)));
            fwhmA = ((SegX2Y * SegXX) - (SegXY * SegXX2)) / ((SegXX * SegX2X2) - Math.pow(SegXX2, 2));
            fwhmAs.add(Math.abs(fwhmA));


        });
    }

    public void openFile() {


        Stage stage = (Stage) Content.getScene().getWindow();
        KeyCombination cntrlR = new KeyCodeCombination(KeyCode.R, KeyCodeCombination.CONTROL_DOWN);
        lineChart.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (cntrlR.match(event)) {
                    reset();
                }
            }
        });
        reset.setOnMouseClicked(mousePressed -> {
            reset();
        });


        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DAT files (*.DAT)", "*.DAT");

        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {

            encal.setDisable(true);
            fwhmcal.setDisable(true);
            effcal.setDisable(true);

            peakChannels.clear();
            peak2Channels.clear();
            startChannels.clear();
            endChannels.clear();
            startCounts.clear();
            endCounts.clear();
            peakEnergies.clear();
            peakCounts.clear();
            peak2Counts.clear();
//            peakEnergies.clear();
            counts.clear();
            channels.clear();
            peakFWHM.clear();
            peakEnergies.clear();
            nums.clear();
            countsSums.clear();

            int count = 0;
            iPeak = 0;

            try {
                // get LT and ST
                String line = Files.readAllLines(file.toPath()).get(0);
                String[] splited = line.split("\\s+");

                testName = splited[0];
                LT = splited[2];
                ST = splited[3].substring((1 + splited[3].indexOf("="))) + " " + splited[4];


                title.setText(testName + "                      " + "LT= " + LT + "    " + "ST= " + ST);

                // get ALL DATA
                count = Files.readAllLines(file.toPath()).size();
                int channel = 1;
                for (int i = 1; i < count; i++) {

                    line = Files.readAllLines(file.toPath()).get(i);
                    splited = line.split("\\s+");

                    for (int j = 0; j < splited.length; j++) {
                        if (!splited[j].isEmpty()) {
                            try {
                                counts.add(Double.parseDouble(splited[j]));
//                                System.out.println(splited[j] + " -- " + channel);
                                channels.add(channel);
                                channel++;
                            } catch (Exception e) {
                            }
                        }
                    }

                }
                counts.remove(0);
                channels.remove(0);


                plotLine();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        setReport("");

    }

    private void lineTooltip() {

        for (int i = 0; i < lineChart.getData().size(); i++) {

            for (XYChart.Data<Number, Number> entry : lineChart.getData().get(i).getData()) {
                entry.getNode().setCursor(Cursor.HAND);

                entry.getNode().setOnMouseEntered(mousePressed -> {

                    if (peakEnergies.size() > 0) {
                        tooltip.setText(lineXaxis.getLabel() + ": " + entry.getXValue() + "\n" +
                                "Energy Cal.: " + pointEnergyCal(entry.getXValue().doubleValue()) + "\n" +
                                lineYaxis.getLabel() + ": " + entry.getYValue());
                        Tooltip t = new Tooltip(entry.getXValue() + " , " + entry.getYValue().toString());
                        Tooltip.install(entry.getNode(), t);
                    } else {

                        tooltip.setText(lineXaxis.getLabel() + ": " + entry.getXValue() + "\n" +
                                lineYaxis.getLabel() + ": " + entry.getYValue());
                        Tooltip t = new Tooltip(entry.getXValue() + " , " + entry.getYValue().toString());
                        Tooltip.install(entry.getNode(), t);
                    }
                });

                entry.getNode().setOnMouseExited(mousePressed -> {
                    tooltip.setText("");
                });

                entry.getNode().setOnMouseClicked(mousePressed -> {
                    if (setPoint == 0) {
                        startPoint.setText(lineXaxis.getLabel() + ": " + entry.getXValue() + "\n" +
                                lineYaxis.getLabel() + ": " + entry.getYValue());

                        if (peakEnergies.size() > 0) {
                            for (int j = 0; j < peakEnergies.size(); j++) {
                                startEnergyCal += peakEnergies.get(j).doubleValue() * entry.getYValue().doubleValue();
                            }
                            startPoint.setText(lineXaxis.getLabel() + ": " + entry.getXValue() + "\n" +
                                    "Energy Cal.: " + pointEnergyCal(entry.getXValue().doubleValue()) + "\n" +
                                    lineYaxis.getLabel() + ": " + entry.getYValue());

                        }
                        startCount = entry.getYValue().doubleValue();
                        startChannel = entry.getXValue().doubleValue();

                        startLine = new XYChart.Series();
                        startLine2 = new XYChart.Series();

                        XYChart.Data point1 = new XYChart.Data(entry.getXValue(), 0);
                        XYChart.Data point2 = new XYChart.Data(entry.getXValue(), max);
                        startLine.getData().add(point1);
                        startLine.getData().add(point2);
                        startLine2.getData().add(point1);
                        startLine2.getData().add(point2);
                        lineChart.getData().add(startLine);
                        lineChart3.getData().add(startLine2);
                        startLine.getNode().setStyle("-fx-stroke: #ffc40d; -fx-stroke-width: 3px;");
                        startLine2.getNode().setStyle("-fx-stroke: #ffc40d; -fx-stroke-width: 3px;");


                        setPoint++;
                    } else if (setPoint == 1) {
                        endPoint.setText(lineXaxis.getLabel() + ": " + entry.getXValue() + "\n" +
                                lineYaxis.getLabel() + ": " + entry.getYValue());

                        if (peakEnergies.size() > 0) {

                            for (int j = 0; j < peakEnergies.size(); j++) {
                                endEnergyCal += peakEnergies.get(j).doubleValue() * entry.getYValue().doubleValue();
                            }
                            endPoint.setText(lineXaxis.getLabel() + ": " + entry.getXValue() + "\n" +
                                    "Energy Cal.: " + pointEnergyCal(entry.getXValue().doubleValue()) + "\n" +
                                    lineYaxis.getLabel() + ": " + entry.getYValue());

                        }

                        endCount = entry.getYValue().doubleValue();
                        endChannel = entry.getXValue().doubleValue();

                        endLine = new XYChart.Series();
                        endLine2 = new XYChart.Series();

                        XYChart.Data point1 = new XYChart.Data(entry.getXValue(), 0);
                        XYChart.Data point2 = new XYChart.Data(entry.getXValue(), max);
                        endLine.getData().add(point1);
                        endLine.getData().add(point2);
                        endLine2.getData().add(point1);
                        endLine2.getData().add(point2);
                        lineChart.getData().add(endLine);
                        lineChart3.getData().add(endLine2);
                        endLine.getNode().setStyle("-fx-stroke: #00a300; -fx-stroke-width: 3px;");
                        endLine2.getNode().setStyle("-fx-stroke: #00a300; -fx-stroke-width: 3px;");


                        setPoint++;
                    }
                });
            }
        }
    }

    private void plotLine() {


        series = new XYChart.Series();
        series2 = new XYChart.Series();
        series3 = new XYChart.Series();

        lineChart.getData().clear();
        lineChart2.getData().clear();
        lineChart3.getData().clear();

        for (int i = 0; i < counts.size(); i++) {
            XYChart.Data data = new XYChart.Data(channels.get(i), counts.get(i));
            Circle rect = new Circle(15, Color.rgb(0, 0, 0, 0.0001));
            rect.setVisible(true);
            data.setNode(rect);

            series.getData().add(data);
            series2.getData().add(data);
            series3.getData().add(data);

        }

        lineChart2.getData().add(series2);
        lineChart3.getData().add(series3);
        lineChart.getData().add(series);
        series3.getNode().setStyle("-fx-stroke: #2b5797; -fx-stroke-width: 0.7px;");


        double sum = 0;
        max = 0;
        for (int i = 0; i < counts.size(); i++) {
            sum += counts.get(i).doubleValue();
            if (max < counts.get(i).doubleValue()) {
                max = counts.get(i).doubleValue();
            }
        }

        meanValue = sum / counts.size();


        lineTooltip();

    }

    private void setReport(String status) {
        report.clear();
        report.setText(testName + "                      " + "LT= " + LT + "    " + "ST= " + ST);

        report.appendText("\n\nPeaks:\n");


        report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s",
                "No.", "Start", "Peak1", "Peak2", "End"));
        report.appendText("\n");

        report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s",
                "",
                "ch" + "      " + "counts",
                "ch" + "      " + "counts",
                "ch" + "      " + "counts",
                "ch" + "      " + "counts"));

        report.appendText("\n");
        for (int i = 0; i < peakChannels.size(); i++) {
            report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s",
                    String.valueOf(i + 1),
                    startChannels.get(i) + "  " + startCounts.get(i),
                    peakChannels.get(i) + "  " + peakCounts.get(i),
                    peak2Channels.get(i) + "  " + peak2Counts.get(i),
                    endChannels.get(i) + "  " + endCounts.get(i)));
            report.appendText("\n");

        }

        // Print the list objects in tabular format.
        report.appendText("\n\n" + "Output Results:\n");
        String header = String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s",
                "No.", "Centroid", "Energy", "FWHM", "Integral", "Area", "Background", "Efficiency");
        report.appendText(header + "\n");
        if (status.equals("calculation")) {
            for (int i = 0; i < peakChannels.size(); i++) {
                report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s",
                        String.valueOf(i + 1),
                        String.valueOf((double) Math.round((Double) cents.get(i).doubleValue() * 10000d) / 10000d),
                        "-",
                        "-",
                        String.valueOf(integrals.get(i).doubleValue()),
                        String.valueOf(areas.get(i).doubleValue())
                        ,String.valueOf(backgrounds.get(i).doubleValue())
                        , "-"));
                report.appendText("\n");

            }
        } else if (status.equals("energy")) {
            for (int i = 0; i < peakChannels.size(); i++) {

                report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s",
                        String.valueOf(i + 1),
                        String.valueOf((double) Math.round((Double) cents.get(i).doubleValue() * 10000d) / 10000d),
                        String.valueOf((double) Math.round((Double) peakEnergies.get(i) * 10000d) / 10000d),
                        "-",
                        String.valueOf(integrals.get(i).doubleValue()),
                        String.valueOf(areas.get(i).doubleValue())
                        ,String.valueOf(backgrounds.get(i).doubleValue())
                        , "-"));
                report.appendText("\n");
            }
        } else if (status.equals("fwhm")) {
            for (int i = 0; i < peakChannels.size(); i++) {
                report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s",
                        String.valueOf(i + 1),
                        String.valueOf((double) Math.round((Double) cents.get(i).doubleValue() * 10000d) / 10000d),
                        String.valueOf((double) Math.round((Double) peakEnergies.get(i) * 10000d) / 10000d),
                        String.valueOf((double) Math.round((Double) peakFWHM.get(i) * 10000d) / 10000d),
                        String.valueOf(integrals.get(i).doubleValue()), String.valueOf(areas.get(i).doubleValue())
                        ,String.valueOf(backgrounds.get(i).doubleValue())
                        , "-"));
                report.appendText("\n");

            }
        } else if (status.equals("eff")) {
            if (peakFWHM.size() > 0) {
                for (int i = 0; i < peakChannels.size(); i++) {
                    report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s %-20s",
                            String.valueOf(i + 1),
                            String.valueOf((double) Math.round((Double) cents.get(i).doubleValue() * 10000d) / 10000d),
                            String.valueOf((double) Math.round((Double) peakEnergies.get(i) * 10000d) / 10000d),
                            String.valueOf((double) Math.round((Double) peakFWHM.get(i) * 10000d) / 10000d),
                            String.valueOf(integrals.get(i).doubleValue()), String.valueOf(areas.get(i).doubleValue())
                            ,String.valueOf(backgrounds.get(i).doubleValue())
                            , String.valueOf((double) Math.round((Double) peakEFF.get(i) * 10000d) / 10000d)));
                    report.appendText("\n");
                }
            } else {
                for (int i = 0; i < peakChannels.size(); i++) {
                    report.appendText(String.format("%-10s %-20s %-20s %-20s %-20s %-20s %-20s",
                            String.valueOf(i + 1),
                            String.valueOf((double) Math.round((Double) cents.get(i).doubleValue() * 10000d) / 10000d),
                            String.valueOf((double) Math.round((Double) peakEnergies.get(i) * 10000d) / 10000d),
                            "-",
                            String.valueOf(integrals.get(i).doubleValue()), String.valueOf(areas.get(i).doubleValue())
                            , String.valueOf((double) Math.round((Double) peakEFF.get(i) * 10000d) / 10000d)));
                    report.appendText("\n");
                }
            }
        }


        report.setFont(Font.font("monospace", FontWeight.NORMAL, 10));
        iPeak++;
    }

    public void saveReport() {

        if (testName != null) {
            String separator = System.lineSeparator();
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Chose path to save the report");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showSaveDialog(null);

            if (chooser.getSelectedFile() != null) {
                String path = chooser.getSelectedFile().getPath();
                try (PrintStream out = new PrintStream(new FileOutputStream(path + "/" + testName + "_output.DAT"))) {
                    String reporttxt = report.getText().replace("\n", separator);
                    out.print(reporttxt);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void calculation() {

        cents.clear();
        backgrounds.clear();
        integrals.clear();
        areas.clear();


        for (int i = 0; i < peakChannels.size(); i++) {

            midCounts = 0;
            chanel = 0;
            background = 0;
            Num = 0;
            Area = 0;
            EnergyCalibration = 0;

            midCounts = (startCounts.get(i).doubleValue() + endCounts.get(i).doubleValue()) / 2;
            chanel = endChannels.get(i).doubleValue() - startChannels.get(i).doubleValue();
            background = midCounts * chanel;
            backgrounds.add(background);


            Area = countsSums.get(i).doubleValue() - background;
            areas.add(Area);
            cent = nums.get(i).doubleValue() / countsSums.get(i).doubleValue();
            System.out.println("Num" + i + " :" + Num + "\n" +
                    "countsSum" + i + " :" + countsSum);

            cents.add(cent);

            integrals.add(countsSums.get(i).doubleValue());
        }


        setReport("calculation");

        if (peakEnergies.size() > 0) {
            energyCal(energA, energB);
        }
        if (peakFWHM.size() > 0) {
            FWHMCalc();
        }


    }

    public void exit() {
        Stage stage = (Stage) save.getScene().getWindow();

// Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Close Program");

        // Create a ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(new Label("Are you sure to close the program?"));
        dialog.getDialogPane().setContent(scrollPane);

        // Set the button types.
        ButtonType doneButton = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(doneButton, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == doneButton) {
                stage.close();
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

    }

    public void print() {
        JTextArea YourTextArea = new JTextArea(); //define new Swing JtextArea
        YourTextArea.setLineWrap(true); //set line wrap for YourTextArea - this will prevent too long lines to be cut - they will be wrapaed to next line
        YourTextArea.append(report.getText()); //append YourString to YourTextArea
        try {
            YourTextArea.print(); //this will display print dialog that will lead you to print contents of YourTextArea so in efect contents of YourString
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public void runProg() throws IOException {

        Stage stage = (Stage) Content.getScene().getWindow();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("executable file (*.exe)", "*.exe");

        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {

            String cmd = file.toString();
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(cmd);
        }
    }

    public void enlargeChart() {


        // New window (Stage)
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 400);
        Pane anchor = new Pane();
        Zoom zoom = new Zoom(lineChart3, anchor);

        anchor.getChildren().add(lineChart3);

        lineChart3.setTitle("Chart");


        root.getChildren().add(anchor);
        Stage newWindow = new Stage();
        newWindow.setTitle("Chart");
        newWindow.setScene(scene);
        newWindow.show();

        newWindow.widthProperty().addListener((obs, oldVal, newVal) -> {
            lineChart3.setPrefWidth(newWindow.getWidth());
        });

        newWindow.heightProperty().addListener((obs, oldVal, newVal) -> {
            lineChart3.setPrefHeight(newWindow.getHeight() - 20);
        });

    }

    public void energyCalDialog() {

//        peakEnergies.clear();
        peakEnergies.clear();
        if (peakChannels.size() > 0) {

            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Energy Calibration");

            // Set the button types.
            ButtonType doneButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(doneButton, ButtonType.CANCEL);


            // Create a ScrollPane
            ScrollPane scrollPane = new ScrollPane();

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 20, 20));


            TextField energyA = new TextField();
            TextField energyB = new TextField();


            grid.add(new Label("a:"), 0, 0);
            grid.add(energyA, 3, 0);

            grid.add(new Label("b:"), 0, 2);
            grid.add(energyB, 3, 2);


            scrollPane.setContent(grid);


            dialog.getDialogPane().setContent(scrollPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == doneButton) {

                    if (!energyA.getText().isEmpty() && !energyB.getText().isEmpty()) {
                        energA = Double.parseDouble(energyA.getText());
                        energB = Double.parseDouble(energyB.getText());
//                        System.out.println("asdddddddddddddddddddddd      " + peakEnergies.size());

                        energyCal(energA, energB);
                    }
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();


        }
    }

    public void FWHMCalc() {

        peakFWHM.clear();
//        double x = 0.0;         //x : channels
//        double y = 0.0;         //y : counts
//        double x2y = 0.0;        //z : x^2*y
//        double xx = 0.0;       //xx : x*x
//        double xy = 0.0;       //xy : x*y
//        double xx2 = 0.0;      //xx2 : x*x^2
//        double x2x2 = 0.0;     //x2x2 : x^2 * x^2
//
//        for (int i = 0; i < peakChannels.size(); i++) {
////            x = peakChannels.get(i).doubleValue();              //sum(x)
////            y = peakCounts.get(i).doubleValue();                //sum(y)
////            x2y = Math.pow(peakChannels.get(i).doubleValue(), 2) * peakCounts.get(i).doubleValue();    //sum(x^2 * y)
////            xx = peakChannels.get(i).doubleValue() * peakChannels.get(i).doubleValue();             //sum(xx)
////            xy = peakChannels.get(i).doubleValue() * peakCounts.get(i).doubleValue();               //sum(x*y)
////            xx2 = peakChannels.get(i).doubleValue() * Math.pow(peakCounts.get(i).doubleValue(), 2);  //sum(x*x^2)
////            x2x2 = Math.pow(peakChannels.get(i).doubleValue(), 2) * Math.pow(peakChannels.get(i).doubleValue(), 2);   //sum(x^2 * x^2)
////            fwhmA = ((x2y * xx) - (xy * xx2)) / ((xx * x2x2) - Math.pow(xx2, 2));
////            FWHM = ((1 / (Math.sqrt(2 * fwhmA))) * 2.355) / 2;
////
//
//            System.out.println(fwhmAs.get(i).doubleValue());
//            FWHM = ((1 / (Math.sqrt(2 * fwhmAs.get(i).doubleValue()))) * 2.355) / 2;
//
//            peakFWHM.add(FWHM);
//        }


        File file = getExcelFilePath("Choose FWHM excel file");
        fwhmA = getCellfromExcel(file, 0, 70, "K");
        fwhmB = getCellfromExcel(file, 0, 70, "M");
        for (int i = 0; i < peakChannels.size(); i++) {

            FWHM = fwhmA * peakEnergies.get(i).doubleValue() + fwhmB;
            peakFWHM.add(FWHM);

        }

        setReport("fwhm");
    }

    public void EFFCalc() {
        peakEFF.clear();

        double effA, effB;

        File file = getExcelFilePath("Choose Efficiency excel file");
        effA = getCellfromExcel(file, 0, 10, "j");
        effB = getCellfromExcel(file, 0, 10, "k");

        for (int i = 0; i < peakChannels.size(); i++) {

            peakEFF.add(effA * Math.pow(peakEnergies.get(i).doubleValue(), effB));
        }

        setReport("eff");


    }

    public void energyCal(Double energA, Double energB) {
        peakEnergies.clear();
        for (int i = 0; i < peakChannels.size(); i++) {
            EnergyCalibration = (cents.get(i).doubleValue() * energA) + energB;
            peakEnergies.add(EnergyCalibration);
        }

        setReport("energy");
        fwhmcal.setDisable(false);
        fwhmBox.setOpacity(1.0);
        fwhmBox.setDisable(false);
        enrgyChartBox.setOpacity(1.0);
        enrgyChartBox.setDisable(false);

        effcal.setDisable(false);
        effBox.setOpacity(1.0);
        effBox.setDisable(false);
    }

    public double pointEnergyCal(double channel) {
        return (channel * energA) + energB;
    }

    public void minim() {
        repPane.setVisible(false);
        orgPane.setVisible(true);
    }

    public void show() {
        repPane.setVisible(true);
        orgPane.setVisible(false);
    }

    public void result() {
        // New window (Stage)
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 400);
        TextArea rep = new TextArea();
        rep.setText(report.getText());
        rep.setEditable(false);
        rep.setFont(Font.font("monospace", FontWeight.NORMAL, 12));
        root.getChildren().add(rep);
        Stage newWindow = new Stage();
        newWindow.setTitle("report");
        newWindow.setScene(scene);
        newWindow.show();
    }

    public void resize() {


        //TopBar resize:
        toolBar.setPrefWidth(window.getWidth() - 200);

        //Main Chart resize:
        mainScrol.setPrefSize(window.getWidth() - 200, window.getHeight() - 325);
        mainBox.setPrefSize(window.getWidth() - 210, window.getHeight() - 370);
        title.setPrefSize(window.getWidth() - 210, window.getHeight() - 370);
        chartsAnchor.setPrefSize(window.getWidth() - 210, window.getHeight() - 370);
        lineChart.setPrefSize(window.getWidth() - 210, window.getHeight() - 370);
        enlargIcon.setTranslateX(lineChart.getPrefWidth() - 30);


        //Bottom Pane resize:
        reportParent.setPrefWidth(window.getWidth() - 10);
        bottom.setPrefWidth(reportParent.getPrefWidth());
        bottom.setPrefHeight(200);
        repPane.setPrefWidth(reportParent.getPrefWidth());
        orgPane.setPrefWidth(reportParent.getPrefWidth());
        orgPane.setPrefHeight(bottom.getHeight());
        orgPane.setPrefHeight(repPane.getHeight());
        lineChart2.setPrefHeight(bottom.getPrefHeight() - 20);
        reportParent.setLayoutY(window.getHeight() - 260);


        //Right Pane resize:
        rightBox.setLayoutX(mainScrol.getPrefWidth() + 10);


    }

    private void reset() {
        if (setPoint == 2) {
            setPeak = false;
            endChannel = 0;
            endCount = 0;
            endPoint.setText("");
            lineChart.getData().remove(endLine);
            lineChart3.getData().remove(endLine2);
            setPoint--;
        } else if (setPoint == 1) {
            setPeak = false;
            startChannel = 0;
            startCount = 0;
            startPoint.setText("");
            lineChart.getData().remove(startLine);
            lineChart3.getData().remove(startLine2);
            setPoint--;
        }

    }

    public File getExcelFilePath(String title) {
        Stage stage = (Stage) Content.getScene().getWindow();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");

        final FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle(title);
        return fileChooser.showOpenDialog(stage);


    }

    public double getCellfromExcel(File file, int sheet, int rowNumber, String columnName) {

        if (file != null) {

            try {
                Workbook wb = new XSSFWorkbook(file.getPath());
                return wb.getSheetAt(sheet).getRow(rowNumber - 1).getCell(CellReference.convertColStringToIndex(columnName)).getNumericCellValue();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return 0.0;

    }

    public void radFactorsCal() {


        File file = getExcelFilePath("Choose Activity excel file");
        Ara = getCellfromExcel(file, 1, 15, "I");
        Ath = getCellfromExcel(file, 1, 20, "I");
        Ak = getCellfromExcel(file, 1, 21, "H");

        Raeq = Ara + 1.34 * Ath + 0.077 * Ak;
        D = 0.462 * Ara + 0.604 * Ath + 0.041 * Ak;
        Hex = (Ara / 370) + (Ath / 259) + (Ak / 4810);
        Hin = (Ara / 185) + (Ath / 259) + (Ak / 4810);
        Iy = (Ara / 150) + (Ath / 100) + (Ak / 1500);
        Ialpha = (Ara / 200);

        radFactorsReport();
    }
    
    public void radFactorsReport() {

        StackPane root = new StackPane();
        Scene scene = new Scene(root, 800, 400);
        TextArea rep = new TextArea();
        rep.setEditable(false);
        Button save = new Button("save");

        StackPane.setAlignment(save, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(save, new Insets(10));

        root.getChildren().add(rep);
        root.getChildren().add(save);
        Stage newWindow = new Stage();
        newWindow.setTitle("Radiological Factors");
        newWindow.setScene(scene);
        newWindow.show();


        save.setOnMouseClicked(mousePressed -> {

            String separator = System.lineSeparator();
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Chose path to save the report");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showSaveDialog(null);

            if (chooser.getSelectedFile() != null) {
                String path = chooser.getSelectedFile().getPath();
                try (PrintStream out = new PrintStream(new FileOutputStream(path + "/" + "Radiological Factors" + ".DAT"))) {
                    String reporttxt = rep.getText().replace("\n", separator);
                    out.print(reporttxt);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        });
        rep.appendText(centerString(120, "Radiological Factors"));
        String date = new SimpleDateFormat("yyyy/MM/dd").format(Calendar.getInstance().getTime());
        rep.appendText("\n"+centerString(120, date));

        rep.appendText("\n\n");

        rep.appendText(String.format("%-30s %-20s",
                "Nuclide name", "Av. Activity"));
        rep.appendText("\n");

        rep.appendText(String.format("%-30s %-20s",
                "Radium", Ara));
        rep.appendText("\n");

        rep.appendText(String.format("%-30s %-20s",
                "Thorium", Ath));
        rep.appendText("\n");

        rep.appendText(String.format("%-30s %-20s",
                "K-40", Ak));


        // Print the list objects in tabular format.
        rep.appendText("\n\n" + "The Radiological Factors are:\n");


        rep.appendText(String.format("%-20s %-20s %-20s %-20s %-20s %-20s",
                "Raeq(Bq/Kg)", "A.D.R(nGy/h)", "H_ex", "H_in", "Irg", "I_alpha"));
        rep.appendText("\n");
        rep.appendText(String.format("%-20s %-20s %-20s %-20s %-20s %-20s",
                Raeq, D, Hex, Hin, Iy, Ialpha));

        rep.setFont(Font.font("monospace", FontWeight.NORMAL, 10));

    }

    public void energyChart(){
        lineChart4.getData().clear();


        for (int i = 0; i < counts.size(); i++) {

            XYChart.Data data = new XYChart.Data(pointEnergyCal(channels.get(i).doubleValue()), counts.get(i));
            Circle rect = new Circle(15, Color.rgb(0, 0, 0, 0.0001));
            rect.setVisible(true);
            data.setNode(rect);


            series4.getData().add(data);

        }

        lineChart4.getData().add(series4);
        series4.getNode().setStyle("-fx-stroke: #2b5797; -fx-stroke-width: 0.7px;");



        // New window (Stage)
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 400);
        Pane anchor = new Pane();

        anchor.getChildren().add(lineChart4);

        lineChart4.setTitle("Energy Cal. Chart");
        Zoom zoom = new Zoom(lineChart4, anchor);


        root.getChildren().add(anchor);
        Stage newWindow = new Stage();
        newWindow.setTitle("Energy Cal. Chart");
        newWindow.setScene(scene);
        newWindow.show();

        newWindow.widthProperty().addListener((obs, oldVal, newVal) -> {
            lineChart4.setPrefWidth(newWindow.getWidth());
        });

        newWindow.heightProperty().addListener((obs, oldVal, newVal) -> {
            lineChart4.setPrefHeight(newWindow.getHeight() - 20);
        });
    }

}