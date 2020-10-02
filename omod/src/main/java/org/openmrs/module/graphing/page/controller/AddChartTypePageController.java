/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.graphing.page.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.api.context.Context;
import org.openmrs.module.graphing.ChartType;
import org.openmrs.module.graphing.api.ChartTypeService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class AddChartTypePageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session) {
		List<ChartType> ctList = Context.getService(ChartTypeService.class).getAllChartTypes();
		model.addAttribute("charttypes", ctList);
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "newChartType", required = false) String newChartType) {
		newChartType = newChartType.trim();
		if (newChartType.length() == 0) {
			InfoErrorMessageUtil.flashInfoMessage(session, "Empty Chart Type");
			return "redirect:" + "graphing/addChartType.page";
		}
		List<ChartType> ctList = Context.getService(ChartTypeService.class).getAllChartTypes();
		for (ChartType ct : ctList) {
			if (ct.getName().equalsIgnoreCase(newChartType)) {
				InfoErrorMessageUtil.flashInfoMessage(session, "Duplicate Chart Type: " + newChartType);
				return "redirect:" + "graphing/addChartType.page";
			}
		}
		ChartType ct = new ChartType();
		ct.setDateCreated(new Date());
		ct.setName(newChartType);
		Context.getService(ChartTypeService.class).saveChartType(ct);
		InfoErrorMessageUtil.flashInfoMessage(session, "Chart Type Saved: " + newChartType);
		return "redirect:" + "graphing/addChartType.page";
		
	}
}
