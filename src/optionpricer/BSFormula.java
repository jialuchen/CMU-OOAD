package optionpricer;

public class BSFormula extends Algorithm {
    
    double d1;
    double d2;
    
    public BSFormula() {
        super.name = "Black-Scholes Formula";
    }
    
    public BSFormula(double currentStockPrice, double strikePrice, double term, double riskFreeInterestRate, double volatility){
        super(currentStockPrice, strikePrice, term, riskFreeInterestRate, volatility);
        
        double e1 = Math.log(currentStockPrice/strikePrice);
        double e2 = (riskFreeInterestRate + Math.pow(volatility, 2)/2 ) * term;
        double e3 = volatility * Math.sqrt(term);
        
        this.d1 = (e1 + e2) / e3;
        
        this.d2 = this.d1 - e3;
        
    }
    
    public double getCallOption(){
       
        double e1 = currentStockPrice * StandardNormal.cumulative(d1);
        double e2 = strikePrice * Math.exp(-riskFreeInterestRate*term) * StandardNormal.cumulative(d2);
        
        return e1 - e2;
    }
    
    public double getPutOption(){
        
        double e1 = strikePrice * Math.exp(-riskFreeInterestRate*term) * StandardNormal.cumulative(-d2);
        double e2 = currentStockPrice * StandardNormal.cumulative(-d1);
        
        return e1 - e2;
    }
}
