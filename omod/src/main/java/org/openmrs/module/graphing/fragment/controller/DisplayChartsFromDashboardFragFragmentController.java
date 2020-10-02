package org.openmrs.module.graphing.fragment.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class DisplayChartsFromDashboardFragFragmentController {
	
	public void controller(HttpServletRequest request, FragmentModel model, HttpSession session,
	        @RequestParam("patientId") Patient patient) {
		model.addAttribute("patientId", patient.getPatientId());
		System.out.println("\n\n\n\n\n\nPATIENT ID: " + patient.getPatientId() + "\n\n\n\n\n\n");
	}
}
