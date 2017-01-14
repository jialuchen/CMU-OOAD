
package optionpricer;

import java.util.Random;

public class Simulation extends Algorithm{
    
    public Simulation() {
        super.name = "Simulation";
    }

	public Simulation (double currentStockPrice, double strikePrice, double term, double riskFreeInterestRate, double volatility) {
		super(currentStockPrice, strikePrice, term, riskFreeInterestRate, volatility);
	}

	public double getCallOption(int numIntervals, int numTrials) {
		int i, trialCount;
		double deltaT = term / (double) numIntervals;
		double trialRunningSum, trialAverage, trialPayoff;
		double simulationRunningSum, simulationAveragePayoff;
		double s;
		Random rand = new Random();
		simulationRunningSum = 0.0;
		for (trialCount = 1; trialCount <= numTrials; trialCount++) {
			s = currentStockPrice;
			trialRunningSum = 0.0;
			double nns = 0;
			for (i = 0; i < numIntervals; i++) {
				nns = rand.nextGaussian();
				s = s * Math.exp((riskFreeInterestRate - volatility * volatility / 2) * deltaT + volatility * nns * Math.sqrt(deltaT));
				trialRunningSum += s;

			}
			trialAverage = trialRunningSum / numIntervals;
			trialPayoff = Math.max(trialAverage - strikePrice, 0.0);
			simulationRunningSum += trialPayoff;
		}
		simulationAveragePayoff = simulationRunningSum / numTrials;
		double valueOfOption;
		valueOfOption = simulationAveragePayoff * Math.exp(-riskFreeInterestRate * term);
		return valueOfOption;
	}

	public double getPutOption(int numIntervals, int numTrials) {
		int i, trialCount;
		double deltaT = term / (double) numIntervals;
		double trialRunningSum, trialAverage, trialPayoff;
		double simulationRunningSum, simulationAveragePayoff;
		double s;
		Random rand = new Random();
		simulationRunningSum = 0.0;
		for (trialCount = 1; trialCount <= numTrials; trialCount++) {
			s = currentStockPrice;
			trialRunningSum = 0.0;
			double nns = 0;
			for (i = 0; i < numIntervals; i++) {
				nns = rand.nextGaussian();
				s = s * Math.exp(
						(riskFreeInterestRate - volatility * volatility / 2) * deltaT + volatility * nns * Math.sqrt(deltaT));
				trialRunningSum += s;

			}
			trialAverage = trialRunningSum / numIntervals;
			trialPayoff = Math.max(strikePrice - trialAverage, 0.0);
			simulationRunningSum += trialPayoff;
		}
		simulationAveragePayoff = simulationRunningSum / numTrials;
		double valueOfOption;
		valueOfOption = simulationAveragePayoff * Math.exp(-riskFreeInterestRate * term);
		return valueOfOption;
	}    
}
