package by.epamtc.zarutski.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class CardTag extends TagSupport {

	private static final long serialVersionUID = 1407419439301788426L;

	private static final Logger logger = LogManager.getLogger(CardTag.class);

    private static final String MASK_NUMBER = "**** **** **** ";
    private static final String VISA_PREFIX = "4";
    private static final String MASTERCARD_PREFIX = "5";
    private static final String TYPE_VISA = "<br>[visa]<br>";
    private static final String TYPE_MASTERCARD = "<br>[mastercard]<br>";

    private static final String LOG_ERROR = "Error writing card tag";

    private String cardNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public int doStartTag() throws JspException {

        try {
            JspWriter out = pageContext.getOut();

            String maskedNumber = MASK_NUMBER + cardNumber.substring(12, 16);
            out.write(maskedNumber);

            if (cardNumber.startsWith(VISA_PREFIX)) {
                out.write(TYPE_VISA);
            } else if (cardNumber.startsWith(MASTERCARD_PREFIX)) {
                out.write(TYPE_MASTERCARD);
            }
        } catch (IOException e) {
            logger.info(LOG_ERROR);
        }

        return SKIP_BODY;
    }
}
