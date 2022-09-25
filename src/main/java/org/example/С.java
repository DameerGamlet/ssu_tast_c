package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

class PointsChart {

    // конфигурация отображаемого графика (необязательный, нужен для форматирования)
    public static ChartPanel createDemoPanel(XYDataset dataset, String title) {
        // инициируем график
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, "X", "Y", dataset,
                PlotOrientation.VERTICAL, true, true, false);
        // делаем дополнительные настройки (цвет точек, цвет фона, толщину точек и многое другое)
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setBackgroundPaint(Color.WHITE);
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesOutlinePaint(0, Color.black);
        renderer.setSeriesOutlineStroke(0, new BasicStroke(0));
        renderer.setSeriesPaint(0, Color.blue);
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setRange(0.00, 1.00);
        domain.setTickUnit(new NumberTickUnit(0.1));
        domain.setVerticalTickLabels(true);
        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setRange(0.0, 1.0);
        range.setTickUnit(new NumberTickUnit(0.1));

        chart.setTitle(new TextTitle(title, new Font("Roboto", Font.BOLD, 18)));

        return new ChartPanel(chart);
    }
}

public class С extends JFrame {

    // величина К
    private static final int K = 10;
    private static final int N = 75;
    // отображаемое имя приложения
    private static final String title = String.format("Option 34. K = %s, N = %s", K, N);

    // выполнения конфигурации графика
    public С(String s) {
        super(s);
        final ChartPanel chartPanel = PointsChart.createDemoPanel(createSampleData(), title);
        this.add(chartPanel, BorderLayout.CENTER);
    }

    // функция согласно заданию
    public static long func(long x) {
        return (x ^ 4) ^
                (2 * (x & (1 + 2 * x) &
                        (3 + 4 * x) &
                        (7 + 8 * x) &
                        (15 + 16 * x) &
                        (63 + 64 * x))) ^
                (4 * (x * x + 34));
    }


    public static double toBeta(double x, double beta) {
        String binaryX = Integer.toBinaryString((int) x);
        int len = binaryX.length();
        StringBuilder bin = new StringBuilder(binaryX.substring(Math.max(0, len - N))).reverse();

        double res = 0;
        len = bin.length();

        for (int i = 0; i < len; i++) {
            res += Double.parseDouble(String.valueOf(bin.charAt(i) * Math.pow(beta, i)));
        }

        return res;
    }

    // вычисление положения всех точек
    private XYDataset createSampleData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("Points");
        double x, y,
                beta = Math.pow(2, 1.0 / N),
                iMax = Math.pow(2, K),
                betaK = Math.pow(beta, K);
        for (int i = 0; i < iMax; i++) {
            x = (toBeta(i, beta) / betaK) % 1;
            y = (toBeta(func(i), beta) / betaK) % 1;
            series.add(x, y);
        }
        xySeriesCollection.addSeries(series);
        return xySeriesCollection;
    }

    public static void main(String[] args) {
        // запуск приложения
        EventQueue.invokeLater(() -> {
            С c = new С("Task C");
            c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            c.pack();
            c.setLocationRelativeTo(null);
            c.setVisible(true);
        });
    }
}