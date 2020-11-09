package by.epamtc.zarutski.controller.command.impl;

import by.epamtc.zarutski.bean.AuthenticationData;
import by.epamtc.zarutski.controller.command.Command;
import by.epamtc.zarutski.service.ServiceProvider;
import by.epamtc.zarutski.service.UserService;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;

public class UserPhotoUploadCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UserPhotoUploadCommand.class);

    private static final String PARAMETER_AUTHENTICATION_DATA = "authentication_data";

    public static final String PARAMETER_WRONG_EXTENSION_ERROR = "&photo_message=wrong_extension";
    private static final String PARAMETER_SERVICE_ERROR = "&photo_message=service_error";
    private static final String PARAMETER_UPLOAD_ERROR = "&photo_message=upload_error";
    private static final String PARAMETER_ONLY_FILE_ERROR = "&photo_message=only_file";

    private static final String GO_TO_MAIN_PAGE = "controller?command=go_to_main_page";
    public static final String GO_TO_PERSONAL_AREA = "controller?command=go_to_personal_area";

    public static final String LOG_WRONG_EXTENSION = "Wrong file extension";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        AuthenticationData authenticationData = (AuthenticationData) session.getAttribute(PARAMETER_AUTHENTICATION_DATA);
        String page = GO_TO_MAIN_PAGE;

        ServiceProvider provider = ServiceProvider.getInstance();
        UserService service = provider.getUserService();
        page = GO_TO_PERSONAL_AREA;

        if (ServletFileUpload.isMultipartContent(request)) {

            try {
                FileItem item = getPhotoItem(request);
                int userId = authenticationData.getUserId();

                if (item.getSize() != 0) {
                    service.uploadUserPhoto(item, userId);
                }
            } catch (WrongDataServiceException e) {
                page += PARAMETER_WRONG_EXTENSION_ERROR;
                logger.info(LOG_WRONG_EXTENSION, e);
            } catch (ServiceException e) {
                page += PARAMETER_SERVICE_ERROR;
            } catch (Exception e) {
                page += PARAMETER_UPLOAD_ERROR;
            }
        } else {
            page += PARAMETER_ONLY_FILE_ERROR;
        }

        response.sendRedirect(page);
    }

    private FileItem getPhotoItem(HttpServletRequest request) throws Exception {
        List<FileItem> multiParts = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
        FileItem fileItem = null;

        for (FileItem item : multiParts) {
            if (!item.isFormField()) {
                fileItem = item;
            }
        }

        return fileItem;
    }
}
