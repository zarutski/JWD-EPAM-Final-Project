package by.epamtc.zarutski.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The class {@code ExpirationTag} implements custom tag for displaying
 * card expiration date in a certain format
 *
 * @author Maksim Zarutski
 */
public class ExpirationTag extends TagSupport {

    private static final long serialVersionUID = 5861322951375646709L;

    private static final Logger logger = LogManager.getLogger(ExpirationTag.class);

    private static final String LOG_ERROR = "Error writing expiration tag";
    private static final String EXPIRATION_PATTERN = "MM/yy";
    private static final String LINE_BREAK = "<br>";

    private LocalDate expirationDate;

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * Formats {@code LocalDate} value to the {@value EXPIRATION_PATTERN} format
     *
     * @return {@value SKIP_BODY} reports that tag's body evaluation will be skipped
     * @throws JspException if an error occurs while processing
     */
    @Override
    public int doStartTag() throws JspException {

        try {
            JspWriter out = pageContext.getOut();

            String expirationFormatted = expirationDate.format(DateTimeFormatter.ofPattern(EXPIRATION_PATTERN));
            out.write(expirationFormatted + LINE_BREAK);
        } catch (IOException e) {
            logger.info(LOG_ERROR);
        }

        return SKIP_BODY;
    }
}
