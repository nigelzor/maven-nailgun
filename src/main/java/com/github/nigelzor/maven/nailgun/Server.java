package com.github.nigelzor.maven.nailgun;

import java.net.InetAddress;

import org.codehaus.plexus.classworlds.ClassWorld;

import com.martiansoftware.nailgun.NGConstants;
import com.martiansoftware.nailgun.NGServer;

public class Server {

	static ClassWorld classWorld;

	public static void main(String[] args, ClassWorld classWorld) throws Exception {
		Server.classWorld = classWorld;

		// bah, this should just be a call to NGServer.main(), but plexus doesn't want us to just return
		if (args.length > 1) {
			System.err.println("only expecting one arg: port");
			return;
		}

		InetAddress serverAddress = InetAddress.getLocalHost();
		int port = NGConstants.DEFAULT_PORT;
		if (args.length != 0) {
			port = Integer.parseInt(args[0]);
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
