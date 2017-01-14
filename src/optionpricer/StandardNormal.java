package optionpricer;

public class StandardNormal {
    
    public static double cumulative(double x) {
        
        double PI = 3.141592653;
        double GAMMA = 0.2316419;
        double A1 = 0.319381530;
        double A2 = -0.356563782;
        double A3 = 1.781477937;
        double A4 = -1.821255978;
        double A5 = 1.330274429;
        boolean xIsNeg = false;
        
        if (x < 0.0) {
            xIsNeg = true;
            x = -x;
        }
        
        double k = 1.0/(1.0 + GAMMA * x);
        double oneOverSqrtTwoPI = 1.0 / Math.sqrt(2*PI);
        double nPrime = (1.0/Math.sqrt(2*PI))*Math.exp((-0.5)*x*x);
        double kSquared = k*k;
        double kFourth = kSquared * kSquared;
        double result = 1.0 - nPrime*(A1*k + A2*kSquared + A3*k*kSquared + A4*kFourth + A5*kFourth*k);
        if (xIsNeg)    
            return 1-result;
        else
            return result;
    } 
}
