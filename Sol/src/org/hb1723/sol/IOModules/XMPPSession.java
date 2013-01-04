package org.hb1723.sol.IOModules;

import java.util.concurrent.LinkedBlockingQueue;

public class XMPPSession implements IOSession, Runnable {
	private String id;
	private String destination;
	private XMPPModule module;
	private boolean running;
	
	private LinkedBlockingQueue<String> inputQueue;
	
	public XMPPSession( XMPPModule module ) {
		this.id         = "";
		this.inputQueue = new LinkedBlockingQueue<String>();
		this.module     = module;
		this.running    = false;
	}

	@Override
	public void setID(String id) {
		this.id = id;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public void setDestination(String text) {
		this.destination = text;
	}

	@Override
	public String getDestination() {
		return this.destination;
	}	
	
	
	@Override
	public void send(String text) {
		// TODO Auto-generated method stub
	}

	@Override
	public String receive() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addInputQueue( String text ) {
		inputQueue.add( text );
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void run() {
		running = true;
		System.out.println( "TStart - " + destination );
		
		while( inputQueue.size() > 0 ) {
			String command = inputQueue.poll();
			module.send( destination, "Starting: " + command );
			try {
				Thread.sleep( 2000 );
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			module.send( destination, "Done    : " + command );
		}
		System.out.println( "TStop  - " + destination );
		running = false;
	}
	
	public String toString() {
		String output = "";
		
		
		for( String text: inputQueue ) 
			output += ">" + id + " : " + text + "\n";
			
		return output;
	}
	

}
