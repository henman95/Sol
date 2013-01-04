package org.hb1723.sol.IOModules;

public interface IOSession extends Runnable {
	public void   setID( String id );
	public String getID();
	
	public void   setDestination( String dest );
	public String getDestination();

	public void send( String text );
	public String receive();
	
	public boolean isRunning();
	
	public void addInputQueue( String text);
}
