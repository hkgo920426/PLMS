package com.pactera.hkgo.controller;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pactera.hkgo.dao.ApplyleaveDAO;
import com.pactera.hkgo.dao.UserprofileDAO;
import com.pactera.hkgo.service.Applyleave;
import com.pactera.hkgo.service.Userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pactera.hkgo.auth.TokenResponse;


@Controller
public class ApplyleavesController {
	

	
	@Autowired
	private ApplyleaveDAO applyleaveDAO;
	
	@Autowired
	private UserprofileDAO userprofileDAO;	
	
	@Autowired
	private JavaMailSender mailSender;
		
	@RequestMapping("/applyleaves")
	public ModelAndView listApplyleave(ModelAndView model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException{
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
		session.setAttribute("isApprovetab","No");
		
		Userprofile userprofile = userprofileDAO.getUserprofile(email,name);
		if(userprofile.getRole()=="Admin"){
			session.setAttribute("adminConnected",true);
		}
		List<Applyleave> listApplyleave = applyleaveDAO.list(email);
		model.addObject("listApplyleave", listApplyleave);
		model.setViewName("applyleaves");
		
		return model;
	}
	
	@RequestMapping(value ="/newApplyleave", method = RequestMethod.GET)
	public ModelAndView newApplyleave(ModelAndView model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
			// TODO: Use the refresh token to request a new token from the token endpoint
			// For now, just complain
			redirectAttributes.addFlashAttribute("error", "The access token has expired. Please logout and re-login.");
			return new ModelAndView("redirect:/");
		}
		
		String name = (String)session.getAttribute("userName");
		String email = (String)session.getAttribute("userEmail");
		
		Userprofile userprofile = userprofileDAO.getUserprofile(email,name);
		if(userprofile.getRole()=="Admin"){
			session.setAttribute("adminConnected",true);
		}
	
		Applyleave newApplyleave = new Applyleave();
		model.addObject("applyleave", newApplyleave);
		model.setViewName("ApplyForm");
		return model;
	}
	
