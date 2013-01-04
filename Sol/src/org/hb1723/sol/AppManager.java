package org.hb1723.sol;

import org.hb1723.sol.IOModules.IOModule;
import org.hb1723.sol.IOModules.XMPPModule;

public class AppManager {

	public static void main(String[] args) {
		SessionManager sessionManager = new SessionManager();
		IOModule xmpp = new XMPPModule( sessionManager );

		xmpp.start();

		try {
			xmpp.getThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println( "Application Exiting" );

	}

}
