public class Polynomial{
    private double[] coeff;

    public Polynomial(){
        this.coeff = new double[]{0.0};
    }

    public Polynomial(double[] coeff){
        this.coeff = new double[coeff.length];
        System.arraycopy(coeff, 0, this.coeff, 0, coeff.length);
    }

    public Polynomial add(Polynomial input){
        int maxlen = Math.max(this.coeff.length, input.coeff.length);
        double[] result = new double[maxlen];

        for (int i = 0; i < maxlen; i++){
            double a = (i < this.coeff.length) ? this.coeff[i] : 0.0;
            double b = (i < input.coeff.length) ? input.coeff[i] : 0.0;
            result[i] = a + b;
        }

        return new Polynomial(result);
    }

    public double evaluate(double x){
        double result = 0.0;
        double powOfX = 1.0;

        for (double c : this.coeff){
            result += c * powOfX;
            powOfX *= x;
        }

        return result;
    }

    public boolean hasRoot(double x){
        return Math.abs(evaluate(x)) < 1e-9;
    }
}