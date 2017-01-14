package optionpricer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * This is the controller for this application
 */

public class OptionPricer extends Application {
    
    BorderPane root = new BorderPane();
    
    @Override
    public void start(Stage primaryStage) {
        
        Scene scene = new Scene(root, 800, 500, Color.WHITE);
        
        // Menu Bar at the top
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        root.setTop(menuBar);
        
        // Menu Item
        Menu algorithmMenu = new Menu("Algorithm");
        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setOnAction(event->{
            displayPopup();
        });
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent->Platform.exit());
        algorithmMenu.getItems().addAll(newMenuItem, new SeparatorMenuItem(), exitMenuItem);
        
        // Include Menu Item to Menu Bar
        menuBar.getMenus().addAll(algorithmMenu);
        
        // Input Form for taking values from the user
        root.setLeft(addGrid());
        
        primaryStage.setTitle("Option Pricer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Pop-up window for users to decide which algorithm to add.
     */
    public void displayPopup(){
        
        Stage newStage = new Stage();
        VBox vbox = new VBox();
        
        CheckBox cb = new CheckBox("Numerical Integration");
        vbox.getChildren().add(cb);
        
        Button btn = new Button("Confirm");
        btn.setOnAction(event->{
            if (cb.isSelected()) {
                Algorithm.addAlgorithm(new NumericalIntegration());
                root.setLeft(addGrid());
            }
            newStage.close();
        });
        
        vbox.getChildren().add(btn);
        
        Scene scene = new Scene(vbox, 300, 300);
        newStage.setScene(scene);
        newStage.show();
    }
    
    /**
     * 
     * Input Form for taking values from the user
     * 
     * @return GridPane
     * 
     */
    public GridPane addGrid(){
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        
        // Instruction
        Text title = new Text("Please input values: ");
        
        // ComboBox for selecting Option Style
        Label optionStyle = new Label("Option Style:");
        final ComboBox optionStyleComboBox = new ComboBox();
        optionStyleComboBox.getItems().addAll(
            "European Call",
            "European Put",
            "American Call",
            "American Put",
            "Asian Call",
            "Asian Put"
        );
        
        // ComboBox for selecting an algorithm
        Label algorithm = new Label("Algorithm:");
        ComboBox algorithmComboBox = new ComboBox();
        List<String> list = new ArrayList<String>();
        for (Algorithm a : Algorithm.list) {
            list.add(a.name); // construct a drop-down list from static member of Algorithm class
        }
        ObservableList<String> observableList = FXCollections.observableList(list);
        algorithmComboBox.setItems(observableList);
        
        // Business logic to display algorithms available according to user choice of Option Style
        optionStyleComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                
                // If user chooses American Put, Black-Sholes should not be displayed.
                if (t1.equals("American Put")) {
                    
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).equals("Black-Scholes Formula")) {
                            list.remove(i);
                        }
                    }
                    
                // If user chooses any Asian style, only Simluation should remain.
                } else if (t1.contains("Asian")) {
                    
                    for (Iterator<String> iter = list.listIterator(); iter.hasNext(); ) {
                        String s = iter.next();
                        if (s.equals("Black-Scholes Formula") || s.equals("Binomial Tree") || s.equals("Numerical Integration")) {
                            iter.remove();
                        }
                    }
                    
                // If user chooses European style, every algorithm should be displayed.
                } else {
                    list.clear();
                    for (Algorithm a : Algorithm.list) {
                        list.add(a.name);
                    }
                }
                
                ObservableList<String> observableList = FXCollections.observableList(list);
                algorithmComboBox.setItems(observableList);
            }
        });
        
        // The form content
        Label stockPrice = new Label("Stock Price:");
        TextField stockPriceTextField = new TextField();
        Label strikePrice = new Label("Strike Price: ");
        TextField strikePriceTextField = new TextField();
        Label termInYears = new Label("Term in Years:");
        TextField termInYearsTextField = new TextField();
        Label interestRate = new Label("Interest Rate: ");
        TextField interestRateTextField = new TextField();
        Label volatility = new Label("Volatility: ");
        TextField volatilityTextField = new TextField();
        
        // Text for displaying a result message
        final Text actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        
        // Button
        Button btn = new Button("Calculate!");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        btn.setOnAction(event ->              
        {
            String optionStyleChosen = optionStyleComboBox.getValue().toString();
            String algorithmChosen = algorithmComboBox.getValue().toString();
            
            String message = "";
            Map graphData = new LinkedHashMap();
            
            try {
                double a1 = Double.parseDouble(stockPriceTextField.getText());
                double a2 = Double.parseDouble(strikePriceTextField.getText());
                double a3 = Double.parseDouble(termInYearsTextField.getText());
                double a4 = Double.parseDouble(interestRateTextField.getText());
                double a5 = Double.parseDouble(volatilityTextField.getText());
                
                Calculation cal = new Calculation(optionStyleChosen, algorithmChosen, a1, a2, a3, a4, a5);
                message = cal.getMessage();
                graphData = cal.getGraphData();

            } catch (NumberFormatException e) {
                message = e.getMessage();
            }
            
            actiontarget.setText(message); // display message
            root.setCenter(displayGraph(graphData)); // display graph
        });
        
        // Layout
        grid.add(title,0,0,2,1);
        grid.add(optionStyle,0,1);
        grid.add(optionStyleComboBox,1,1);
        grid.add(algorithm,0,2);
        grid.add(algorithmComboBox,1,2);
        grid.add(stockPrice,0,3);
        grid.add(stockPriceTextField, 1,3);
        grid.add(strikePrice,0,4);
        grid.add(strikePriceTextField,1,4);
        grid.add(termInYears,0,5);
        grid.add(termInYearsTextField,1,5);
        grid.add(interestRate,0,6);
        grid.add(interestRateTextField,1,6);
        grid.add(volatility,0,7);
        grid.add(volatilityTextField,1,7);
        grid.add(hbBtn,1,8);
        grid.add(actiontarget,1,9);
        grid.setGridLinesVisible(false);
        
        return grid;
    }
    
    // Display a graph taking the calculated results
    LineChart displayGraph(Map graphData) {
        
        // X, Y axis definition
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Strike Price");
        yAxis.setLabel("Option Price");
        
        final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        
        XYChart.Series series;
        series = new XYChart.Series();
        series.setName("Option Price vs. Strike Price");
        
        // Populate the charts with input data
        graphData.forEach( (k,v) -> series.getData().add(new XYChart.Data(k,v)));

        lineChart.getData().add(series);
        
        return lineChart;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}