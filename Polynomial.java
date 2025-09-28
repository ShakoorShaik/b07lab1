import java.io.*;
import java.util.*;

public class Polynomial{
    private double[] coeffs;
    private int[] exps;

    public Polynomial(){
        this.coeffs = new double[]{0.0};
        this.exps = new int[]{0};
    }

    public Polynomial(double[] coeffs, int[] exps){
        if (coeffs.length != exps.length) {
            throw new IllegalArgumentException("Coefficients and exponents arrays must be the same length.");
        }
        this.coeffs = new double[coeffs.length];
        System.arraycopy(coeffs, 0, this.coeffs, 0, coeffs.length);
        this.exps = new int[exps.length];
        System.arraycopy(exps, 0, this.exps, 0, exps.length);
        simplify();
    }

    public Polynomial add(Polynomial input){
        double[] newCoeffs = new double[this.coeffs.length + input.coeffs.length];
        int[] newExps = new int[this.exps.length + input.exps.length];

        System.arraycopy(this.coeffs, 0, newCoeffs, 0, this.coeffs.length);
        System.arraycopy(this.exps, 0, newExps, 0, this.exps.length);
        System.arraycopy(input.coeffs, 0, newCoeffs, this.coeffs.length, input.coeffs.length);
        System.arraycopy(input.exps, 0, newExps, this.exps.length, input.exps.length);

        return new Polynomial(newCoeffs, newExps);
    }

    public Polynomial multiply(Polynomial input){
        List<Double> resultCoeffs = new ArrayList<>();
        List<Integer> resultExps = new ArrayList<>();

        for (int i = 0; i < this.coeffs.length; i++) {
            for (int j = 0; j < input.coeffs.length; j++) {
                resultCoeffs.add(this.coeffs[i] * input.coeffs[j]);
                resultExps.add(this.exps[i] + input.exps[j]);
            }
        }

        double[] arrCoeffs = new double[resultCoeffs.size()];
        for (int i = 0; i < resultCoeffs.size(); i++) {
            arrCoeffs[i] = resultCoeffs.get(i);
        }
        
        int[] arrExps = new int[resultExps.size()];
        for (int i = 0; i < resultExps.size(); i++) {
            arrExps[i] = resultExps.get(i);
        }
        return new Polynomial(arrCoeffs, arrExps);
    }

    public double evaluate(double x){
        double result = 0.0;
        for (int i = 0; i < coeffs.length; i++) {
            result += coeffs[i] * Math.pow(x, exps[i]);
        }
        return result;
    }

    public boolean hasRoot(double x){
        return Math.abs(evaluate(x)) < 1e-9;
    }

    public Polynomial (File file) throws IOException {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            line = br.readLine();
        }
        parseFromString(line);
    }

    private void parseFromString(String s){
        s = s.replace("-", "+-");
        if (s.startsWith("+")) s = s.substring(1);
        String[] terms = s.split("\\+");
    
        List<Double> coeffList = new ArrayList<>();
        List<Integer> expList = new ArrayList<>();
    
        for (String term : terms) {
            if (term.isEmpty()) continue;
    
            double coeff;
            int exp;
    
            if (term.contains("x")) {
                String[] parts = term.split("x");
                if (parts[0].isEmpty() || parts[0].equals("+")) {
                    coeff = 1.0;
                } else if (parts[0].equals("-")) {
                    coeff = -1.0;
                } else {
                    coeff = Double.parseDouble(parts[0]);
                }
    
                exp = (parts.length > 1 && !parts[1].isEmpty()) 
                        ? Integer.parseInt(parts[1]) 
                        : 1;
            } else {
                coeff = Double.parseDouble(term);
                exp = 0;
            }
    
            coeffList.add(coeff);
            expList.add(exp);
        }
    
        this.coeffs = new double[coeffList.size()];
        for (int i = 0; i < coeffList.size(); i++) {
            this.coeffs[i] = coeffList.get(i);
        }
    
        this.exps = new int[expList.size()];
        for (int i = 0; i < expList.size(); i++) {
            this.exps[i] = expList.get(i);
        }
    
        simplify();
    }

    private void simplify(){
        List<Integer> newExps = new ArrayList<>();
        List<Double> newCoeffs = new ArrayList<>();

        for (int i = 0; i < exps.length; i++) {
            int currentExp = exps[i];
            double currentCoeff = coeffs[i];
            boolean found = false;
            for (int j = 0; j < newExps.size(); j++) {
                if (newExps.get(j) == currentExp) {
                    newCoeffs.set(j, newCoeffs.get(j) + currentCoeff);
                    found = true;
                    break;
                }
            }

            if (!found) {
                newExps.add(currentExp);
                newCoeffs.add(currentCoeff);
            }
        }

        for (int i = newExps.size() - 1; i >= 0; i--) {
            if (Math.abs(newCoeffs.get(i)) < 1e-12) {
                newExps.remove(i);
                newCoeffs.remove(i);
            }
        }

        for (int i = 0; i < newExps.size() - 1; i++) {
            for (int j = i + 1; j < newExps.size(); j++) {
                if (newExps.get(j) < newExps.get(i)) {
                    int tmpExp = newExps.get(i);
                    newExps.set(i, newExps.get(j));
                    newExps.set(j, tmpExp);
                    double tmpCoeff = newCoeffs.get(i);
                    newCoeffs.set(i, newCoeffs.get(j));
                    newCoeffs.set(j, tmpCoeff);
                }
            }
        }
        coeffs = new double[newCoeffs.size()];
        exps = new int[newExps.size()];
        for (int i = 0; i < exps.length; i++) {
            exps[i] = newExps.get(i);
            coeffs[i] = newCoeffs.get(i);
        }
    }

    public void saveToFile(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coeffs.length; i++) {
            double c = coeffs[i];
            int e = exps[i];
            if (i > 0) sb.append(c >= 0 ? "+" : "");
            if (e == 0) {
                sb.append(c);
            } else {
                if (c == 1.0) sb.append("x");
                else if (c == -1.0) sb.append("-x");
                else sb.append(c).append("x");
                if (e != 1) sb.append(e);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(sb.toString());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coeffs.length; i++) {
            sb.append(coeffs[i]).append("x^").append(exps[i]);
            if (i < coeffs.length - 1) sb.append(" + ");
        }
        return sb.toString();
    }
}