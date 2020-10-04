package org.openmrs.module.graphing.fragment.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.api.ChartConceptService;
import org.openmrs.module.graphing.api.ChartTypeService;

public class ChartInfo {
	
	private VisitChart visitChart;
	
	private GraphChart graphChart;
	
	private HealthTrendChart healthTrendChart;
	
	//private BmiChart bmiChart;
	private Chart chart;
	
	private Patient patient;
	
	private int chartTypeId;
	
	private String title, chartTypeName;
	
	public ChartInfo(Chart chart, Patient patient) {
		this.patient = patient;
		this.chart = chart;
		chartTypeId = chart.getchartTypeId();
		title = chart.getTitle();
		List<ChartType> chartTypes = Context.getService(ChartTypeService.class).getAllChartTypes();
		for (ChartType chartType : chartTypes) {
			if (chartType.getId() == chartTypeId) {
				this.chartTypeName = chartType.getName().toLowerCase();
				break;
			}
		}
		if (chartTypeName.contains("graph")) {
			chartTypeName = "graph";
			graphChart = new GraphChart(chart, patient, chartTypeName);
			if (patient == null) { // in this case we only need to get chart info for add/remove charts; we're not displaying charts
				return;
			}
			graphChart.setAllLineChartInfos(getConceptLineChartInfos(chart, patient));
		} else if (chartTypeName.contains("bmi")) {
			chartTypeName = "bmi";
			graphChart = new GraphChart(chart, patient, chartTypeName);
			if (patient == null) {
				return;
			}
			graphChart.setAllLineChartInfos(getBMILineChartInfos(chart, patient));
		} else if (chartTypeName.contains("visit")) {
			chartTypeName = "visit";
			visitChart = new VisitChart(chart, patient);
		} else if (chartTypeName.contains("trend")) {
			chartTypeName = "trend";
			healthTrendChart = new HealthTrendChart(chart, patient);
		}
		
	}
	
	private List<LineChartData> getConceptLineChartInfos(Chart chart, Patient patient) {
		List<LineChartData> allLineChartData = new ArrayList<LineChartData>();
		List<ChartConcept> chartConcepts = Context.getService(ChartConceptService.class).getAllChartConceptsForChart(
		    chart.getId());
		//System.out.println("\n\n\nCONCEPTS NUMBER: " + chartConcepts.size() + "\n\n");
		for (ChartConcept chartConcept : chartConcepts) {
			if (chartConcept.getVoided()) {
				continue;
			}
			allLineChartData.add(getLineChartData(chart, patient, chartConcept));
		}
		return allLineChartData;
	}
	
	private LineChartData getLineChartData(Chart chart, Patient patient, ChartConcept chartConcept) {
		ArrayList<LineChartDataPoint> allLineChartDataPoints = new ArrayList<LineChartDataPoint>();
		int conceptId = chartConcept.getConceptId();
		String conceptName = Context.getConceptService().getConcept(conceptId).getDisplayString();
		ConceptNumeric conceptNumeric = Context.getConceptService().getConceptNumeric(conceptId);
		Concept concept = Context.getConceptService().getConcept(conceptId);
		String color = chartConcept.getColor();
		
		Person person = Context.getPersonService().getPerson(patient.getPatientId());
		List<Obs> allObs = Context.getObsService().getObservationsByPersonAndConcept(person, concept);
		System.out.println("\n\nChartConceptInfo: " + conceptId + " color: " + color);
		for (Obs obs : allObs) {
			allLineChartDataPoints.add(new LineChartDataPoint(obs.getObsDatetime(), obs.getValueNumeric()));
		}
		LineChartData lineChartData = new LineChartData(chartConcept.getColor(), conceptName);
		lineChartData.setDataPoints(allLineChartDataPoints);
		return lineChartData;
		//System.out.println(obs.getDateCreated() + "  " + obs.getValueNumeric());
	}
	
	private List<LineChartData> getBMILineChartInfos(Chart chart, Patient patient) {
		List<LineChartData> allLineChartData = new ArrayList<LineChartData>();
		int conceptIdWeight, conceptIdHeight;
		
		conceptIdHeight = 5090;
		conceptIdWeight = 5089;
		if (patient != null) {
			allLineChartData.add(getAllBmiData(chart, patient, conceptIdHeight, conceptIdWeight));
		}
		return allLineChartData;
	}
	
