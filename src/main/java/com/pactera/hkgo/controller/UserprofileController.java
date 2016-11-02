package com.pactera.hkgo.controller;


import java.io.IOException;
import java.text.ParseException;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.pactera.hkgo.dao.UserprofileDAO;
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
public class UserprofileController {
	

	
	@Autowired
	private UserprofileDAO userprofileDAO;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@RequestMapping("/userprofiles")
	public ModelAndView userprofiles(ModelAndView model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException{
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
		if (userprofile.getRole().equals("Admin"))
		{
		session.setAttribute("adminConnected",true);
		}
		
		
		model.addObject("userprofile", userprofile);
		model.setViewName("userprofiles");
		
		return model;
	}
	
	@RequestMapping(value="/searchuserprofiles", method = RequestMethod.POST )
	public ModelAndView searchuserprofiles(ModelAndView model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException{
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
		if (userprofile.getRole().equals("Admin"))
		{
		session.setAttribute("adminConnected",true);
		}
		
		else{
			session.setAttribute("adminConnected", false);
			redirectAttributes.addFlashAttribute("error", "You don't have the access clearance to the requested page");
			return new ModelAndView("redirect:/userprofiles.html");
		}
		
		
		String search_email = request.getParameter("search_email");
		List<Userprofile> listUserprofile = userprofileDAO.list(search_email);
		model.addObject("listUserprofile", listUserprofile);
		model.setViewName("searchuserprofiles");
				
		return model;
	}
	
	@RequestMapping(value = "/editUserprofile", method = RequestMethod.GET)
	public ModelAndView editUserprofile(HttpServletRequest request, RedirectAttributes redirectAttributes) {
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
		if (userprofile.getRole().equals("Admin"))
		{
		session.setAttribute("adminConnected",true);
		}
		
		else{
			session.setAttribute("adminConnected", false);
			redirectAttributes.addFlashAttribute("error", "You don't have the access clearance to the requested page");
			return new ModelAndView("redirect:/userprofiles.html");
		}
		
		
		int userprofileId = Integer.parseInt(request.getParameter("id"));
		userprofile = userprofileDAO.get(userprofileId);
		System.out.println("In edit profile:"+userprofile.getEmail_address());
		session.setAttribute("selected_email", userprofile.getEmail_address());
		ModelAndView model = new ModelAndView("UserForm");
		model.addObject("userprofile", userprofile);
		System.out.println("In edit profile before model:"+userprofile.getEmail_address());
		return model;
	}
	
	@RequestMapping(value ="/saveUserprofile", method = RequestMethod.POST)
	public ModelAndView saveUserprofile(@ModelAttribute Userprofile userprofile, HttpServletRequest request, RedirectAttributes redirectAttributes) throws ParseException {
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
		
		Userprofile auserprofile = new Userprofile();
		String name = (String)session.getAttribute("userName");
		String email = (String)session.getAttribute("userEmail");
		
		auserprofile = userprofileDAO.getUserprofile(email,name);
		if (auserprofile.getRole().equals("Admin"))
		{
		session.setAttribute("adminConnected",true);
		}
		
		else{
			session.setAttribute("adminConnected", false);
			redirectAttributes.addFlashAttribute("error", "You don't have the access clearance to the requested page");
			return new ModelAndView("redirect:/userprofiles.html");
		}
		
		userprofileDAO.setUserprofile(userprofile);	
		//send email to notify 1 applicant about change
		// creates a simple e-mail object
		String recipientAddress = (String)session.getAttribute("selected_email");
		System.out.println(recipientAddress);
		String subject="Pactera Leave Management Notification";
		String message="Admin has updated for your userprofile.(Role,Annual Balance,MC Balance, Annual Taken, MC Taken)("
				+ userprofile.getRole()+", "+userprofile.getLeave_balance_annual()+", "+userprofile.getLeave_balance_mc()+", "
				+ userprofile.getAnnual_taken()+", "+userprofile.getLeave_balance_mc()+")";
		SimpleMailMessage emailwriter = new SimpleMailMessage();
        emailwriter.setTo(recipientAddress);
        emailwriter.setSubject(subject);
        emailwriter.setText(message);
        
        // sends the e-mail
        mailSender.send(emailwriter);
		//end of email notification
		
		
		return new ModelAndView("redirect:/userprofiles.html");

	}
	
	
}
