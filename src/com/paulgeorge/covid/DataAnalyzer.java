package com.paulgeorge.covid;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAnalyzer {

	public DataAnalyzer() {

	}

	private List<CovidData> stateData = null;
	private List<CovidData> countyData = null;

	private List<CovidData> populateStateData() {
		// Open file for US States
		String stateFile = "F:\\eclipse-workspace\\covid-19-data\\us-states.csv";
		BufferedReader csvReader;
		stateData = new ArrayList<>();
		try {
			csvReader = new BufferedReader(new FileReader(stateFile));
			String row;
			// Create data list

			// Loop through file and create data items.
			while ((row = csvReader.readLine()) != null) {
				String[] datum = row.split(",");
				// do something with the data
				if ("date".equalsIgnoreCase(datum[0])) {
					continue;
				}
				CovidData state = new CovidData(LocalDate.parse(datum[0]), datum[1], datum[2], Long.parseLong(datum[3]),
						Long.parseLong(datum[4]));
				stateData.add(state);

			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("There are " + stateData.size() + " state data items.");
		return stateData;
	}

	private List<CovidData> populateCountyData() {
		// Open file for US Counties
		String countyFile = "F:\\eclipse-workspace\\covid-19-data\\us-counties.csv";
		BufferedReader csvReader;
		countyData = new ArrayList<>();
		try {
			csvReader = new BufferedReader(new FileReader(countyFile));
			String row;
			// Create data list

			// Loop through file and create data items.
			while ((row = csvReader.readLine()) != null) {
				String[] datum = row.split(",");
				// do something with the data
				// System.out.println("Looking at row: " + row);
				if ("date".equalsIgnoreCase(datum[0])) {
					continue;
				}
				// 0 1 2 3 4 5
				// date,county,state,fips,cases,deaths
				if (datum[3] == null)
					datum[3] = "99999";
				CovidData county = new CovidData(LocalDate.parse(datum[0]), datum[1], datum[2], datum[3],
						Long.parseLong(datum[4]), Long.parseLong(datum[5]));
				countyData.add(county);

			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("There are " + countyData.size() + " county data items.");

		return countyData;
	}

	public List<Map.Entry<LocalDate, Long>> getStateDeaths() {
		if (stateData == null)
			populateStateData();

		// Find US numbers
		Map<LocalDate, Long> USDeaths = new HashMap<>();

		for (CovidData d : stateData) {
			if (USDeaths.containsKey(d.getDate())) {
				USDeaths.put(d.getDate(), USDeaths.get(d.getDate()) + d.getDeaths());
			} else {
				USDeaths.put(d.getDate(), d.getDeaths());
			}
		}

		LocalDate startDate = LocalDate.parse("2020-03-12");
		LocalDate endDate = LocalDate.now().plusDays(1);

		LocalDate currentDate = startDate;
		List<Map.Entry<LocalDate, Long>> usDead = new ArrayList<>();
		while (currentDate.isBefore(endDate)) {
			long dead = USDeaths.getOrDefault(currentDate, -1l);
			usDead.add(new AbstractMap.SimpleEntry<LocalDate, Long>(currentDate, dead));
			currentDate = currentDate.plusDays(1);
		}

		for (Map.Entry<LocalDate, Long> e : usDead) {
			System.out.println("Date: " + e.getKey() + "   Dead: " + e.getValue());
		}

		return usDead;

	}

	public List<Map.Entry<LocalDate, Long>> getStateInfected() {
		if (stateData == null)
			populateStateData();

		// Find US numbers
		Map<LocalDate, Long> USInfected = new HashMap<>();

		for (CovidData d : stateData) {
			if (USInfected.containsKey(d.getDate())) {
				USInfected.put(d.getDate(), USInfected.get(d.getDate()) + d.getCases());
			} else {
				USInfected.put(d.getDate(), d.getCases());
			}
		}

		LocalDate startDate = LocalDate.parse("2020-03-12");
		LocalDate endDate = LocalDate.now().plusDays(1);

		LocalDate currentDate = startDate;
		List<Map.Entry<LocalDate, Long>> usInfected = new ArrayList<>();
		while (currentDate.isBefore(endDate)) {
			long infected = USInfected.getOrDefault(currentDate, -1l);
			usInfected.add(new AbstractMap.SimpleEntry<LocalDate, Long>(currentDate, infected));
			currentDate = currentDate.plusDays(1);
		}

		for (Map.Entry<LocalDate, Long> e : usInfected) {
			System.out.println("Date: " + e.getKey() + "   Infected: " + e.getValue());
		}

		return usInfected;

	}

	public List<Map.Entry<LocalDate, Long>> getCountyDeaths(String countyName, String stateName) {

		if (countyData == null)
			populateCountyData();
		// Find County Deaths
		Map<LocalDate, Long> theDeaths = new HashMap<>();

		for (CovidData d : countyData) {
			if (stateName != null && countyName != null & d.getState().equalsIgnoreCase(stateName)
					&& d.getCounty().equalsIgnoreCase(countyName)) {
				if (theDeaths.containsKey(d.getDate())) {
					theDeaths.put(d.getDate(), theDeaths.get(d.getDate()) + d.getDeaths());
				} else {
					theDeaths.put(d.getDate(), d.getDeaths());
				}
			}
		}

		LocalDate startDate = LocalDate.parse("2020-02-28");
		LocalDate endDate = LocalDate.now().plusDays(1);

		LocalDate currentDate = startDate;
		List<Map.Entry<LocalDate, Long>> usDead = new ArrayList<>();
		while (currentDate.isBefore(endDate)) {
			long dead = theDeaths.getOrDefault(currentDate, -1l);
			usDead.add(new AbstractMap.SimpleEntry<LocalDate, Long>(currentDate, dead));
			currentDate = currentDate.plusDays(1);
		}

		for (Map.Entry<LocalDate, Long> e : usDead) {
			System.out.println("Date: " + e.getKey() + "   Dead: " + e.getValue());
		}
		return usDead;
	}

	public List<Map.Entry<LocalDate, Long>> getCountyInfected(String countyName, String stateName) {
		if (countyData == null)
			populateCountyData();

		// Find County infected
		System.out.println("Looking for " + countyName + ", " + stateName);
		Map<LocalDate, Long> theInfected = new HashMap<>();

		for (CovidData d : countyData) {
			// System.out.println("Looking at state: " + d.getState() + " and County: " +
			// d.getCounty());
			if (stateName != null && countyName != null && d.getState().equalsIgnoreCase(stateName)
					&& d.getCounty().equalsIgnoreCase(countyName)) {
				if (theInfected.containsKey(d.getDate())) {
					theInfected.put(d.getDate(), theInfected.get(d.getDate()) + d.getCases());
				} else {
					theInfected.put(d.getDate(), d.getCases());
				}
			}
		}

		LocalDate startDate = LocalDate.parse("2020-03-12");
		LocalDate endDate = LocalDate.now().plusDays(1);

		LocalDate currentDate = startDate;
		List<Map.Entry<LocalDate, Long>> totalInfected = new ArrayList<>();
		while (currentDate.isBefore(endDate)) {
			long infected = theInfected.getOrDefault(currentDate, -1l);
			totalInfected.add(new AbstractMap.SimpleEntry<LocalDate, Long>(currentDate, infected));
			currentDate = currentDate.plusDays(1);
		}

		for (Map.Entry<LocalDate, Long> e : totalInfected) {
			System.out.println("Date: " + e.getKey() + "   Infected: " + e.getValue());
		}
		return totalInfected;
	}

}
