package org.hb1723.sol.IOModules;

public interface IOModule {
	public void start();
	public void stop();
	public Thread getThread();
	
	public void send( String id, String text );
}
