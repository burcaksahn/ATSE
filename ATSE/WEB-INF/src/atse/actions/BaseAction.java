package atse.actions;

import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cayenne.BaseContext;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.configuration.CayenneRuntime;
import org.apache.cayenne.configuration.web.WebUtil;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

import atse.common.Settings;

@SuppressWarnings("serial")
public class BaseAction extends ActionSupport implements SessionAware, ServletRequestAware, ServletResponseAware {

	/////////////////////////////////////////////////////////////
    // Instance attributes
    //
	
	public static CayenneRuntime cRuntime = null;
    
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    protected ObjectContext context = null;

    private String actionMessage;
    private int iId;
    private int userid;
    private int navPageId;
    
    private String recaptchaPublicKey;
    
    public static final int NAV_PAGE_HOME = 1;
	public static final int NAV_PAGE_CLUSTER_SEARCH = 2;
	public static final int NAV_PAGE_CLUSTER_MANAGEMENT = 3;
    
	/**
     * Default execute method
     */
	public String execute() {
    		return SUCCESS;
    }
	
    /////////////////////////////////////////////////////////////
    // Object Context Methods
    //
	
	public CayenneRuntime getCayenneRuntime(){
		if (cRuntime == null){
			cRuntime = WebUtil.getCayenneRuntime(this.getSession().getServletContext());
		}
		
		return cRuntime;
	}
	
	public static ObjectContext getThreadObjectContext(){
		if (cRuntime != null){
			return cRuntime.getContext();
		}else
			return BaseContext.getThreadObjectContext();
	}

	public ObjectContext getObjectContext(){
		return this.getCayenneRuntime().getContext();
	}
	
    public String rollBackContext(Exception e, ObjectContext context){
    		String errorMsg = (e.getCause() == null) ? e.getMessage() : e.getCause().getMessage();
		context.rollbackChanges();
		return this.returnJsonErrorResponse(errorMsg);
    }
	
	/////////////////////////////////////////////////////////////
	// Application Methods
	//
    
	
	/////////////////////////////////////////////////////////////
	// Helper Methods
	//
	
	
	public Date getCurrentTime(){
		return new Date();
	}
	
	public Date getLastUpdateDate(){
		return Settings.getInstance().getLastUpdateDate();
	}
	
	public String returnJsonResponse(String returnStr){
		HttpServletResponse response = this.getServletResponse();

		if (response != null){

			response.reset();
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			try {
				byte[] bytes = returnStr.getBytes("UTF-8");
				ServletOutputStream sos = response.getOutputStream();
				sos.write(bytes);
			}catch(Exception ex){
				return null;
			}
			
		}

		return null;
	}
	
	public String returnJsonErrorResponse(String returnStr){
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("error", returnStr);
		} catch (JSONException e) {
			return null;
		}
		return this.returnJsonResponse(jsonObj.toString());
	}
	
	public String returnJsonSuccessResponse(String returnStr){
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("success", returnStr);
		} catch (JSONException e) {
			return null;
		}
		return this.returnJsonResponse(jsonObj.toString());
	}	
	
    /////////////////////////////////////////////////////////////
    // GET / SET Methods
    //
	
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getServletRequest(){
        return this.request;
    }
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpServletResponse getServletResponse(){
		return this.response;
	}
	
	public HttpSession getSession() {
		return this.getServletRequest().getSession();
    }
	
	@Override
	public void setSession(Map<String, Object> arg0) {
		// TODO Auto-generated method stub
		
	}

	public int getNavPageId() {
		return navPageId;
	}

	public void setNavPageId(int navPageId) {
		this.navPageId = navPageId;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

	public static CayenneRuntime getcRuntime() {
		return cRuntime;
	}

	public static void setcRuntime(CayenneRuntime cRuntime) {
		BaseAction.cRuntime = cRuntime;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ObjectContext getContext() {
		return context;
	}

	public void setContext(ObjectContext context) {
		this.context = context;
	}

	public int getId() {
		return iId;
	}

	public void setId(int iId) {
		this.iId = iId;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public static int getNavPageHome() {
		return NAV_PAGE_HOME;
	}
	
	public String getRecaptchaPublicKey() {
		return recaptchaPublicKey;
	}

	public void setRecaptchaPublicKey(String recaptchaPublicKey) {
		this.recaptchaPublicKey = recaptchaPublicKey;
	}

}
