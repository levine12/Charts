/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.graphing.page.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.ConceptDatatype;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.api.ChartConceptService;
import org.openmrs.module.graphing.api.ChartService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class AddRemoveChartsPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		
	}
	
	/*
	Chart Types: trend, visit, graph
	Chart Concepts: 
	        graph: 165871/OD_visus/blue, 165872/OS_visus/red, 165873/OU_visus/green,
	        trend:5089/Weight (kg), 5090/Height (cm)
	 */
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "id", required = false) int chartId,
	        @RequestParam(value = "title", required = false) String title,
	        @RequestParam(value = "chartTypeId", required = false) int chartTypeId,
	        @RequestParam(value = "chartTypeName", required = false) String chartTypeName,
	        @RequestParam(value = "xAxisLabel", required = false) String xAxisLabel,
	        @RequestParam(value = "yAxisLabel", required = false) String yAxisLabel,
	        @RequestParam(value = "concepts", required = false) String concepts, // for trend charts
	        @RequestParam(value = "conceptsColors", required = false) String conceptsColors) { // for graph charts
		Chart chart = Context.getService(ChartService.class).getChart(chartId);
		chart.setTitle(title);
		boolean goodConcepts = true;
		chartTypeName = chartTypeName.trim();
		
		if (chartTypeName.equals("graph")) {
			chart.setXaxisLabel(xAxisLabel);
			chart.setYaxisLabel(yAxisLabel);
			goodConcepts = adjustConcepts(chart, chartTypeName, conceptsColors.trim());
		} else if (chartTypeName.equals("bmi")) {
			chart.setXaxisLabel(xAxisLabel);
			chart.setYaxisLabel(yAxisLabel);
		} else if (chartTypeName.equals("trend")) {
			goodConcepts = adjustConcepts(chart, chartTypeName, concepts.trim());
		}
		
		if (goodConcepts) {
			Context.getService(ChartService.class).saveChart(chart);
			InfoErrorMessageUtil.flashInfoMessage(session, "Chart Edited: " + title);
		} else {
			InfoErrorMessageUtil.flashInfoMessage(session, "Chart NOT Edited: " + title);
		}
		
		return "redirect:" + "graphing/addRemoveCharts.page";
	}
	
	boolean adjustConcepts(Chart chart, String chartTypeName, String concepts) {
		if (!isAllNewConceptsValid(concepts, chartTypeName)) {
			return false;
		}
		addNewConceptsRemoveOldConcepts(chart, chartTypeName, concepts);
		return true;
	}
	
	private boolean isAllNewConceptsValid(String concepts, String chartTypeName) throws APIException, NumberFormatException {
		if ((concepts == null) || concepts.equals("")) {
			return false;
		}
		String[] conceptsArray = concepts.split(",");
		// Make sure new concept ids are valid; be sure graph chart type concepts are numeric
		for (int i = 0; i < conceptsArray.length; i++) {
			String[] conceptArray = conceptsArray[i].split("/");
			int conceptId = Integer.valueOf(conceptArray[0]);
			//String color = consColorArray[2];
			Concept newConcept = Context.getConceptService().getConcept(conceptId);
			if (newConcept == null) {
				return false;
			}
			if (chartTypeName.equals("graph") && (!newConcept.getDatatype().isNumeric())) {
				return false;
			}
		}
		return true;
	}
	
	void addNewConceptsRemoveOldConcepts(Chart chart, String chartTypeName, String concepts) {
		List<ChartConcept> oldChartConcepts = Context.getService(ChartConceptService.class).getAllChartConceptsForChart(
		    chart.getId());
		ChartConcept oldChartConcept, newChartConcept;
		String[] conceptsArray = concepts.split(",");
		String color = null;
		for (String conceptsArray1 : conceptsArray) {
			String[] conceptArray = conceptsArray1.split("/");
			int conceptId = Integer.valueOf(conceptArray[0]);
			if (chartTypeName.equals("graph")) {
				color = conceptArray[2];
			} else {
				color = null;
			}
			oldChartConcept = getMatchingOldChartConcept(conceptId, oldChartConcepts);
			if (oldChartConcept == null) {
				newChartConcept = new ChartConcept();
				newChartConcept.setChartId(chart.getId());
				newChartConcept.setColor(color);
				newChartConcept.setConceptId(conceptId);
				Context.getService(ChartConceptService.class).saveChartConcept(newChartConcept);
			} else {
				oldChartConcept.setVoided(Boolean.FALSE);
				oldChartConcept.setColor(color);
				System.out.println("\noldChartConcept voided: " + oldChartConcept.getVoided());
				Context.getService(ChartConceptService.class).saveChartConcept(oldChartConcept);
				oldChartConcepts.remove(oldChartConcept);
			}
		}
		// remove old chart concepts that are no longer required
		for (ChartConcept chartConcept : oldChartConcepts) {
			chartConcept.setVoided(Boolean.TRUE);
			Context.getService(ChartConceptService.class).saveChartConcept(chartConcept);
		}
	}
	
	private ChartConcept getMatchingOldChartConcept(int conceptId, List<ChartConcept> oldChartConcepts) {
		for (ChartConcept chartConcept : oldChartConcepts) {
			if (chartConcept.getConceptId() == conceptId) {
				return chartConcept;
			}
		}
		return null;
	}
}
