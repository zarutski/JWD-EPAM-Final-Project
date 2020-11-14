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

/**
 * The class {@code FacilityActionServiceImpl} provides
 * implementation of the {@code FacilityActionService} interface
 *
 * @author Maksim Zarutski
 */
public class FacilityActionServiceImpl implements FacilityActionService {

    private static final String WRONG_INPUT_DATA_MESSAGE = "Wrong data input";

    /**
     * Performs transfer operation from user's card to destination card,
     * or from one account to another
     * <p>
     * Transfer can be performed only after successful data validation
     *
     * @param transferData {@code TransferData} object contains data to perform transfer
     * @return boolean value that indicates if the transfer was completed successfully
     * @throws WrongDataServiceException if provided operation data is invalid
     * @throws ServiceException          if an error occurs during transfer operation
     */
    @Override
    public boolean transfer(TransferData transferData) throws ServiceException {
        if (!OperationValidator.transferDataValidation(transferData)) {
            throw new WrongDataServiceException(WRONG_INPUT_DATA_MESSAGE);
        }

        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityActionDAO dao = daoProvider.getFacilityActionDAO();

            return dao.transfer(transferData);
        } catch (ConfirmationDAOException | WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (InsufficientFundsDaoException e) {
            throw new InsufficientFundsServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Performs ordering a new card for user
     * <p>
     * Uses static methods of the {@link FacilityDataGenerator} class
     * to generate new account number, card number and cvv code
     * <p>
     * Operation can be performed only after successful data validation
     *
     * @param cardOrderData {@code CardOrderData} object contains data for ordering new card
     * @param accOrderData  {@code AccOrderData} object contains data for ordering new account
     * @return boolean value that indicates if the card order was completed successfully
     * @throws WrongDataServiceException if provided operation data is invalid
     * @throws ServiceException          if an error occurs during card order
     */
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

        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityActionDAO dao = daoProvider.getFacilityActionDAO();

            return dao.orderNewCard(cardOrderData, accOrderData);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Performs changing user's card state
     *
     * @param cardId    value is the identifier of the card
     * @param cardState value contains new card state
     * @throws WrongDataServiceException if provided operation data is invalid
     * @throws ServiceException          if an error occurs during operation
     */
    @Override
    public void changeCardState(int cardId, int cardState) throws ServiceException {
        try {
            DAOProvider daoProvider = DAOProvider.getInstance();
            FacilityActionDAO dao = daoProvider.getFacilityActionDAO();

            dao.changeCardState(cardId, cardState);
        } catch (WrongDataDAOException e) {
            throw new WrongDataServiceException(e);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}