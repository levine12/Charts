/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.graphing.page.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.api.ChartConceptService;
import org.openmrs.module.graphing.api.ChartService;
import org.openmrs.module.graphing.api.ChartTypeService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class AddChartPageController {
	
	public void controller(HttpServletRequest request, PageModel model,
	        @SpringBean("appFrameworkService") AppFrameworkService appFrameworkServiceHttpSession, HttpSession session) {
		List<ChartType> ctList = Context.getService(ChartTypeService.class).getAllChartTypes();
		List<Chart> charts = Context.getService(ChartService.class).getAllCharts();
		List<ChartInfo> chartInfos = new ArrayList<ChartInfo>();
		for (Chart chart : charts) {
			if (!chart.getVoided()) {
				chartInfos.add(new ChartInfo(chart));
			}
		}
		model.addAttribute("charttypes", ctList);
		model.addAttribute("chartInfos", chartInfos);
	}
	
	private void getExtensionPoints(AppFrameworkService appFrameworkService) {
		List<Extension> exts = appFrameworkService.getAllEnabledExtensions();
		System.out.println("EXTENSIONS");
		for (Extension ext : exts) {
			System.out.println(ext.getExtensionPointId());
		}
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "chartTitle", required = false) String chartTitle,
	        @RequestParam(value = "chartTypeIdStr", required = false) String chartTypeIdStr,
	        @RequestParam(value = "xAxisLabel", required = false) String xAxisLabel,
	        @RequestParam(value = "yAxisLabel", required = false) String yAxisLabel,
	        @RequestParam(value = "conceptIdsColors", required = false) String conceptIdsColors) {
		chartTitle = chartTitle.trim();
		
		xAxisLabel = xAxisLabel.trim();
		yAxisLabel = yAxisLabel.trim();
		
		conceptIdsColors = conceptIdsColors.trim();
		if (chartTitle.equals("")) {
			InfoErrorMessageUtil.flashInfoMessage(session, "Chart Title is Required");
			return "redirect:" + "graphing/addChart.page";
		}
		System.out.println("\n\n\nCHART TYPE: " + chartTypeIdStr + " conceptIdsColors: " + conceptIdsColors + "\n\n");
		if (chartTypeIdStr.equals("") || chartTypeIdStr.equals("0")) {
			InfoErrorMessageUtil.flashInfoMessage(session, "Chart Type is Required");
			return "redirect:" + "graphing/addChart.page";
		}
		
		int chartTypeId = Integer.valueOf(chartTypeIdStr);
		String chartTypeStr = getChartTypeStr(chartTypeId);
		
		Chart chart = new Chart();
		chart.setTitle(chartTitle);
		chart.setXaxisLabel(xAxisLabel);
		chart.setYaxisLabel(yAxisLabel);
		chart.setDateCreated(new Date());
		chart.setchartTypeId(chartTypeId);
		
		if (chartTypeStr.contains("recent visits") || chartTypeStr.contains("bmi")) {
			Context.getService(ChartService.class).saveChart(chart);
			return "redirect:" + "graphing/addChart.page";
		}
		
		if (conceptIdsColors.equals("")) {
			InfoErrorMessageUtil.flashInfoMessage(session, "Concepts are Required");
			return "redirect:" + "graphing/addChart.page";
		}
		
		if (chartTypeStr.contains("health trend")) {
			saveChartAndConcepts(session, chart, conceptIdsColors, "health trend");
		} else {
			// line graph
			saveChartAndConcepts(session, chart, conceptIdsColors, "graphing");
		}
		return "redirect:" + "graphing/addChart.page";
		
	}
	
	private String getChartTypeStr(int chartTypeId) {
		List<ChartType> ctList = Context.getService(ChartTypeService.class).getAllChartTypes();
		for (ChartType cType : ctList) {
			if (cType.getId() == chartTypeId) {
				return cType.getName().toLowerCase();
			}
		}
		return null;
	}
	
	private void saveChartAndConcepts(HttpSession session, Chart chart, String conceptIdsColors, String chartTypeName) {
		// conceptIdsColors are provided as: 5089/blue,5090/red
		if (chartTypeName.contains("graphing")) {
			if (!areAllConceptsNumeric(conceptIdsColors)) {
				InfoErrorMessageUtil.flashInfoMessage(session, "Invalid Concept");
				return;
			}
		} else {
			if (!doAllConceptsExistForHealthTrend(conceptIdsColors)) {
				InfoErrorMessageUtil.flashInfoMessage(session, "Invalid Concept");
				return;
			}
		}
		
		chart = Context.getService(ChartService.class).saveChart(chart);
		
		String[] conceptIdColors = conceptIdsColors.split(",");
		ChartConcept chartConcept;
		int conceptId;
		for (String conceptIdColor : conceptIdColors) {
			chartConcept = new ChartConcept();
			chartConcept.setChartId(chart.getId());
			String color = null;
			if (chartTypeName.contains("health trend")) { // it's only a list of concept ids
				conceptId = Integer.valueOf(conceptIdColor);
			} else { // list of conceptId/color
				String[] cIdColorArray = conceptIdColor.split("/");
				conceptId = Integer.valueOf(cIdColorArray[0]);
				color = cIdColorArray[1];
			}
			
			chartConcept.setConceptId(conceptId);
			chartConcept.setColor(color);
			Context.getService(ChartConceptService.class).saveChartConcept(chartConcept);
		}
		
	}
	
	private boolean doAllConceptsExistForHealthTrend(String conceptIds) { // colors are not included
		String[] conIds = conceptIds.split(",");
		for (String conceptIdStr : conIds) {
			int conceptId = Integer.valueOf(conceptIdStr);
			Concept concept = Context.getConceptService().getConcept(conceptId);
			if (concept == null) {
				return false;
			}
		}
		return true;
	}
	
	private boolean areAllConceptsNumeric(String conceptIdsColors) {
		String[] conceptIdColors = conceptIdsColors.split(",");
		for (String conceptColor : conceptIdColors) {
			String[] cIdColorArray = conceptColor.split("/");
			int conceptId = Integer.valueOf(cIdColorArray[0]);
			String conceptDatatype = getConceptDatatype(conceptId);
			if (conceptDatatype == null) {
				return false;
			}
			if (!conceptDatatype.equals("Numeric")) {
				return false;
			}
		}
		return true;
	}
	
	private String getConceptDatatype(int conceptId) {
		Concept concept = Context.getConceptService().getConcept(conceptId);
		if (concept == null) {
			return null;
		}
		return concept.getDatatype().getName();
	}
}

