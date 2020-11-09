package by.epamtc.zarutski.service.impl;

import by.epamtc.zarutski.bean.AccOrderData;
import by.epamtc.zarutski.bean.CardOrderData;
import by.epamtc.zarutski.bean.TransferData;
import by.epamtc.zarutski.service.FacilityActionService;
import by.epamtc.zarutski.service.exception.InsufficientFundsServiceException;
import by.epamtc.zarutski.service.exception.ServiceException;
import by.epamtc.zarutski.service.exception.WrongDataServiceException;
import by.epamtc.zarutski.service.impl.generation.FacilityDataGenerator;
import by.epamtc.zarutski.service.validation.OperationValidator;
import by.epamtc.zarutski.service.validation.OrderValidator;
import by.epamtc.zarutski.dao.DAOProvider;
import by.epamtc.zarutski.dao.FacilityActionDAO;
import by.epamtc.zarutski.dao.exception.ConfirmationDAOException;
import by.epamtc.zarutski.dao.exception.DAOException;
import by.epamtc.zarutski.dao.exception.InsufficientFundsDaoException;
import by.epamtc.zarutski.dao.exception.WrongDataDAOException;

public class FacilityActionServiceImpl implements FacilityActionService {

    private static final String WRONG_INPUT_DATA_MESSAGE = "Wrong data input";

    @Override
    public boolean transfer(TransferData transferData) throws ServiceException {

        if (!OperationValidator.transferDataValidation(transferData)) {
            throw new WrongDataServiceException(WRONG_INPUT_DATA_MESSAGE);
        }

        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityActionDAO dao = daoProvider.getFacilityActionDAO();

        try {
            return dao.transfer(transferData);
        } catch (ConfirmationDAOException e) {
            throw new WrongDataServiceException();
        } catch (InsufficientFundsDaoException e) {
            throw new InsufficientFundsServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean orderNewCard(CardOrderData cardOrderData, AccOrderData accOrderData) throws ServiceException {
        String generatedAccNum = FacilityDataGenerator.generateAccNum();
        accOrderData.setAccNumber(generatedAccNum);

        String generatedCVV = FacilityDataGenerator.generateCvvCode();
        String paymentSystem = cardOrderData.getPaymentSystem();
        String generatedCardNum = FacilityDataGenerator.generateCardNum(generatedAccNum, paymentSystem);

        cardOrderData.setCvvCode(generatedCVV);
        cardOrderData.setCardNumber(generatedCardNum);

        if (!OrderValidator.cardValidation(cardOrderData) && OrderValidator.accValidation(accOrderData)) {
            throw new WrongDataServiceException(WRONG_INPUT_DATA_MESSAGE);
        }

        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityActionDAO dao = daoProvider.getFacilityActionDAO();

        try {
            return dao.orderNewCard(cardOrderData, accOrderData);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changeCardState(int cardId, int cardState) throws ServiceException {
        DAOProvider daoProvider = DAOProvider.getInstance();
        FacilityActionDAO dao = daoProvider.getFacilityActionDAO();

        try {
            dao.changeCardState(cardId, cardState);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}