package com.pactera.hkgo.controller;


import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pactera.hkgo.dao.ApplyleaveDAO;
import com.pactera.hkgo.service.Applyleave;
import com.pactera.hkgo.service.Userprofile;
import com.pactera.hkgo.dao.UserprofileDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pactera.hkgo.auth.TokenResponse;


@Controller
public class ApproveleavesController {
	

	
	@Autowired
	private ApplyleaveDAO applyleaveDAO;
	
	@Autowired
	private UserprofileDAO userprofileDAO;
	
	@Autowired
	private JavaMailSender mailSender;	
		
	@RequestMapping("/approveleaves")
	public ModelAndView listPendingleave(ModelAndView model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException{
		HttpSession session = request.getSession();
		TokenResponse tokens = (TokenResponse)session.getAttribute("tokens");
		if (tokens == null) {
			// No tokens in session, user needs to sign in
			redirectAttributes.addFlashAttribute("error", "Please sign in to continue.");
			return new ModelAndView("redirect:/");
		}
		
		Date now = new Date();
		if (now.after(tokens.getExpirationTime())) {
			// Token expired
			redirectAttributes.addFlashAttribute("error", "The access token has expired. Please logout and re-login.");
			return new ModelAndView("redirect:/");
		}
		
		String name = (String)session.getAttribute("userName");
		String email = (String)session.getAttribute("userEmail");
		session.setAttribute("isApprovetab","Yes");
		
		Userprofile userprofile = userprofileDAO.getUserprofile(email,name);
		if (userprofile.getRole().equals("Admin"))
		{
		session.setAttribute("adminConnected",true);
		}
		
		else{
			session.setAttribute("adminConnected", false);
			redirectAttributes.addFlashAttribute("error", "You don't have the access clearance to the requested page");
			return new ModelAndView("redirect:/userprofiles.html");
		}
		
		List<Applyleave> listApplyleave = applyleaveDAO.list("Pending");
		model.addObject("listApplyleave", listApplyleave);
		model.setViewName("approveleaves");
		
		return model;
	}
	
	@RequestMapping(value = "/approveApplyleave", method = RequestMethod.GET)
	public ModelAndView approveApplyleave(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		HttpSession session = request.getSession();
		TokenResponse tokens = (TokenResponse)session.getAttribute("tokens");
		if (tokens == null) {
			// No tokens in session, user needs to sign in
			redirectAttributes.addFlashAttribute("error", "Please sign in to continue.");
			return new ModelAndView("redirect:/");
		}
		
		Date now = new Date();
		if (now.after(tokens.getExpirationTime())) {
			// Token expired
			redirectAttributes.addFlashAttribute("error", "The access token has expired. Please logout and re-login.");
			return new ModelAndView("redirect:/");
		}
		
		String name = (String)session.getAttribute("userName");
		String email = (String)session.getAttribute("userEmail");
		
		
		Userprofile userprofile = userprofileDAO.getUserprofile(email,name);
		if (userprofile.getRole().equals("Admin")){
			session.setAttribute("adminConnected",true);
		}
		
		else{
			session.setAttribute("adminConnected", false);
			redirectAttributes.addFlashAttribute("error", "You don't have the access clearance to the requested page");
			return new ModelAndView("redirect:/userprofiles.html");
		}
				
		int applyleaveId = Integer.parseInt(request.getParameter("id"));
		//get applicant info
		Applyleave applyleave = applyleaveDAO.get(applyleaveId);
		
		userprofile= userprofileDAO.getUserprofile(applyleave.getApply_email(), name);
		// end of get applicant info
		
		//start of leave balance check before approve
				if (applyleave.getApplytype().equals("Annual")){
					if(userprofile.getLeave_balance_annual()<applyleave.getDays()){
						applyleaveDAO.delete(applyleaveId);
						redirectAttributes.addFlashAttribute("error", "The applicant has insufficient annual leave balance and the application is denied");
						return new ModelAndView("redirect:/approveleaves.html");				
					}
					else{//deduct annual leave balance
						userprofile.setLeave_balance_annual(userprofile.getLeave_balance_annual()-applyleave.getDays());
						userprofile.setAnnual_taken(userprofile.getAnnual_taken()+applyleave.getDays());
						
					}
				}		
			
				if (applyleave.getApplytype().equals("MC")){
					if(userprofile.getLeave_balance_mc()<applyleave.getDays()){
						applyleaveDAO.delete(applyleaveId);
						redirectAttributes.addFlashAttribute("error", "The applicant has insufficient MC leave balance and the application is denied");
						return new ModelAndView("redirect:/approveleaves.html");				
					}
					else{//deduct mc balance
						userprofile.setLeave_balance_mc(userprofile.getLeave_balance_mc()-applyleave.getDays());
						userprofile.setMc_taken(userprofile.getMc_taken()+applyleave.getDays());
					}
				}
				//end of leave balance check before approve
		

		//send email to notify applicant about approval
		// creates a simple e-mail object
		String recipientAddress=applyleave.getApply_email();
		String subject="Pactera Leave Management Notification";
		String message="Admin has approved your "+applyleave.getApplytype()+" leaves starting from "+applyleave.getApplyfrom()+" to "+applyleave.getApplyto()+".";
		SimpleMailMessage emailwriter = new SimpleMailMessage();
		emailwriter.setTo(recipientAddress);
		emailwriter.setSubject(subject);
		emailwriter.setText(message);
		         
		// sends the e-mail
		mailSender.send(emailwriter);
		//end of email notification
				
		userprofileDAO.setUserprofile(userprofile);
		applyleaveDAO.approve(applyleaveId);
		return new ModelAndView("redirect:/approveleaves.html");
		
	}
	
}
