/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.graphing.page.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class DisplayChartsPageController {
	
	public void controller(HttpServletRequest request, PageModel model, HttpSession session,
	        @RequestParam("patientId") Patient patient) {
		model.addAttribute("patName", patient.getGivenName() + " " + patient.getFamilyName());
	}
}
