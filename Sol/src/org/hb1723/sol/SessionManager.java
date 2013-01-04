package org.hb1723.sol;

import java.util.HashMap;

import org.hb1723.sol.IOModules.IOModule;

public class SessionManager {
	
	private HashMap<String,Session> sessionList;
	
	public SessionManager() {
		sessionList = new HashMap<String,Session>();
	}

	public Session createSession( String id, IOModule iomodule ) {
		Session session;
		
		if( hasSession( id ) ) {
			session = getSession( id );	
		} else {
			session = new Session( id, iomodule, this );
		}
		
		sessionList.put( id, session );
				
		return session;
	}

	public boolean hasSession(String id) {
		return sessionList.containsKey( id );
	}


	public Session getSession(String id) {
		return sessionList.get( id );
	}

}
