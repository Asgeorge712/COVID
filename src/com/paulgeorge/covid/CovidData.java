package com.paulgeorge.covid;

import java.time.LocalDate;

public class CovidData implements Comparable<CovidData> {
	LocalDate date;
	String state;
	String county;
	long cases;
	long deaths;
	String fips;

	public CovidData() {

	}

	public CovidData(LocalDate date, String state, String fips, long cases, long deaths) {
		this.state = state;
		this.cases = cases;
		this.date = date;
		this.deaths = deaths;
		this.fips = fips;
	}

	// date,county,state,fips,cases,deaths
	public CovidData(LocalDate date, String county, String state, String fips, long cases, long deaths) {
		if (county.equalsIgnoreCase("Prince William")) {
			System.out.println(
					"Creating record for " + county + ".  There are " + deaths + " deaths and " + cases + " infected.");
		}
		this.state = state;
		this.county = county;
		this.cases = cases;
		this.date = date;
		this.deaths = deaths;
		this.fips = fips;
	}

	@Override
	public int compareTo(CovidData o) {
		return getDate().compareTo(o.getDate());
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getCases() {
		return cases;
	}

	public void setCases(long cases) {
		this.cases = cases;
	}

	public long getDeaths() {
		return deaths;
	}

	public void setDeaths(long deaths) {
		this.deaths = deaths;
	}

	public String getFips() {
		return fips;
	}

	public void setFips(String fips) {
		this.fips = fips;
	}

}
