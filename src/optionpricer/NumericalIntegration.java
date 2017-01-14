package optionpricer;

public class NumericalIntegration extends Algorithm {
    private int N; 
    private int M;
    private double sMax;
    private boolean print;
    
    public NumericalIntegration() {
        super.name = "Numerical Integration";
    }
    
    public NumericalIntegration(double currentStockPrice, double strikePrice, double term, double riskFreeInterestRate, double volatility) {
        super(currentStockPrice, strikePrice, term, riskFreeInterestRate, volatility);
        this.M = 750;
        this.N = 750;
        this.sMax = currentStockPrice*2;
        this.print = true;
    }
    
    double getPutOption() {
        int i, j;
	double deltaT = term / N;
	double deltaS = sMax / M;
        
        double[][] f = new double[N+1][];
        
        for (i = 0 ; i <= N; i ++) {
            f[i] = new double[M+1];
            for ( j = 0; j < M + 1; j++)
			f[i][j] = 0.0;
        }
        
        // initialize stock price array
	double[]  stockPrice = new double[M + 1];
	for (j = 0; j <= M; j++)
		stockPrice[j] = j* sMax / M;
        
	// initialize stock price at zero and sMax
	for (i = 0; i <= N; i++) {
		f[i][0] = strikePrice;
		f[i][M] = 0.0;
	}
        
	// initialize option value at maturity
	for (j = 0; j <= M; j++)
		f[N][j] = Math.max(stockPrice[j] - strikePrice, 0.0);
        
        // Calculate interior values
	// These are the arrays to be sent to the tridiagonal algorithm
	double[] a = new double[M-1];
	double[] b = new double[M-1];
	double[] c = new double[M-1];
	double[] r = new double[M-1];
	double[] u = new double[M-1];
        
        for (j = 0; j < M-1; j++) {		// note the offsets
		a[j] = aj(j+1, deltaT);
		b[j] = bj(j+1, deltaT);
		c[j] = cj(j+1, deltaT);
	}
        
        for ( i = 1; i<=N; i++) {
            for( j = 0; j<M-1; j++) {
                r[j] = f[N-i+1][j+1];
            }
            r[0] = r[0] - a[0]*f[N-i][0];
            r[M-2] = r[M-2] - c[M-2]*f[N-i][M];
        
        
        tridiag(a,b,c,r,u,M-1);
        
        for(j = 1; j < M; j++) {
            if(u[j-1] < strikePrice - j*deltaS) {
                f[N-i][j] = strikePrice - j*deltaS;
            }
            else {
                f[N-i][j] = u[j-1];
            }
        }
        
    }
        
            double optionValue;

            j = 0;

            while (stockPrice[j] < currentStockPrice) {
                j++;
            }

            if(stockPrice[j] == currentStockPrice) {
                optionValue = f[0][j];
            } else {
                optionValue = f[0][j-1]+(f[0][j]-f[0][j-1])*
			(currentStockPrice-stockPrice[j-1])/(stockPrice[j]-stockPrice[j-1]);
            }
        
	
        return optionValue;    
        
    }
     
     
    double getCallOption() {
        int i, j;
	double deltaT = term / N;
	double deltaS = sMax / M;
        
        double[][] f = new double[N+1][];
        
        for (i = 0 ; i <= N; i ++)
        {
            f[i] = new double[M+1];
            for ( j = 0; j < M + 1; j++)
			f[i][j] = 0.0;
        }
        
        // initialize stock price array
	double[]  stockPrice = new double[M + 1];
	for (j = 0; j <= M; j++)
		stockPrice[j] = j* sMax / M;
        
	// initialize stock price at zero and sMax
	for (i = 0; i <= N; i++) {
		f[i][0] = strikePrice;
		f[i][M] = 0.0;
	}
        
	// initialize option value at maturity
	for (j = 0; j <= M; j++)
		f[N][j] = Math.max(stockPrice[j]  -  strikePrice, 0.0);
        
        // Calculate interior values
	// These are the arrays to be sent to the tridiagonal algorithm
	double[] a = new double[M-1];
	double[] b = new double[M-1];
	double[] c = new double[M-1];
	double[] r = new double[M-1];
	double[] u = new double[M-1];
        
        for (j = 0; j < M-1; j++) {		// note the offsets
		a[j] = aj(j+1, deltaT);
		b[j] = bj(j+1, deltaT);
		c[j] = cj(j+1, deltaT);
	}
        
        for ( i = 1; i<=N; i++)
        {
            for( j = 0; j<M-1; j++)
            {
                r[j] = f[N-i+1][j+1];
            }
            r[0] = r[0] - a[0]*f[N-i][0];
            r[M-2] = r[M-2] - c[M-2]*f[N-i][M];
        
        
        tridiag(a,b,c,r,u,M-1);
        
        for(j = 1; j < M; j++)
        {
            if(u[j-1] < strikePrice - j*deltaS)
            {
                f[N-i][j] = strikePrice - j*deltaS;
            }
            else
            {
                f[N-i][j] = u[j-1];
            }
            }
        }
        
        double optionValue;
        
        j = 0;
        
        while (stockPrice[j] < currentStockPrice) 
        {
            j++;
        }
            if(stockPrice[j] == currentStockPrice)
            {
                optionValue = f[0][j];
            }
            else
            {
                optionValue = f[0][j-1]+(f[0][j]-f[0][j-1])*
			(currentStockPrice-stockPrice[j-1])/(stockPrice[j]-stockPrice[j-1]);
            }
        
	
        return optionValue;
    }
      
     void tridiag(double[] a, double[] b, double[] c, double[] r, double[] u, int length)
    {
        int j;
        double bet;
        u[0] = r[0] ;
        bet = b[0];
        double[] gam = new double[length];
        u[0] = r[0] /bet ;
        for (j = 1; j < length; j++) {
		gam[j] = c[j-1] / bet;
		bet = b[j] - a[j] * gam[j];
		u[j] = (r[j] - a[j] * u[j-1]) / bet;
	}
        
        for (j = (length-2); j >= 0; j--)
		u[j] = u[j] -  gam[j+1] * u[j+1];
        
        
    }
     double aj(int j, double deltaT)
    {
        return (0.5*riskFreeInterestRate*j*deltaT - 0.5*volatility*volatility*j*j*deltaT);
    }
    
    double bj(int j, double deltaT)
    {
        return (1.0 + volatility*volatility*j*j*deltaT + riskFreeInterestRate*deltaT);
    }
    
    double cj(int j, double deltaT)
    {
        return (-0.5*riskFreeInterestRate*j*deltaT - 0.5*volatility*volatility*j*j*deltaT);
    }
}