	private LineChartData getAllBmiData(Chart chart, Patient patient, int conceptIdHeight, int conceptIdWeight) {
		LineChartData lineChartData = new LineChartData("red", "BMI");
		ArrayList<LineChartDataPoint> allLineChartDataPoints = new ArrayList<LineChartDataPoint>();
		List<Encounter> encounters = Context.getEncounterService().getEncountersByPatient(patient);
		Set<Obs> allObs;
		Date date;
		Double weightValue = 0.0, heightValue = 0.0;
		System.out.println("Patient id: " + patient.getPatientIdentifier().getIdentifier());
		for (Encounter encounter : encounters) {
			weightValue = 0.0;
			heightValue = 0.0;
			date = encounter.getEncounterDatetime();
			allObs = encounter.getAllObs();
			for (Obs obs : allObs) {
				if (obs.getConcept().getConceptId() == conceptIdHeight) {
					heightValue = obs.getValueNumeric();
				}
				if (obs.getConcept().getConceptId() == conceptIdWeight) {
					weightValue = obs.getValueNumeric();
				}
			}
			if ((weightValue > 0.0) && heightValue > 0.0) {
				// The formula for BMI is weight in kilograms divided by height in meters squared
				double height = heightValue / 100.; // heightValue is in cm so convert to meters
				double value = weightValue / (height * height);
				System.out.println("\n\nBMI: height/weight, bmi: " + heightValue + " " + weightValue + "  " + value + "   "
				        + date);
				allLineChartDataPoints.add(new LineChartDataPoint(date, value));
			}
		}
		lineChartData.setDataPoints(allLineChartDataPoints);
		return lineChartData;
	}
	
	public VisitChart getVisitChart() {
		return visitChart;
	}
	
	public void setVisitChart(VisitChart visitChart) {
		this.visitChart = visitChart;
	}
	
	public GraphChart getGraphChart() {
		return graphChart;
	}
	
	public void setGraphChart(GraphChart graphChart) {
		this.graphChart = graphChart;
	}
	
	public HealthTrendChart getHealthTrendChart() {
		return healthTrendChart;
	}
	
	public void setHealthTrendChart(HealthTrendChart healthTrendChart) {
		this.healthTrendChart = healthTrendChart;
	}
	
	public Chart getChart() {
		return chart;
	}
	
