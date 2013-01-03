package org.hb1723.sol;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class XMPPIOModule implements IOModule, Runnable, MessageListener, ChatManagerListener {
	private Thread thread = null;
	private volatile boolean done = false;

	@Override
	public void start() {
		if( thread == null ) {
			thread = new Thread( this );

			done = false;
			thread.start();
		}
	}

	@Override
	public void stop() {
		if( thread != null ) {
			done = true;
		}
	}

	public Thread getThread() {
		return thread;
	}

	@Override
	public void run() {
		Connection connection = new XMPPConnection( "webmail.1723.org" );

		try {
			connection.connect();
			connection.login( "sal", "server", "Server Connection" );

			ChatManager chatManager = connection.getChatManager();
			chatManager.addChatListener( this );

			//Chat chat = chatManager.createChat( "hbennett@webmail.1723.org", this );
			//chat.sendMessage( "I am alive" );

			while( !done ) {
				Thread.sleep( 10000 );
				System.out.println( "XmppIoModule - Done: " + done );
			}

			connection.disconnect();

		} catch( XMPPException e ) {
			System.out.println( "XMPP Exception: " + e );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	@Override
	public void processMessage(Chat chat, Message message) {
		try {
			String dest = chat.getParticipant();
			String text = message.getBody();

			if( "die".equals( text ) ) {
				chat.sendMessage( "Attempting suicide" );
				done = true;
			}

			chat.sendMessage( "Same to you (" + dest + "): " + text );

		} catch( XMPPException e ) {
			System.out.println( "XMPP Exception: " + e );
		}
	}

	@Override
	public void chatCreated(Chat chat, boolean createdLocally) {
		System.out.println( "Creating chat to " + chat.getParticipant() );

		 if( !createdLocally) 
			 	chat.addMessageListener( this );
	}

}