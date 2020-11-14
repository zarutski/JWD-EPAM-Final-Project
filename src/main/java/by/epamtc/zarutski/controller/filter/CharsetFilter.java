package by.epamtc.zarutski.controller.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * The class {@code CharsetFilter} sets character encoding of the request and response to {@value ENCODING_UTF_8}
 *
 * @author Maksim Zarutski
 */
public class CharsetFilter implements Filter {

    private static final String ENCODING_UTF_8 = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(ENCODING_UTF_8);
        response.setCharacterEncoding(ENCODING_UTF_8);

        chain.doFilter(request, response);
    }
}