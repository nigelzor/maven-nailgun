package com.github.nigelzor.maven.nailgun;

import org.apache.maven.cli.MavenCli;
import org.codehaus.plexus.classworlds.ClassWorld;
import com.martiansoftware.nailgun.NGContext;

public class Client {

	public static void nailMain(NGContext context) throws Exception {
		System.exit(run(context, Server.classWorld));
	}

	private static int run(NGContext context, ClassWorld classWorld) throws Exception {
		MavenCli cli = new MavenCli(classWorld);
		return cli.doMain(context.getArgs(), context.getWorkingDirectory(), context.out, context.err);
	}

}
