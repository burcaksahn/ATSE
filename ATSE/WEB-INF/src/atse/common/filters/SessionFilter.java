package atse.common.filters;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionFilter implements HttpSessionListener {

	private static int activeSessions = 0;
	private static ArrayList<HttpSession> sessionList = new ArrayList<HttpSession>();
	
	public void sessionCreated(HttpSessionEvent se) {
		activeSessions++;
		
		HttpSession httpSession = se.getSession();
		sessionList.add(httpSession);
	}
	
	public void sessionDestroyed(HttpSessionEvent se) {
	
		if(activeSessions > 0)
			activeSessions--;
		
		sessionList.remove(se.getSession());
	}
	
	public static int getActiveSessionsNumber() {
		return activeSessions;
	}

	public static ArrayList<HttpSession> getActiveSessions(){
		return sessionList;
	}
	
	public static void createNewSessionList(){
		sessionList = new ArrayList<HttpSession>();
	}
	
	public static void cleanUpSessionList(){
		for (int i = 0; i < sessionList.size(); i++){
			if (sessionList.get(i) == null){
				sessionList.remove(i);
			}
		}
	}
	
}