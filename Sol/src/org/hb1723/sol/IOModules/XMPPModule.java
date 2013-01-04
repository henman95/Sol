package org.hb1723.sol.IOModules;

import org.hb1723.sol.Session;
import org.hb1723.sol.SessionManager;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


public class XMPPModule implements IOModule, Runnable, PacketListener {
	
	private Connection connection;
	private Thread thread;
	private volatile boolean done;
	private SessionManager sessionManager;
	
	public XMPPModule( SessionManager sessionManager ) {
		this.thread = null;
		this.done   = false;
		
		this.sessionManager = sessionManager;  
	}
	
	@Override
	public void start() {
		if( thread == null ) {
			thread = new Thread( this );
			done   = false;
			
			thread.start();
		}
	}
	
	@Override
	public void stop() {
		if( thread != null ) {
			done = true;
		}
	}
	
	@Override
	public Thread getThread() {
		return thread;
	}
	
	public void send( String destination, String text ) {
		Message response = new Message( destination, Message.Type.chat );
		response.setBody( "*" + text );
		
		connection.sendPacket( response );
	}
	
	@Override
	public void run() {
		connection = new XMPPConnection( "webmail.1723.org" );

		try {
			PacketFilter filter = new FromContainsFilter( "webmail.1723.org" );
			
			connection.connect();
			connection.login( "sal", "server", "Server Connection" );
			connection.addPacketListener( this, filter );
			
			while( !done ) {
				Thread.sleep( 10000 );
				System.out.println( "XMPPModule - Alive" );
			}

			connection.disconnect();
		} catch( XMPPException e ) {
			System.out.println( "XMPP Exception: " + e );
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void processPacket(Packet packet) {
		Message message = (Message) packet;
		
		if( message.getType() == Message.Type.chat ) {
			String sessionId = message.getFrom();
			String text      = message.getBody();
			
			if( !sessionManager.hasSession( sessionId ) )
				sessionManager.createSession( sessionId, this ); 
	
			Session session = sessionManager.getSession( sessionId );
			
			session.pushInput( text );
		}
	}
	
	
}
