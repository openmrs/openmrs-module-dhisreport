package org.openmrs.module.dhisreport.web.filter;

/**
 * Created by maurya on 12/19/14.
 */
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;

/**
 * Filter intended for all /ws/rest calls that allows the user to authenticate via Basic
 * authentication. (It will not fail on invalid or missing credentials. We count on the API to throw
 * exceptions if an unauthenticated user tries to do something they are not allowed to do.) <br/>
 * <br/>
 * IP address authorization is also performed based on the global property:
 */
public class AuthorizationFilter implements Filter {
	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.debug("Initializing DHISreport WS Authorization filter");
	}

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		log.debug("Destroying DHISreport WS Authorization filter");
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// skip if the session has timed out, we're already authenticated, or it's not an HTTP request
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			if (httpRequest.getRequestedSessionId() != null
					&& !httpRequest.isRequestedSessionIdValid()) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,
						"Session timed out");
			}
			if (!Context.isAuthenticated()) {
				String basicAuth = httpRequest.getHeader("Authorization");
				if (basicAuth != null) {
					// this is "Basic ${base64encode(username + ":" + password)}"
					try {
						basicAuth = basicAuth.substring(6); // remove the leading "Basic "
						String decoded = new String(Base64
								.decodeBase64(basicAuth), Charset
								.forName("UTF-8"));
						String[] userAndPass = decoded.split(":");
						Context.authenticate(userAndPass[0], userAndPass[1]);
						if (log.isDebugEnabled())
							log.debug("authenticated " + userAndPass[0]);
					} catch (Exception ex) {
						// This filter never stops execution. If the user failed to
						// authenticate, that will be caught later.
					}
				}
			}
		}
		// continue with the filter chain in all circumstances
		chain.doFilter(request, response);
	}
}
