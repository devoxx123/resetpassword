package com.resetPassword.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.resetPassword.model.Message;
import com.resetPassword.model.Person;
import com.resetPassword.service.PersonService;


@RestController
public class RestResource {

	@Autowired
	private PersonService personService;

	private static final String USER = "user";

	@RequestMapping(value = "test/signup", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
	public Response signup(@RequestBody Person person, @Context HttpServletRequest req, @Context HttpSession session) {
		session = req.getSession(true);
		session.setAttribute(USER, person);
		personService.signup(person);
		String[] names = person.getEmail().split("@");
		return Response.status(200).entity(new Message("Welcome " + names[0], null)).build();
	}

	@RequestMapping(value = "test/login", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
	public Response login(@RequestBody Person person, @Context HttpServletResponse res) {
		if (personService.login(person)) {
			String[] names = person.getEmail().split("@");
			return Response.status(200).entity(new Message("Welcome " + names[0], true)).build();
		}
		return Response.status(200).entity(new Message("*Wrong email or password", false)).build();
	}

	@RequestMapping(value = "test/forgotMyPassword", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
	public Response sendConformationMailTo(@RequestBody String sendConformationMailTo,
			@Context HttpServletResponse res) {
		if (personService.sendConformationMail(sendConformationMailTo, sendConformationMailTo)) {
			return Response.status(200)
					.entity(new Message("A confirmation link has been sent to your email address", null)).build();
		}
		return Response.status(200).entity(new Message("*This email is not registered in our website", null)).build();

	}

	@RequestMapping(path = "test/updateMyPassword/{token}", produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON, method = RequestMethod.POST)
	public Response updatePassword(@RequestBody String newPassword, @PathVariable("token") String token,
			@Context HttpServletRequest req, @Context HttpSession session) {
		session = req.getSession(false);
		Person customer = (Person) session.getAttribute(USER);
		if (personService.isEligible(customer, token)) {
			personService.updatePassword(customer.getEmail(), newPassword);
			return Response.status(200).entity(new Message("Your password has been reset successfully", null)).build();
		}
		return Response.status(200).entity(new Message("*Request not authorized", null)).build();
	}

}
