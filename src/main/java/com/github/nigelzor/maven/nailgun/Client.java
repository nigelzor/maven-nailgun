package com.github.nigelzor.maven.nailgun;

import org.apache.maven.cli.MavenCli;
import com.martiansoftware.nailgun.NGContext;

public class Client {

	public static void nailMain(NGContext context) throws Exception {
		System.exit(run(context));
	}

	private static int run(NGContext context) throws Exception {
		MavenCli cli = new MavenCli();
		return cli.doMain(context.getArgs(), context.getWorkingDirectory(), context.out, context.err);
	}

}
