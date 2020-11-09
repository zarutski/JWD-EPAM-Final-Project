package by.epamtc.zarutski.controller.command.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.epamtc.zarutski.bean.RegistrationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.validation.UserValidation;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.UserExistsServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

public class RegistrationCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RegistrationCommand.class);

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

    private static final String DATE_FORMATTER_PATTERN = "yyyy-MM-d";
    private static final String DATE_OF_BIRTH_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";

    private static final String AMPERSAND = "&";
    private static final String PARAMETER_REGISTRATION_ERROR = "error=error_11";
    private static final String PARAMETER_SERVICE_ERROR = "error=error_12";
    private static final String PARAMETER_REGISTRATION_DATA_ERROR = "error=error_13";
    private static final String PARAMETER_USER_EXISTS_ERROR = "error=error_14";
    private static final String MESSAGE_REG_SUCCESS = "message=reg_success";

    private static final String GO_TO_AUTHENTICATION_PAGE = "controller?command=go_to_authentication_page";
    private static final String GO_TO_REGISTRATION_PAGE = "controller?command=go_to_registration_page";

    private static final String LOG_REGISTRATION_SUCCESSFUL = "User registration successful";
    private static final String LOG_WRONG_REGISTRATION_DATA = "Registration data format isn't correct";
    private static final String LOG_EMPTY_FIELDS = "Empty fields in registration form";
    private static final String LOG_ERROR_PARSING_DATE = "Error while parsing date field";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter(PARAMETER_LOGIN);
        String password = request.getParameter(PARAMETER_PASSWORD);
        String email = request.getParameter(PARAMETER_EMAIL);

        String name = request.getParameter(PARAMETER_NAME);
        String surname = request.getParameter(PARAMETER_SURNAME);
        String patronymic = request.getParameter(PARAMETER_PATRONYMIC);
        String phoneNumber = request.getParameter(PARAMETER_PHONE_NUMBER);
        String passportSeries = request.getParameter(PARAMETER_PASSPORT_SERIES);
        String passportNumber = request.getParameter(PARAMETER_PASSPORT_NUMBER);
        String address = request.getParameter(PARAMETER_ADDRESS);
        String postCode = request.getParameter(PARAMETER_POST_CODE);

        String date = request.getParameter(PARAMETER_DATE_OF_BIRTH);
        LocalDate dateOfBirth = null;

        if (isValidDateFormat(date)) {
            dateOfBirth = parseDate(date);
        }

        RegistrationData registrationData = new RegistrationData();

        registrationData.setEmail(email);
        registrationData.setLogin(login);
        registrationData.setPassword(password);

        registrationData.setName(name);
        registrationData.setSurname(surname);
        registrationData.setPatronymic(patronymic);
        registrationData.setPhoneNumber(phoneNumber);
        registrationData.setPassportSeries(passportSeries.toUpperCase());
        registrationData.setPassportNumber(passportNumber);
        registrationData.setDateOfBirth(dateOfBirth);
        registrationData.setAddress(address);
        registrationData.setPostCode(postCode);

        String page = GO_TO_REGISTRATION_PAGE;

        if (UserValidation.isRegistrationDataExists(registrationData)) {

            ServiceProvider provider = ServiceProvider.getInstance();
            UserService service = provider.getUserService();

            try {
                if (service.registration(registrationData)) {
                    logger.info(LOG_REGISTRATION_SUCCESSFUL);
                    page = GO_TO_AUTHENTICATION_PAGE + AMPERSAND + MESSAGE_REG_SUCCESS;
                } else {
                    page += AMPERSAND + PARAMETER_SERVICE_ERROR;
                }
            } catch (WrongDataServiceException e) {
                logger.info(LOG_WRONG_REGISTRATION_DATA, e);
                page = AMPERSAND + PARAMETER_REGISTRATION_DATA_ERROR;
            } catch (UserExistsServiceException e) {
                page = AMPERSAND + PARAMETER_USER_EXISTS_ERROR;
            } catch (ServiceException e) {
                page = AMPERSAND + PARAMETER_SERVICE_ERROR;
            }
        } else {
            logger.info(LOG_EMPTY_FIELDS);
            page = AMPERSAND + PARAMETER_REGISTRATION_ERROR;
        }

        response.sendRedirect(page);
    }

    private static boolean isValidDateFormat(String date) {
        return (date != null) && date.matches(DATE_OF_BIRTH_PATTERN);
    }

    private LocalDate parseDate(String date) {
        LocalDate dateOfBirth = null;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN);
            dateOfBirth = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            logger.warn(LOG_ERROR_PARSING_DATE);
        }
        return dateOfBirth;
    }
}