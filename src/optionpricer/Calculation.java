package optionpricer;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class Calculation {
    
    String optionStyleChosen;
    String algorithmChosen;
    double stockPrice;
    double strikePrice;
    double termInYears;
    double interestRate;
    double volatility;
    
    public Calculation(String optionStyleChosen, String algorithmChosen, double stockPrice, double strikePrice, double termInYears, double interestRate, double volatility) {
        this.optionStyleChosen = optionStyleChosen;
        this.algorithmChosen = algorithmChosen;
        this.stockPrice = stockPrice;
        this.strikePrice = strikePrice;
        this.termInYears = termInYears;
        this.interestRate = interestRate;
        this.volatility = volatility;
    }
    
    public double getResult() {
        
        double result;
        
        if (algorithmChosen.equals("Black-Scholes Formula")) {
            
            BSFormula bsf = new BSFormula(stockPrice, strikePrice, termInYears, interestRate, volatility);
            
            if (optionStyleChosen.contains("Call")) {
                result = bsf.getCallOption();
            } else {
                result = bsf.getPutOption();
            }
        } else if (algorithmChosen.equals("Simulation")) {
            
            Simulation simulation = new Simulation(stockPrice, strikePrice, termInYears, interestRate, volatility);
            
            if (optionStyleChosen.contains("Call")) {
                result = simulation.getCallOption(500, 10000);
            } else {
                result = simulation.getPutOption(500, 10000);
            }
        } else if (algorithmChosen.equals("Binomial Tree")) {
            BinomialTree bt = new BinomialTree(stockPrice, strikePrice, termInYears, interestRate, volatility);
            
            if (optionStyleChosen.contains("Call")) {
                result = bt.getCallOption(500);
            } else {
                result = bt.getPutOption(500);
            }
        } else {
            NumericalIntegration ni = new NumericalIntegration(stockPrice, strikePrice, termInYears, interestRate, volatility);
            
            if (optionStyleChosen.contains("Call")) {
                result = ni.getCallOption();
            } else {
                result = ni.getPutOption();
            }
        }
        
        return result;
    }
    
    public String getMessage(){
        DecimalFormat formatter = new DecimalFormat("$0.00");
        String message = "The option price is: " + formatter.format(getResult());
        return message;
    }
    
    public Map getGraphData() {
        
        Calculation lower = new Calculation(optionStyleChosen, algorithmChosen, strikePrice, 0.8 * strikePrice, termInYears, interestRate, volatility);
        Calculation higher = new Calculation(optionStyleChosen, algorithmChosen, strikePrice, 1.2 * strikePrice, termInYears, interestRate, volatility);
        
        Map graphData = new LinkedHashMap();
        graphData.put(0.8 * strikePrice, lower.getResult());
        graphData.put(strikePrice, getResult());
        graphData.put(1.2 * strikePrice, higher.getResult());
        
        return graphData;
    }
}
