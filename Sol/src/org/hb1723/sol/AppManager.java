package org.hb1723.sol;

import org.hb1723.sol.IOModules.IOModule;
import org.hb1723.sol.IOModules.XMPPModule;

public class AppManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IOModule xmpp = new XMPPModule();

		xmpp.start();

		try {
			xmpp.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println( "Application Exiting" );

	}

}
