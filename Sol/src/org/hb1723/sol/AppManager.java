package org.hb1723.sol;

public class AppManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IOModule xmpp = new XMPPIOModule();

		xmpp.start();

		try {
			xmpp.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println( "Application Exiting" );

	}

}
