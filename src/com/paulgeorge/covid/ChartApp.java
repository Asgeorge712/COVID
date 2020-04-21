package com.paulgeorge.covid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;

public class ChartApp extends Application {
	private static Series<String, Number> infectedSeries;
	private static Series<String, Number> deathSeries;
	private static List<Map.Entry<LocalDate, Long>> deathList;
	private static List<Map.Entry<LocalDate, Long>> infectedList;

	private static String title;

	public static void main(String args[]) {
		DataAnalyzer dataAnalyzer = new DataAnalyzer();

		/*
		 * deathList = dataAnalyzer.getCountyDeaths("Prince William", "Virginia");
		 * infectedList = dataAnalyzer.getCountyInfected("Prince William", "Virginia");
		 * title = "Total Cases and Deaths in Prince William County"; String[] newArgs =
		 * new String[] { "Totals" };
		 */
		title = "Total Cases and Deaths in the US";
		deathList = dataAnalyzer.getStateDeaths();
		infectedList = dataAnalyzer.getStateInfected();
		String[] newArgs = new String[] { "Totals" };

		launch(newArgs);
	}

	public static void setTotalDeathSeries(List<Map.Entry<LocalDate, Long>> dataList) {
		deathSeries = new XYChart.Series();
		deathSeries.setName("Total Deaths");
		// populating the series with data
		for (Map.Entry<LocalDate, Long> entry : dataList) {
			if (entry.getValue() > -1) {
				deathSeries.getData()
						.add(new Data<String, Number>(entry.getKey().toString(), (Number) entry.getValue()));
			}
		}
	}

	public static void setTotalInfectedSeries(List<Map.Entry<LocalDate, Long>> dataList) {
		infectedSeries = new XYChart.Series();
		infectedSeries.setName("Total Infected");
		// populating the series with data
		for (Map.Entry<LocalDate, Long> entry : dataList) {
			if (entry.getValue() > -1) {
				infectedSeries.getData()
						.add(new Data<String, Number>(entry.getKey().toString(), (Number) entry.getValue()));
			}
		}
	}

	public static void setDailyDeathSeries(List<Map.Entry<LocalDate, Long>> dataList) {
		deathSeries = new XYChart.Series();
		deathSeries.setName("Total Deaths");
		// populating the series with data
		Long previousDeaths = -1l;
		for (Map.Entry<LocalDate, Long> entry : dataList) {
			if (entry.getValue() > 0) {
				Long currentDeaths = entry.getValue();
				Long dailyDeaths;
				if (previousDeaths == -1) {
					dailyDeaths = currentDeaths;
				} else {
					dailyDeaths = currentDeaths - previousDeaths;
				}
				deathSeries.getData().add(new Data<String, Number>(entry.getKey().toString(), (Number) dailyDeaths));
				previousDeaths = currentDeaths;
			}
		}
	}

	public static void setDailyInfectedSeries(List<Map.Entry<LocalDate, Long>> dataList) {
		infectedSeries = new XYChart.Series();
		infectedSeries.setName("Total Infected");
		// populating the series with data
		Long previousInfected = -1l;
		for (Map.Entry<LocalDate, Long> entry : dataList) {
			if (entry.getValue() > 0) {
				Long currentInfected = entry.getValue();
				Long dailyDeaths;
				if (previousInfected == -1) {
					dailyDeaths = currentInfected;
				} else {
					dailyDeaths = currentInfected - previousInfected;
				}
				infectedSeries.getData().add(new Data<String, Number>(entry.getKey().toString(), (Number) dailyDeaths));
				previousInfected = currentInfected;
			}
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		Parameters params = this.getParameters();
		String type = "";
		for (String p : params.getUnnamed()) {
			System.out.println("Param: " + p);
			type = p;
		}

		if ("Totals".equalsIgnoreCase(type)) {
			setTotalDeathSeries(deathList);
			setTotalInfectedSeries(infectedList);
			stage = setStageTotals(stage);
		} else if ("Daily".equalsIgnoreCase(type)) {
			setDailyDeathSeries(deathList);
			setDailyInfectedSeries(infectedList);
			stage = setStageDaily(stage);
		}
		stage.show();
	}

	private Stage setStageDaily(Stage stage) {
		stage.setTitle(title);
		// defining the axes

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		// creating the chart
		final BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);

		barChart.setTitle(title);
		// defining a series
		barChart.getData().add(deathSeries);
		barChart.getData().add(infectedSeries);

		Scene scene = new Scene(barChart, 1000, 600);

		stage.setScene(scene);

		return stage;
	}

	private Stage setStageTotals(Stage stage) {
		stage.setTitle(title);
		// defining the axes

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		// creating the chart
		final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);

		lineChart.setTitle(title);
		// defining a series

		Scene scene = new Scene(lineChart, 800, 600);
		lineChart.getData().add(deathSeries);
		lineChart.getData().add(infectedSeries);

		stage.setScene(scene);
		return stage;
	}

}
