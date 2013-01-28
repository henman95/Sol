package org.hb1723.sol;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.hb1723.sol.IOModules.IOModule;

public class Session implements Runnable {
	public Session( String id, IOModule ioModule, SessionManager sessionManager ) {
		this.id             = id;
		this.inputQueue     = new LinkedBlockingQueue<String>();
		this.properties     = new HashMap<String,String>();
		this.ioModule       = ioModule;
		this.sessionManager = sessionManager;
		this.thread         = new Thread( this );
	}
	
	public void setId( String id ) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	// IOManager Interface
	public void pushInput( String text ) {
		inputQueue.add( text );

		if( !thread.isAlive() ) {
			thread = new Thread( this );
			thread.start();
		}
	}
		
	public void send( String text ) {
		ioModule.send( this, text );
	}
	
	public void debug( int level, String text ) {
		ioModule.debug( this, level, text );
	}
	
	public void setProperty( String key, String value ) { 
		properties.put(key, value);
	}
	
	public String getProperty( String key ) {
		return getProperty( key, null );
	}
	
	public String getProperty( String key, String defaultValue ) {
		String value = defaultValue;
		
		if( properties.containsKey( key ) ) {
			value = properties.get( key );
		}
		
		return value;		
	}
	
	public void remProperty( String key ) {
		if( properties.containsKey( key ) )
			properties.remove( key );
	}
	
	public void run() {
		while( inputQueue.size() > 0 )	
			process( inputQueue.poll() );
	}
	
	private void process( String command ) {
		send( "Processing for (" + id + ") : " + command );
		debug( 7, command );
		sleep( 1000 );
	}
	
	private void sleep( int ms ) {
		try {
			Thread.sleep( ms );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private String   id;
	private IOModule ioModule;
	@SuppressWarnings("unused")
	private SessionManager sessionManager;
	private LinkedBlockingQueue<String> inputQueue;
	
	private HashMap<String,String> properties;

	private Thread   thread;
	private volatile boolean running;
	
}
