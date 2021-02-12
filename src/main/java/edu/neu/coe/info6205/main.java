package edu.neu.coe.info6205;

import edu.neu.coe.info6205.union_find.UF_HWQUPC;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.Destination;
import tech.tablesaw.io.csv.CsvWriter;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class main {
    private static final List<Integer> X = new ArrayList<>();
    private static final List<Double> Y = new ArrayList<>();
    private static final List<Integer> N = new ArrayList<>();
    private static final List<Double> M = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Please enter an integer number bigger than 0 or just enter 0 to do experiments:");
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (str.equals("0")){
            System.out.println("Start experiments:");
            doExperiments();
        }
        else if(!isNum.matches() || str.isEmpty()){
            System.out.println("Illegal input!!!");
        }
        else {
            int n = Integer.parseInt(str);
            System.out.printf("Start UF_HWQUPC with %d sites:\n", n);
            System.out.println("Connections: " + count(n));
        }
    }

    public static int count(int n){
        UF_HWQUPC client = new UF_HWQUPC(n);
        int count = 0;
        Random random = new Random();
        //Loop until all sites are connected (number of components becomes 1)
        while (client.components() != 1){
            int p = random.nextInt(n);
            int q = random.nextInt(n);
            if (p!=q) {
                if (!client.isConnected(p, q)) {
                    client.union(p, q);
                    count++;
                }
            }
        }
        client.show();
        return count;
    }

    public static int countPairs(int n){
        UF_HWQUPC client = new UF_HWQUPC(n);
        int count = 0;
        Random random = new Random();
        //Loop until all sites are connected (number of components becomes 1)
        while (client.components() != 1){
            int p = random.nextInt(n);
            int q = random.nextInt(n);
            if (p!=q) {
                if (!client.isConnected(p, q)) {
                    client.union(p, q);
                }
                count++;
            }
        }
        return count;
    }

    public static void doExperiments() throws IOException {
        int e = 1;
        for (int i = 64; i <= 65536; i *= 2){
            int n = i;
            int sum =0;
            for(int j=0; j<1000; j++){
                sum +=countPairs(n);
            }
            sum =sum/1000;
            int m =sum;
            X.add((int) log2(n));
            Y.add(log2(m));
            N.add(n);
            M.add((double) m);
            System.out.printf("Experiment %d: %d objects — %d pairs generated\n", e, n, m);
            e++;
            if (i==65536) break;
        }
        plotChart();
        System.out.println("Experiments done!!!");
    }

    public static double log2(double n){
        return Math.log(n)/Math.log(2);
    }

    public static void plotChart() throws IOException {
        Table table = createTable(X, Y, N, M);
        CsvWriter writer = new CsvWriter();
        File file = new File("Results.csv");
        Destination destination = new Destination(file);
        writer.write(table, destination);
        Plot.show(LinePlot.create("Log-log plot", table, "lg(N)", "lg(M)"));
    }

    public static Table createTable(List<Integer> x, List<Double> y, List<Integer> n, List<Double> m){
        Integer [] x_column = new Integer[x.size()];
        Double [] y_column = new Double[y.size()];
        Integer [] n_column = new Integer[n.size()];
        Double [] m_column = new Double[m.size()];
        IntColumn lgN = IntColumn.create("lg(N)", x.toArray(x_column));
        DoubleColumn lgM = DoubleColumn.create("lg(M)", y.toArray(y_column));
        IntColumn Ns = IntColumn.create("N", n.toArray(n_column));
        DoubleColumn Ms = DoubleColumn.create("M", m.toArray(m_column));
        Table table = Table.create(lgN, lgM, Ns, Ms);
        return table;
    }
}
