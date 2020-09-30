package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;

public class RegistrationCommand implements Command{
	
	
	private static final String PARAMETER_LOGIN = "login";
	private static final String PARAMETER_PASSWORD = "password";
	private static final String PARAMETER_EMAIL = "email";
	
	private static final String PARAMETER_NAME = "name";
	private static final String PARAMETER_SURNAME = "surname";
	private static final String PARAMETER_PATRONYMIC = "patronymic";
	private static final String PARAMETER_PHONE_NUMBER = "phone_number";
	
	private static final String PARAMETER_PASSPORT_SERIES = "passport_series";
	private static final String PARAMETER_PASSPORT_NUMBER = "passport_number";
	private static final String PARAMETER_DATE_OF_BIRTH = "date_of_birth";
	private static final String PARAMETER_ADDRESS = "address";
	private static final String PARAMETER_POST_CODE = "post_code";

	private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    private static final String GO_TO_AUTHENTICATION_PAGE = "controller?command=go_to_authentication_page";
    private static final String GO_TO_REGISTRATION_PAGE = "controller?command=go_to_registration_page";
    
    
    // TODO сделать нормально
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		// мб вынести в глобальные переменные
		ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();


        
		String login = request.getParameter(PARAMETER_LOGIN);
		String password = request.getParameter(PARAMETER_PASSWORD);
		String email = request.getParameter(PARAMETER_EMAIL);
		
        String name = request.getParameter(PARAMETER_NAME);
		String surname = request.getParameter(PARAMETER_SURNAME);
		String patronymic = request.getParameter(PARAMETER_PATRONYMIC);
		String phoneNumber = request.getParameter(PARAMETER_PHONE_NUMBER);
		
		String passportSeries = request.getParameter(PARAMETER_PASSPORT_SERIES);
		String passportNumber = request.getParameter(PARAMETER_PASSPORT_NUMBER);
		
		// проверку
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		String date = request.getParameter(PARAMETER_DATE_OF_BIRTH);
		LocalDate dateOfBirth = LocalDate.parse(date, formatter);
		
		String address = request.getParameter(PARAMETER_ADDRESS);
		String postCode = request.getParameter(PARAMETER_POST_CODE);

		
		
        RegistrationData registrationData = new RegistrationData();
        
        
        registrationData.setEmail(email);
        registrationData.setLogin(login);
        registrationData.setPassword(password);
        
        registrationData.setName(name);
        registrationData.setSurname(surname);
        registrationData.setPatronymic(patronymic);
        registrationData.setPhoneNumber(phoneNumber);

        registrationData.setPassportSeries(passportSeries);
        registrationData.setPassportNumber(passportNumber);
        registrationData.setDateOfBirth(dateOfBirth);
        registrationData.setAddress(address);
        registrationData.setPostCode(postCode);
        
        
        String page;
        
        try {
			if (service.registration(registrationData)) {
				// registration_successful
				page = GO_TO_AUTHENTICATION_PAGE;
			} else {
				// registration_unsuccessful
				page = GO_TO_REGISTRATION_PAGE; // нужен форвард ???
			}
			// if user_exist (from excepion messege ???)
			// page = GO_TO_REGISTRATION_PAGE; // нужен форвард??
		} catch (ServiceException e) {
			// TODO
			page = GO_TO_MAIN_PAGE;
		}
		response.sendRedirect(page);
    }
}