package org.openmrs.module.graphing.fragment.controller;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.graphing.Chart;
import org.openmrs.module.graphing.api.ChartService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class AddRemoveChartsFragFragmentController {
	
	public void controller(FragmentModel model) {
		List<Chart> charts = Context.getService(ChartService.class).getAllCharts();
		List<ChartInfo> chartInfos = new ArrayList<ChartInfo>();
		for (Chart chart : charts) {
			if (!chart.isVoided()) {
				chartInfos.add(new ChartInfo(chart, null));
			}
		}
		model.addAttribute("chartInfos", chartInfos);
	}
	
	public List<SimpleObject> deleteChart(@RequestParam(value = "chartIdForDelete", required = false) int chartIdForDelete,
	        UiUtils ui) {
		String[] properties = new String[0];
		System.out.println("\n\nDELETING CHART ID: " + chartIdForDelete);
		Chart chart = Context.getService(ChartService.class).getChart(chartIdForDelete);
		chart.setVoided(Boolean.TRUE);
		Context.getService(ChartService.class).saveChart(chart);
		return SimpleObject.fromCollection(null, ui, properties);
	}
}
