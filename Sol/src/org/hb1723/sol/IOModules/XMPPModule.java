package org.hb1723.sol.IOModules;

import java.util.HashMap;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


public class XMPPModule implements IOModule, Runnable, PacketListener {
	
	private Connection connection;
	private Thread thread;
	private volatile boolean done;
	
	private HashMap<String,IOSession> sessions;
	
	public XMPPModule() {
		thread = null;
		done   = false;
		sessions = new HashMap<String,IOSession>();
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
		
		String sessionKey = packet.getFrom();
		
		if( ! sessions.containsKey( sessionKey ) ) {
			XMPPSession newSession = new XMPPSession( this );
			newSession.setID( sessionKey );
			newSession.setDestination( packet.getFrom() );
			
			sessions.put( sessionKey, newSession );
		}
	
		IOSession session = sessions.get( sessionKey );
		session.addInputQueue( message.getBody() );
		
		if( !session.isRunning() ) {
			Thread sessionThread = new Thread( session );
			sessionThread.start();
		}
		
		System.out.println( session );
		
		if( message.getType() == Message.Type.chat ) {
			System.out.println( "-----------------------------:" + packet.getPropertyNames().size() );
			System.out.println( "! From : " + message.getFrom() );
			System.out.println( "! Body : " + message.getBody() );
			
			//Message response = new Message( message.getFrom(), Message.Type.chat );
			//response.setBody( "! Echo: " + message.getBody() );
			
			//connection.sendPacket( response );
		}
	}
	
	
}