class ChartInfo {
	
	private int id, chartTypeId;
	
	private String conceptNames = "";
	
	private String xaxisLabel, yaxisLabel, title, chartTypeName;
	
	public ChartInfo(Chart chart) {
		id = chart.getId();
		xaxisLabel = chart.getXaxisLabel();
		if ((xaxisLabel == null) || (xaxisLabel.equals(""))) {
			xaxisLabel = "";
		}
		yaxisLabel = chart.getYaxisLabel();
		if ((yaxisLabel == null) || (yaxisLabel.equals(""))) {
			yaxisLabel = "";
		}
		title = chart.getTitle();
		chartTypeId = chart.getchartTypeId();
		List<ChartType> chartTypes = Context.getService(ChartTypeService.class).getAllChartTypes();
		for (ChartType chartType : chartTypes) {
			if (chartType.getId() == chartTypeId) {
				this.chartTypeName = chartType.getName();
				break;
			}
		}
		List<ChartConcept> chartConcepts = Context.getService(ChartConceptService.class).getAllChartConceptsForChart(id);
		//System.out.println("\n\n\nCONCEPTS NUMBER: " + chartConcepts.size() + "\n\n");
		for (ChartConcept chartConcept : chartConcepts) {
			int conceptId = chartConcept.getConceptId();
			
			conceptNames += ", " + Context.getConceptService().getConcept(conceptId).getDisplayString();
			if (chartTypeName.toLowerCase().contains("graph")) {
				conceptNames += "/" + chartConcept.getColor();
			}
			conceptNames = conceptNames.substring(1);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getConceptNames() {
		return conceptNames;
	}
	
	public String getXaxisLabel() {
		return xaxisLabel;
	}
	
	public String getYaxisLabel() {
		return yaxisLabel;
	}
	
	public String getTitle() {
		return title;
	}
	
}
