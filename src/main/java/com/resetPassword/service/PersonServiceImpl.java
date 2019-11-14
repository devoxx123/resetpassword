package com.resetPassword.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.resetPassword.model.MailMessageRequest;
import com.resetPassword.model.Person;
import com.resetPassword.repository.PersonRepository;
import com.resetPassword.utils.Util;

@Service
@Component
public class PersonServiceImpl implements PersonService {

	Map<Person, String> tokenRegister = new HashMap<>();

	@Autowired
	private PersonRepository personRepository;

	@Override
	public boolean signup(Person person) {
		String newPassword = Util.encryptSHY2(person.getPassword());
		person.setPassword(newPassword);
		personRepository.save(person);
		return true;
	}

	@Override
	public boolean login(Person person) {
		String newPass = Util.encryptSHY2(person.getPassword());
		Person person2 = personRepository.login(person.getEmail(), newPass);
		if (person2 != null) {
			return true;
		}
		return false;
	}

	@Override
	public boolean updatePassword(String email, String password) {
		Person temp = personRepository.getPersonByEmail(email);
		temp.setPassword(Util.encryptSHY2(password));
		personRepository.save(temp);
		return true;
	}

	@Override
	public boolean sendConformationMail(String sendConformationMailTo, String username) {
		tokenRegister.clear();
		Person tempPrson = personRepository.getPersonByEmail(sendConformationMailTo);
		if (tempPrson != null) {
			String token = Util.getSaltString();
			MailMessageRequest message = new MailMessageRequest(sendConformationMailTo, "Change password for paeew App",
					"<h1>To reset your password please click on the button below</h1></br><a href=\"http://localhost:8090/reset.html?"
							+ token + "\">link</a>",
					"donotrelplay@paeew.com");
			Util.sendMail(message);
			tokenRegister.put(tempPrson, token);
			return true;
		}
		return false;
	}

	@Override
	public boolean isEligible(Person customer, String token) {
		String tokenInMap = tokenRegister.get(customer);
		return token.equals(tokenInMap);
	}

}
