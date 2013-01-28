package org.hb1723.sol.IOModules;

import org.hb1723.sol.Session;

public interface IOModule {
	public void start();
	public void stop();
	public Thread getThread();
	
	public void send( String id, String text );
	public void send( Session session, String text);
	
	public void debug( Session session, int level, String text );
}
