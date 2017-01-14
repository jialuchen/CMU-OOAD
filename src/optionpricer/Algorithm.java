package optionpricer;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {
    
    static List<Algorithm> list = new ArrayList<Algorithm>(){{
        add(new BSFormula());
        add(new Simulation());
        add(new BinomialTree());
    }};
    
    static List<Algorithm> ready = new ArrayList<Algorithm>(){{
        add(new NumericalIntegration());
    }};
    
    String name;
    
    double currentStockPrice;
    double strikePrice;
    double term;
    double riskFreeInterestRate;
    double volatility;
    
    public Algorithm(){}
    
    public Algorithm(double currentStockPrice, double strikePrice, double term, double riskFreeInterestRate, double volatility){
        this.currentStockPrice = currentStockPrice;
        this.strikePrice = strikePrice;
        this.term = term;
        this.riskFreeInterestRate = riskFreeInterestRate;
        this.volatility = volatility;
    }
    
    public static void addAlgorithm(Algorithm newAlgorithm) {
        list.add(newAlgorithm);
    }
}
