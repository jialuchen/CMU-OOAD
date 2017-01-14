package optionpricer;

public class BinomialTree extends Algorithm {
    
    public BinomialTree() {
        super.name = "Binomial Tree";
    }
    
    public BinomialTree(double currentStockPrice, double strikePrice, double term, double riskFreeInterestRate, double volatility) {
        super(currentStockPrice, strikePrice, term, riskFreeInterestRate, volatility);
    }
    
    double getPutOption(int numIntervals) {
	int i, j;
	double deltaT	= term / numIntervals;
	double up		= 1.0 + riskFreeInterestRate * deltaT + (volatility*Math.sqrt(deltaT));
	double down		= 1.0 + riskFreeInterestRate * deltaT - (volatility*Math.sqrt(deltaT));
	double upProb	= 0.5;
	double downProb = 0.5;
	double binomValue;

        Price[][] binomialTree = new Price[numIntervals+1][];
	for (i = 0; i <= numIntervals; i++) {
            binomialTree[i] = new Price[i + 1];               
	}
        
        for (i = 0; i <= numIntervals; i++) {
            for (j = 0; j <= i; j++) {
                binomialTree[i][j] = new Price();
            }
	}
        
	for (i = 0; i <= numIntervals; i++) {
            for (j = 0; j <= i; j++) {
                binomialTree[i][j].stockPrice = currentStockPrice * Math.pow(up, j) * Math.pow(down, i-j);
            }
	}
      
	for (j = 0; j <= numIntervals; j++) {
            binomialTree[numIntervals][j].optionPrice = Math.max(strikePrice - binomialTree[numIntervals][j].stockPrice, 0.0);
	}
	
	double discount = Math.exp(-riskFreeInterestRate*deltaT);
	for (i = numIntervals-1; i >= 0; i--) {
            for (j = 0; j <= i; j++) {
                binomialTree[i][j].optionPrice = Math.max(strikePrice - binomialTree[i][j].stockPrice,
					discount*(upProb*binomialTree[i+1][j+1].optionPrice +
						downProb*binomialTree[i+1][j].optionPrice));
            }
	}
        binomValue = binomialTree[0][0].optionPrice;
        for(int k = 0;  k <= numIntervals; k++) {
            binomialTree[k] = null;
        }
        binomialTree = null;
        System.gc();
        return binomValue;
    }
        
    double getCallOption(int numIntervals) {
	int i, j;
	double deltaT	= term / numIntervals;
	double up		= 1.0 + riskFreeInterestRate * deltaT + (volatility*Math.sqrt(deltaT));
	double down		= 1.0 + riskFreeInterestRate * deltaT - (volatility*Math.sqrt(deltaT));
	double upProb	= 0.5;
	double downProb = 0.5;
	double binomValue;
        
        Price[][] binomialTree = new Price[numIntervals+1][];

	for (i = 0; i <= numIntervals; i++) {
            binomialTree[i] = new Price[i + 1];   
	}
        
        for (i = 0; i <= numIntervals; i++) {
            for (j = 0; j <= i; j++) {
                binomialTree[i][j] = new Price();
            }
	}
        
	for (i = 0; i <= numIntervals; i++) {
		for (j = 0; j <= i; j++) {
			binomialTree[i][j].stockPrice = currentStockPrice * Math.pow(up, j) * Math.pow(down, i-j);
		}
        }
	for (j = 0; j <= numIntervals; j++) {
            binomialTree[numIntervals][j].optionPrice =
                Math.max(binomialTree[numIntervals][j].stockPrice - strikePrice, 0.0);
	}
	
	double discount = Math.exp(-riskFreeInterestRate*deltaT);
	for (i = numIntervals-1; i >= 0; i--) {
            for (j = 0; j <= i; j++) {
                binomialTree[i][j].optionPrice = 
                    Math.max(binomialTree[i][j].stockPrice - strikePrice,
                        discount*(upProb*binomialTree[i+1][j+1].optionPrice +
                            downProb*binomialTree[i+1][j].optionPrice));
            }
	}
        binomValue = binomialTree[0][0].optionPrice;
        
        return binomValue;
    }
    
}
