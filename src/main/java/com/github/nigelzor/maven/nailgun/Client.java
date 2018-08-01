package com.github.nigelzor.maven.nailgun;

import org.apache.maven.cli.MavenCli;
import com.martiansoftware.nailgun.NGContext;

public class Client {
    
    private static ClassLoader org;

    static {
        org = Client.class.getClassLoader();
    }

	public static void nailMain(NGContext context) throws Exception {
		System.exit(run(context));
	}

	private static int run(NGContext context) throws Exception {
	    Thread t = Thread.currentThread();

        try {

            t.setContextClassLoader(new ClassLoader(org) {
                @Override
                public Class<?> loadClass(String name) throws ClassNotFoundException {
                    return super.loadClass(name);
                }
            });
            MavenCli cli = new MavenCli();
            int doMain = cli.doMain(context.getArgs(), context.getWorkingDirectory(), null, null);
            return doMain;

        } finally {
            t.setContextClassLoader(org);
        }
	}

}
