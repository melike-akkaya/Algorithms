import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

class Main {
    public static void main(String args[]) throws IOException {

        int[] data = readFile("TrafficFlowDataset.csv");

        // x axis data
        int[] inputSizes = new int[] {500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000, 250000};

        // the following line was used to evaluate the searching algorithms:
        Search.calculateDataForSearching(data, inputSizes);

        // as a result of the operations performed for search, the data becomes sorted.
        // the input file was read again to use the random state.
        data = readFile("TrafficFlowDataset.csv");

        System.out.println("-------------------------");
        // the following line was used to evaluate the sorting algorithms:
        ExperimentsWithSortingAlgorithms.calculateDataForSorting(data, inputSizes);
    }

    // the method used to read the input file:
    public static int[] readFile(String fileName) {
        int[] dataList = new int[250000];

        Path filePath = Paths.get(fileName);

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
            String line;

            try {
                int i = 0;
                while ((line = br.readLine()) != null)
                {
                    if (i < 250000) {
                        String[] cols = line.split(",");
                        if (!Objects.equals(cols[6], " Flow Duration")) {
                            dataList[i] = Integer.parseInt(cols[6]);
                            i++;
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    public static void showAndSaveChart(String title, int[] xAxis, double[][] yAxis) throws IOException {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(800).height(600).title(title)
                .yAxisTitle("Time in Nanoseconds").xAxisTitle("Input Size").build();
        // for sorting algorithms, "milliseconds" should be written instead of "nanoseconds"

        // Convert x axis to double[]
        double[] doubleX = Arrays.stream(xAxis).asDoubleStream().toArray();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);

        // Add a plot for a sorting algorithm
        chart.addSeries("Linear Search (random data)", doubleX, yAxis[0]);
        chart.addSeries("Linear Search (sorted data)", doubleX, yAxis[1]);
        chart.addSeries("Binary Search (sorted data)", doubleX, yAxis[2]);

        // Save the chart as PNG
        BitmapEncoder.saveBitmap(chart, title + ".png", BitmapEncoder.BitmapFormat.PNG);

        // Show the chart
        new SwingWrapper(chart).displayChart();
    }
}
