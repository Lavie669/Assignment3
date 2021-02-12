package edu.neu.coe.info6205;

import edu.neu.coe.info6205.union_find.UF_HWQUPC;
import tech.tablesaw.api.IntColumn;
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
    private static final List<Integer> Y = new ArrayList<>();
    private static final List<String> categories = new ArrayList<>();

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
            if (!client.isConnected(p, q)){
                client.union(p, q);
                count++;
            }
        }
//        client.show();
        return count;
    }

    public static void doExperiments() throws IOException {
        int e = 1;
        for (int i = 1; i <= 10000; i += 100){
            int n = i;
            int m =count(i);
            X.add(n);
            Y.add(m);
            System.out.printf("Experiment %d: %d objects gets %d pairs\n", e, n, m);
            e++;
        }
        plotChart();
        System.out.println("Experiments done!!!");
    }

    public static void plotChart() throws IOException {
        Table table = createTable(X, Y);
        CsvWriter writer = new CsvWriter();
        File file = new File("Results.csv");
        Destination destination = new Destination(file);
        writer.write(table, destination);
        Plot.show(LinePlot.create("The number of objects (N) vs. The number of pairs (M)", table, "N", "M"));
    }

    public static Table createTable(List<Integer> x, List<Integer> y){
        Integer [] x_column = new Integer[x.size()];
        Integer [] y_column = new Integer[y.size()];
        IntColumn N = IntColumn.create("N", x.toArray(x_column));
        IntColumn M = IntColumn.create("M", y.toArray(y_column));
        Table table = Table.create(N, M);
        return table;
    }
}
