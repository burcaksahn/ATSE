package atse.common.filters;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class XSSFilter implements Filter {
 
	 private static Pattern[] patterns = new Pattern[]{
	        // Script fragments
	        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
	        // src='...'
	        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // lonely script tags
	        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
	        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // eval(...)
	        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // expression(...)
	        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        // javascript:...
	        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
	        // vbscript:...
	        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
	        // onload(...)=...
	        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
	    };
	 
	 
    public void init(FilterConfig filterConfig) throws ServletException {
    }
 
    public void destroy() {
    }
 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    	
    	@SuppressWarnings("rawtypes")
		Enumeration en = request.getParameterNames();
    	
    	while (en.hasMoreElements()) {
            
            String paramName = (String) en.nextElement();
            
            String originalValue = request.getParameter(paramName);
            String value = stripXSS(originalValue);
            
            request.removeAttribute(paramName);
            request.setAttribute(paramName, value);
            
        }
    	
    	chain.doFilter(request, addResponseOptions(response));
    }
 
    
    private ServletResponse addResponseOptions(ServletResponse response){
    	
    	HttpServletResponse res = (HttpServletResponse)response;
    	
    	res.addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
    //	res.addHeader("Access-Control-Allow-Origin", SettingsAdmMan.getInstance().getCdnServerUrl());
    //	res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
    	
    	/*// Put it back after the report for Security
    	 * 
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        */
    	return res;
    }
    
    private String stripXSS(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);
 
            // Avoid null characters
            value = value.replaceAll("\0", "");
 
            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }
    
}