	@RequestMapping(value ="/saveApplyleave", method = RequestMethod.POST)
	public ModelAndView saveApplyleave(@ModelAttribute Applyleave applyleave, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ParseException {
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
		if(userprofile.getRole()=="Admin"){
			session.setAttribute("adminConnected",true);
		}
		
		//check for applyform status (restrict it from being set to null value)
	    if (applyleave.getApplyfrom()==null){
	    	redirectAttributes.addFlashAttribute("error", "Please fill in the date.");
			return new ModelAndView("redirect:/newApplyleave");				
			
		}	
	    if (applyleave.getApplyto()==null){
	    	redirectAttributes.addFlashAttribute("error", "Please fill in the date.");
			return new ModelAndView("redirect:/newApplyleave");				
			
		}	
	    if (applyleave.getApplytype()==null){
	    	redirectAttributes.addFlashAttribute("error", "Please select the type of leave you wish to apply.");
			return new ModelAndView("redirect:/newApplyleave");				
			
		}	
	    //end of check for applyform status
		
		//start of days calculation
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	    Date date1 = df.parse(applyleave.getApplyfrom());
	    Date date2 = df.parse(applyleave.getApplyto());
	    Calendar cal1 = Calendar.getInstance();
	    Calendar cal2 = Calendar.getInstance();
	    cal1.setTime(date1);
	    cal2.setTime(date2);

	    int numberOfDays = 1;
	    while (cal1.before(cal2)) {
	        if ((Calendar.SATURDAY != cal1.get(Calendar.DAY_OF_WEEK))
	           &&(Calendar.SUNDAY != cal1.get(Calendar.DAY_OF_WEEK))) {
	            numberOfDays++;
	        }
	        cal1.add(Calendar.DATE,1);
	    }
	    applyleave.setDays(numberOfDays);
	    //end of days calculation
		
	    
	   	//start of leave balance check before apply
		if (applyleave.getApplytype().equals("Annual")){
			if(userprofile.getLeave_balance_annual()<numberOfDays){
				redirectAttributes.addFlashAttribute("error", "Insufficient applicant's annual leave balance.");
				return new ModelAndView("redirect:/applyleaves.html");				
			}
		}		
	
		if (applyleave.getApplytype().equals("MC")){
			if(userprofile.getLeave_balance_mc()<numberOfDays){
				redirectAttributes.addFlashAttribute("error", "Insufficient applicant's MC balance.");
				return new ModelAndView("redirect:/applyleaves.html");				
			}
		}
		//end of leave balance check before apply
		
		//send email to notify all admins for approval
		// creates a simple e-mail object
		String recipientAddress;
		String subject="Pactera Leave Management Notification";
		String message=userprofile.getEmail_address()+" has applied for "+applyleave.getApplytype()+" leave starting from "+applyleave.getApplyfrom()+" to "+applyleave.getApplyto()+".";
		SimpleMailMessage emailwriter = new SimpleMailMessage();
        
		//generate list of admin users in list form
		List<String> adminlist = userprofileDAO.getAdminlist();
		for (int i=0; i<adminlist.size(); i++){
		   recipientAddress=adminlist.get(i);
		   emailwriter.setTo(recipientAddress);
	       emailwriter.setSubject(subject);
	       emailwriter.setText(message);
	       mailSender.send(emailwriter);
		}
		//end of email notification
		
		applyleaveDAO.saveOrUpdate(applyleave, email);		
		return new ModelAndView("redirect:/applyleaves.html");

	}
	
	@RequestMapping(value = "/deleteApplyleave", method = RequestMethod.GET)
	public ModelAndView deleteApplyleave(HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
			// TODO: Use the refresh token to request a new token from the token endpoint
			// For now, just complain
			redirectAttributes.addFlashAttribute("error", "The access token has expired. Please logout and re-login.");
			return new ModelAndView("redirect:/");
		}
		
		String name = (String)session.getAttribute("userName");
		String email = (String)session.getAttribute("userEmail");
		
		Userprofile userprofile = userprofileDAO.getUserprofile(email,name);
		if(userprofile.getRole()=="Admin"){
			session.setAttribute("adminConnected",true);
		}
		
		int applyleaveId = Integer.parseInt(request.getParameter("id"));
				
		Applyleave applyleave = applyleaveDAO.get(applyleaveId);
		if(applyleave.getApplystatus().equals("Approved")){
			//get applicant info
			userprofile= userprofileDAO.getUserprofile(applyleave.getApply_email(), name);
			// end of get applicant info
		
				if (applyleave.getApplytype().equals("Annual")){
					userprofile.setLeave_balance_annual(userprofile.getLeave_balance_annual()+applyleave.getDays());
					userprofile.setAnnual_taken(userprofile.getAnnual_taken()-applyleave.getDays());
						
					
				}		
			
				if (applyleave.getApplytype().equals("MC")){
					userprofile.setLeave_balance_mc(userprofile.getLeave_balance_mc()+applyleave.getDays());
					userprofile.setMc_taken(userprofile.getMc_taken()-applyleave.getDays());
					
				}
				userprofileDAO.setUserprofile(userprofile);
				
		}
		//send email to notify applicant about denial
		// creates a simple e-mail object
		String recipientAddress;
		
		String subject="Pactera Leave Management Notification";
		String message;
		if (session.getAttribute("isApprovetab").equals("Yes")){
			recipientAddress=applyleave.getApply_email();
			message="Admin has denied your "+applyleave.getApplytype()+" leaves starting from "+applyleave.getApplyfrom()+" to "+applyleave.getApplyto()+".";
			SimpleMailMessage emailwriter = new SimpleMailMessage();
			emailwriter.setTo(recipientAddress);
			emailwriter.setSubject(subject);
			emailwriter.setText(message);
			         
			// sends the e-mail
			mailSender.send(emailwriter);
		}
		else{
			if (applyleave.getApplystatus().equals("Approved"))
				//generate message: Approved leave cancelled
				message=applyleave.getApply_email()+" cancelled his/her approved "+applyleave.getApplytype()+" leaves starting from "+applyleave.getApplyfrom()+" to "+applyleave.getApplyto()+".";
			else
				//generate message: Pending leave cancelled
				message=applyleave.getApply_email()+" cancelled his/her	pending "+applyleave.getApplytype()+" leaves starting from "+applyleave.getApplyfrom()+" to "+applyleave.getApplyto()+".";
			
			SimpleMailMessage emailwriter = new SimpleMailMessage();
			
			//generate list of admin users in list form
			List<String> adminlist = userprofileDAO.getAdminlist();
			for (int i=0; i<adminlist.size(); i++){
			   recipientAddress=adminlist.get(i);
			   emailwriter.setTo(recipientAddress);
		       emailwriter.setSubject(subject);
		       emailwriter.setText(message);
		       mailSender.send(emailwriter);
			}
			//end of email notification

		}
			
		         
		
		//end of restore leave balance after delete
		
		applyleaveDAO.delete(applyleaveId);
		if (session.getAttribute("isApprovetab").equals("Yes")){
			return new ModelAndView("redirect:/approveleaves.html");			
		}
		else{
			return new ModelAndView("redirect:/applyleaves.html");			
		}
		
	}
	
	@RequestMapping(value = "/editApplyleave", method = RequestMethod.GET)
	public ModelAndView editApplyleave(HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
		if(userprofile.getRole()=="Admin"){
			session.setAttribute("adminConnected",true);
		}
		
		int applyleaveId = Integer.parseInt(request.getParameter("id"));
		Applyleave applyleave = applyleaveDAO.get(applyleaveId);
		ModelAndView model = new ModelAndView("ApplyForm");
		model.addObject("applyleave", applyleave);
		
		return model;
	}
	
	
	
}