	public void setChart(Chart chart) {
		this.chart = chart;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public int getChartTypeId() {
		return chartTypeId;
	}
	
	public void setChartTypeId(int chartTypeId) {
		this.chartTypeId = chartTypeId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getChartTypeName() {
		return chartTypeName;
	}
	
	public void setChartTypeName(String chartTypeName) {
		this.chartTypeName = chartTypeName;
		
	}
	
}

class LineChartData {
	
	private String color;
	
	private List<LineChartDataPoint> dataPoints;
	
	private String lineName;
	
	LineChartData(String color, String lineName) {
		this.color = color;
		this.lineName = lineName;
		dataPoints = new ArrayList<LineChartDataPoint>();
	}
	
	public void addDataPoint(LineChartDataPoint lineChartDataPoint) {
		dataPoints.add(lineChartDataPoint);
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getLineName() {
		return lineName;
	}
	
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	public List<LineChartDataPoint> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(List<LineChartDataPoint> dataPoints) {
		this.dataPoints = dataPoints;
	}
	
}

class LineChartDataPoint {
	
	private Date date;
	
	private double value;
	
	LineChartDataPoint(Date date, double value) {
		this.date = date;
		this.value = value;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
}

class GraphChart {
	
	private String xaxisLabel = "", yaxisLabel = "";
	
	private List<LineChartData> allLineChartInfos = new ArrayList<LineChartData>();
	
	private List<ChartConceptInfo> allChartConceptInfos = new ArrayList<ChartConceptInfo>();
	
	public GraphChart(Chart chart, Patient patient, String chartTypeName) {
		xaxisLabel = chart.getXaxisLabel();
		if ((xaxisLabel == null) || (xaxisLabel.equals(""))) {
			xaxisLabel = "";
		}
		yaxisLabel = chart.getYaxisLabel();
		if ((yaxisLabel == null) || (yaxisLabel.equals(""))) {
			yaxisLabel = "";
		}
		allChartConceptInfos = createChartConceptInfos(chart);
	}
	
	static List<ChartConceptInfo> createChartConceptInfos(Chart chart) {
		List<ChartConceptInfo> chartConceptInfos = new ArrayList<ChartConceptInfo>();
		int conceptId;
		String conceptName;
		List<ChartConcept> chartConcepts = Context.getService(ChartConceptService.class).getAllChartConceptsForChart(
		    chart.getId());
		if ((chartConcepts == null) || (chartConcepts.size() == 0)) { // FORMULA CHART, LIKE BMI, SO NO CONCEPTS
			return null;
		}
		System.out.println("\n\nGET CONCEPTS FOR CHART: " + chart.getTitle() + " concepts size: " + chartConcepts.size());
		for (ChartConcept chartConcept : chartConcepts) {
			conceptId = chartConcept.getConceptId();
			conceptName = Context.getConceptService().getConcept(conceptId).getDisplayString();
			System.out.println(conceptName);
			chartConceptInfos.add(new ChartConceptInfo(conceptId, conceptName, chartConcept.getColor()));
		}
		return chartConceptInfos;
	}
	
	public List<ChartConceptInfo> getAllChartConceptInfos() {
		return allChartConceptInfos;
	}
	
	public void setAllChartConceptInfos(List<ChartConceptInfo> allChartConceptInfos) {
		this.allChartConceptInfos = allChartConceptInfos;
	}
	
	public List<LineChartData> getAllLineChartInfos() {
		return allLineChartInfos;
	}
	
	public void setAllLineChartInfos(List<LineChartData> allLineChartInfos) {
		this.allLineChartInfos = allLineChartInfos;
	}
	
	public String getXaxisLabel() {
		return xaxisLabel;
	}
	
	public void setXaxisLabel(String xaxisLabel) {
		this.xaxisLabel = xaxisLabel;
	}
	
	public String getYaxisLabel() {
		return yaxisLabel;
	}
	
	public void setYaxisLabel(String yaxisLabel) {
		this.yaxisLabel = yaxisLabel;
	}
}

class VisitChart {
	
	List<VisitInfo> allVisitInfos = new ArrayList<VisitInfo>();
	
	VisitChart(Chart chart, Patient patient) {
		List<Visit> visits = Context.getVisitService().getVisitsByPatient(patient);
		for (Visit visit : visits) {
			allVisitInfos.add(new VisitInfo(visit, null));
		}
	}
	
}

class VisitInfo {
	
	String visitType = "", visitDateString;
	
	Date visitDate;
	
	String encounterTypes = "";
	
	public VisitInfo(Visit visit, Patient patient) {
		visitType = visit.getVisitType().getName();
		visitDate = visit.getStartDatetime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		visitDateString = simpleDateFormat.format(visitDate);
		List<Encounter> encounters = Context.getEncounterService().getEncountersByVisit(visit, true);
		for (Encounter encounter : encounters) {
			encounterTypes += "," + encounter.getEncounterType().getName();
		}
		encounterTypes = encounterTypes.substring(1);
	}
	
	public String getVisitType() {
		return visitType;
	}
	
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	
	public String getVisitDateString() {
		return visitDateString;
	}
	
	public void setVisitDateString(String visitDateString) {
		this.visitDateString = visitDateString;
	}
	
	public Date getVisitDate() {
		return visitDate;
	}
	
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	
	public String getEncounterTypes() {
		return encounterTypes;
	}
	
	public void setEncounterTypes(String encounterTypes) {
		this.encounterTypes = encounterTypes;
	}
	
}

class HealthTrendChart {
	
	List<EncounterInfo> allEncounterInfo = new ArrayList<EncounterInfo>();
	
	List<Concept> concepts = new ArrayList<Concept>();
	
	private List<ChartConceptInfo> allChartConceptInfos = new ArrayList<ChartConceptInfo>();
	
	public HealthTrendChart(Chart chart, Patient patient) {
		System.out.println("\n\n\nHealthTrendCharts");
		
		List<ChartConcept> chartConcepts = Context.getService(ChartConceptService.class).getAllChartConceptsForChart(
		    chart.getId());
		getAllConcepts(chartConcepts); // concepts ordered by conceptId
		for (ChartConcept chartConcept : chartConcepts) {
			if (chartConcept.getVoided()) {
				continue;
			}
			int conceptId = chartConcept.getConceptId();
			String conceptName = Context.getConceptService().getConcept(conceptId).getDisplayString();
		}
		allChartConceptInfos = GraphChart.createChartConceptInfos(chart);
		if (patient == null) {
			return;
		}
		
		List<Encounter> allEncounters = new ArrayList<Encounter>();
		allEncounters = Context.getEncounterService().getEncountersByPatient(patient);
		Person person = patient.getPerson();
		List<Obs> allObs = Context.getObsService().getObservationsByPerson(person);
		
		for (Encounter encounter : allEncounters) {
			List<Obs> obsForEncounter = new ArrayList<Obs>();
			obsForEncounter = getObsForEncounter(encounter, allObs);
			EncounterInfo encounterInfo = new EncounterInfo(encounter, concepts, obsForEncounter);
			if (encounterInfo.getAllObsSortedByConceptId().size() > 0) {
				System.out.println("ADDING NEW ENCOUNTER INFO");
				allEncounterInfo.add(encounterInfo);
			}
		}
		System.out.println("allEncounterInfo size: " + allEncounterInfo.size());
		for (EncounterInfo encounterInfo : allEncounterInfo) {
			System.out.println(encounterInfo.getEncounter().getEncounterDatetime());
			List<Obs> allObss = encounterInfo.getAllObsSortedByConceptId();
			System.out.println(allObss.size());
			for (Obs obss : allObss) {
				System.out.print(obss);
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
	void getAllConcepts(List<ChartConcept> chartConcepts) {
		for (ChartConcept chartConcept : chartConcepts) {
			int conceptId = chartConcept.getConceptId();
			Concept concept = Context.getConceptService().getConcept(conceptId);
			concepts.add(concept);
		}
		Collections.sort(concepts, new SortbyConceptId());
	}
	
	ArrayList<Obs> getObsForEncounter(Encounter encounter, List<Obs> allObs) {
		ArrayList<Obs> allObss = new ArrayList<Obs>();
		int encounterId = encounter.getEncounterId();
		for (Obs obs : allObs) {
			if (obs.getEncounter().getEncounterId() == encounterId) {
				allObss.add(obs);
			}
		}
		return allObss;
	}
	
	public List<EncounterInfo> getAllEncounterInfo() {
		return allEncounterInfo;
	}
	
	public void setAllEncounterInfo(List<EncounterInfo> allEncounterInfo) {
		this.allEncounterInfo = allEncounterInfo;
	}
	
	public List<Concept> getConcepts() {
		return concepts;
	}
	
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	
}

class ChartConceptInfo {
	
	private int conceptId;
	
	private String conceptName, color;
	
	ChartConceptInfo(int conceptId, String conceptName, String color) {
		this.color = color;
		this.conceptId = conceptId;
		this.conceptName = conceptName;
	}
	
	public int getConceptId() {
		return conceptId;
	}
	
	public void setConceptId(int conceptId) {
		this.conceptId = conceptId;
	}
	
	public String getConceptName() {
		return conceptName;
	}
	
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
}

class EncounterInfo {
	
	Encounter encounter;
	
	List<Obs> allObsSortedByConceptId = new ArrayList<Obs>();
	
	List<String> allObsStringValues = new ArrayList<String>(); // use strings to accommodate different kinds of obs values
	
	public EncounterInfo(Encounter encounter, List<Concept> concepts, List<Obs> allObs) {
		this.encounter = encounter;
		boolean isThereNonNullObs = false;
		Obs obs = null;
		for (Concept concept : concepts) {
			for (Obs obsNext : allObs) {
				if (obsNext.getConcept().getConceptId() == concept.getConceptId()) {
					allObsSortedByConceptId.add(obsNext);
					allObsStringValues.add(getStringValueForObs(obsNext));
					isThereNonNullObs = true;
					obs = obsNext;
					break;
				}
			}
			if (obs == null) {
				allObsSortedByConceptId.add(null);
				allObsStringValues.add("");
			}
			obs = null;
		}
		if (!isThereNonNullObs) {
			allObsSortedByConceptId = new ArrayList<Obs>();
		}
		System.out.println(encounter.getEncounterDatetime() + " numObs: " + allObsSortedByConceptId.size());
	}
	
	String getStringValueForObs(Obs obs) {
		if (obs.getValueBoolean() != null) {
			return obs.getValueBoolean() == true ? "true" : "false";
		}
		if (obs.getValueDate() != null) {
			return obs.getValueDatetime().toString();
		}
		if (obs.getValueCoded() != null) {
			int codedConceptId = obs.getValueCoded().getConceptId();
			String conceptName = Context.getConceptService().getConcept(codedConceptId).getDisplayString();
			return conceptName;
		}
		if (obs.getValueNumeric() != null) {
			return obs.getValueNumeric().toString();
		}
		if (obs.getValueText() != null) {
			return obs.getValueText();
		}
		return "";
	}
	
	public Encounter getEncounter() {
		return encounter;
	}
	
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	
	public List<Obs> getAllObsSortedByConceptId() {
		return allObsSortedByConceptId;
	}
	
	public void setAllObsSortedByConceptId(List<Obs> allObsSortedByConceptId) {
		this.allObsSortedByConceptId = allObsSortedByConceptId;
	}
	
}

class SortbyConceptId implements Comparator<Concept> {
	
	public int compare(Concept a, Concept b) {
		return a.getConceptId() - b.getConceptId();
	}
}
