package by.epamtc.zarutski.controller.command.impl.go_to;

import by.epamtc.zarutski.bean.Account;
import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.bean.Operation;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.FacilitiesService;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class GoToAccountDetailsCommand implements Command {

    private static final String ACC_DETAILS_PAGE = "/WEB-INF/jsp/accountDetails.jsp";
    private static final String ACC_PAYMENTS_PAGE = "/WEB-INF/jsp/accTransfer.jsp";
    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";

    private static final String SESSION_AUTHENTICATION_DATA = "authentication_data";
    private static final String PARAMETER_ACC_ID = "account_id";
    private static final String PARAMETER_USER_ID = "user_id";

    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String MESSAGE_NO_ACCOUNTS = "no accounts";
    private static final String MESSAGE_NO_OPERATIONS = "no operations";
    private static final String MESSAGE_SERVICE_ERROR = "&message=service_error";

    private static final String PARAMETER_USER_OPERATIONS = "operations_history";
    private static final String PARAMETER_ACC = "account";

    private static final String PARAMETER_ACTION = "action";
    private static final String ACTION_PAYMENT = "payment";
    private static final String ACC_DESTINATION = "account";
    private static final String ROLE_ADMIN = "admin";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServiceProvider provider = ServiceProvider.getInstance();
        FacilitiesService service = provider.getFacilitiesService();

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(SESSION_AUTHENTICATION_DATA);


        int accId = Integer.parseInt(request.getParameter(PARAMETER_ACC_ID));
        int userId = getUserId(authenticationData, request);
        Account account = null;

        String action = request.getParameter(PARAMETER_ACTION);
        String page = getActionPage(action);

        try {

            account = service.getAccById(accId, userId);
            setAccAttribute(request, account);

            if (page.equals(ACC_DETAILS_PAGE)) {
                List<Operation> operations = service.getOperations(accId, ACC_DESTINATION);
                setOperationAttribute(request, operations);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } catch (WrongDataServiceException e) {
            response.sendRedirect(GO_TO_MAIN_PAGE);
        } catch (ServiceException e) {
            page += MESSAGE_SERVICE_ERROR;
            response.sendRedirect(page);
        }
    }

    private String getActionPage(String action) {
        String page = null;
        if (ACTION_PAYMENT.equals(action)) {
            page = ACC_PAYMENTS_PAGE;
        } else {
            page = ACC_DETAILS_PAGE;
        }

        return page;
    }

    private int getUserId(AuthenticationData authenticationData, HttpServletRequest request) {
        if (authenticationData.getUserRole().equals(ROLE_ADMIN)) {
            String userIdParameter = request.getParameter(PARAMETER_USER_ID);
            return Integer.parseInt(userIdParameter);
        } else {
            return authenticationData.getUserId();
        }
    }

    private void setOperationAttribute(HttpServletRequest request, List<Operation> operations) {
        if (operations == null) {
            // TODO -- все эти message в jsp обработать ---
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_OPERATIONS);
        } else {
            request.setAttribute(PARAMETER_USER_OPERATIONS, operations);
        }
    }

    private void setAccAttribute(HttpServletRequest request, Account account) {
        if (account == null) {
            // TODO -- все эти message в jsp обработать --- command=go_to_account_details
            request.setAttribute(ATTRIBUTE_MESSAGE, MESSAGE_NO_ACCOUNTS);
        } else {
            request.setAttribute(PARAMETER_ACC, account);
        }
    }
}
