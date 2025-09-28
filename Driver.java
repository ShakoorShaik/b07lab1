import java.io.*;

public class Driver {
    public static void main(String[] args) {
        try {
            Polynomial p = new Polynomial();
            System.out.println("p(x) = " + p);
            System.out.println("p(3) = " + p.evaluate(3));

            double[] coeffs1 = {6, -2, 5};
            int[] exps1 = {0, 1, 3};
            Polynomial p1 = new Polynomial(coeffs1, exps1);
            System.out.println("p1(x) = " + p1);

            double[] coeffs2 = {1, -9};
            int[] exps2 = {1, 4};
            Polynomial p2 = new Polynomial(coeffs2, exps2);
            System.out.println("p2(x) = " + p2);

            Polynomial sum = p1.add(p2);
            System.out.println("p1 + p2 = " + sum);

            Polynomial product = p1.multiply(p2);
            System.out.println("p1 * p2 = " + product);

            double val = sum.evaluate(0.1);
            System.out.println("(p1 + p2)(0.1) = " + val);

            System.out.println("Does p1 + p2 have root at x=1? " + sum.hasRoot(1));

            String filename = "poly_output.txt";
            sum.saveToFile(filename);
            System.out.println("Saved (p1+p2) to file: " + filename);

            File f = new File(filename);
            Polynomial fromFile = new Polynomial(f);
            System.out.println("Polynomial read from file: " + fromFile);

        } catch (IOException e) {
            System.err.println("Error reading or writing file: " + e.getMessage());
        }
    }
}