package org.openmrs.module.graphing.fragment.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.time.DateUtils;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.ChartConcept;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.api.ChartConceptService;
import org.openmrs.module.graphing.api.ChartService;
import org.openmrs.module.graphing.api.ChartTypeService;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class DisplayChartsFragFragmentController {
	
	public void controller(HttpServletRequest request, FragmentModel model, HttpSession session,
	        @RequestParam("patientId") Patient patient) {
		model.addAttribute("patientId", patient.getPatientId());
		List<Chart> charts = Context.getService(ChartService.class).getAllCharts();
		ChartInfo c;
		ArrayList<ChartInfo> chartInfos = new ArrayList<ChartInfo>();
		for (Chart chart : charts) {
			System.out.println("\n\n\nXXXXXXXXXXchart.getVoided()" + chart.getVoided());
			if (chart.getVoided() == false) {
				chartInfos.add(c = new ChartInfo(chart, patient));
				System.out.println("\n\nADDING CHART TYPE: " + c.getChartTypeName());
			}
		}
		System.out.println("\n\n\nDisplayChartsFragFragmentController: " + chartInfos.size());
		model.addAttribute("chartInfos", chartInfos);
	}
}
