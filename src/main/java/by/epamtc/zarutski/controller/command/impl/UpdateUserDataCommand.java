package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.bean.UpdateUserData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.controller.validation.UserValidation;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateUserDataCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateUserDataCommand.class);

    private static final String PARAMETER_USER_ID = "user_id";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_SURNAME = "surname";
    private static final String PARAMETER_PATRONYMIC = "patronymic";
    private static final String PARAMETER_PHONE_NUMBER = "phone_number";
    private static final String PARAMETER_ADDRESS = "address";
    private static final String PARAMETER_POST_CODE = "post_code";

    private static final String PARAMETER_SERVICE_ERROR = "&message=service_error";
    private static final String PARAMETER_INPUT_DATA_FORMAT = "&message=input_data";

    private static final String LOG_WRONG_UPDATE_DATA = "Update data format isn't correct";
    private static final String LOG_SUCCESSFUL = "User data update finished successfully";

    private static final String GO_TO_PERSONAL_AREA = "controller?command=go_to_personal_area";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String page = GO_TO_PERSONAL_AREA;

        String parameterUserId = request.getParameter(PARAMETER_USER_ID);
        int userId = Integer.parseInt(parameterUserId);
        String name = request.getParameter(PARAMETER_NAME);
        String surname = request.getParameter(PARAMETER_SURNAME);
        String patronymic = request.getParameter(PARAMETER_PATRONYMIC);
        String phoneNumber = request.getParameter(PARAMETER_PHONE_NUMBER);
        String address = request.getParameter(PARAMETER_ADDRESS);
        String postCode = request.getParameter(PARAMETER_POST_CODE);

        UpdateUserData userData = new UpdateUserData();
        userData.setUserId(userId);
        userData.setName(name);
        userData.setSurname(surname);
        userData.setPatronymic(patronymic);
        userData.setPhoneNumber(phoneNumber);
        userData.setAddress(address);
        userData.setPostCode(postCode);

        if (UserValidation.isUserDataExists(userData)) {

            ServiceProvider provider = ServiceProvider.getInstance();
            UserService service = provider.getUserService();

            try {
                service.updateUser(userData);
                logger.info(LOG_SUCCESSFUL);
            } catch (WrongDataServiceException e) {
                page += PARAMETER_INPUT_DATA_FORMAT;
            } catch (ServiceException e) {
                page += PARAMETER_SERVICE_ERROR;
            }
        } else {
            logger.info(LOG_WRONG_UPDATE_DATA);
            page += PARAMETER_INPUT_DATA_FORMAT;
        }

        response.sendRedirect(page);
    }
}