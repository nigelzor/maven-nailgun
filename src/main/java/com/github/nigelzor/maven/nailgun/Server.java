package com.github.nigelzor.maven.nailgun;

import java.net.InetAddress;

import org.codehaus.plexus.classworlds.ClassWorld;

import com.martiansoftware.nailgun.NGConstants;
import com.martiansoftware.nailgun.NGServer;

public class Server {

	public static void main(String[] args, ClassWorld classWorld) throws Exception {
		// bah, this should just be a call to NGServer.main(), but plexus doesn't want us to just return
		if (args.length > 1) {
			System.err.println("only expecting one arg: [host:]port");
			return;
		}

		InetAddress serverAddress = null;
		int port = NGConstants.DEFAULT_PORT;

		// parse the sole command line parameter, which
		// may be an inetaddress to bind to, a port number,
		// or an inetaddress followed by a port, separated
		// by a colon
		if (args.length != 0) {
			String[] argParts = args[0].split(":");
			String addrPart = null;
			String portPart = null;
			if (argParts.length == 2) {
				addrPart = argParts[0];
				portPart = argParts[1];
			} else if (argParts[0].indexOf('.') >= 0) {
				addrPart = argParts[0];
			} else {
				portPart = argParts[0];
			}
			if (addrPart != null) {
				serverAddress = InetAddress.getByName(addrPart);
			}
			if (portPart != null) {
				port = Integer.parseInt(portPart);
			}
		}

		NGServer server = new NGServer(serverAddress, port, NGServer.DEFAULT_SESSIONPOOLSIZE);
		Thread t = new Thread(server);
		t.setName("NGServer(" + serverAddress + ", " + port + ")");
		t.start();

		int runningPort = server.getPort();
		while (runningPort == 0) {
			try {
				Thread.sleep(50);
			} catch (Throwable toIgnore) {
			}
			runningPort = server.getPort();
		}

		System.out.println("NGServer "
				+ NGConstants.VERSION
				+ " started on "
				+ ((serverAddress == null) ? "all interfaces" : serverAddress.getHostAddress())
				+ ", port " + runningPort + ".");

		// wait until someone tells us to die
		t.join();
	}

}
