package com.github.nigelzor.maven.nailgun;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.codehaus.plexus.classworlds.ClassWorld;
import org.codehaus.plexus.classworlds.launcher.Launcher;
import org.codehaus.plexus.classworlds.realm.ClassRealm;

import com.martiansoftware.nailgun.NGContext;

public class Client {
	private static class Reusable {
		private static final Launcher launcher;
		static {
			try {
				launcher = build();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static void nailMain(NGContext context) throws Exception {
		System.exit(run(context, Reusable.launcher));
	}

	// org.codehaus.plexus.classworlds.launcher.Launcher.mainWithExitCode(String[]), less launcher.launch()
	private static Launcher build() throws Exception {
		String classworldsConf = System.getProperty("classworlds.conf");
		Launcher launcher = new Launcher();
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		launcher.setSystemClassLoader(cl);
		InputStream is;
		if (classworldsConf != null) {
			is = new FileInputStream(classworldsConf);
		} else if ("true".equals(System.getProperty("classworlds.bootstrapped"))) {
			is = cl.getResourceAsStream("WORLDS-INF/conf/classworlds.conf");
		} else {
			is = cl.getResourceAsStream("classworlds.conf");
		}
		if (is == null) {
			throw new Exception("classworlds configuration not specified nor found in the classpath");
		}
		launcher.configure(is);
		return launcher;
	}

	// org.codehaus.plexus.classworlds.launcher.Launcher.launchEnhanced(String[])
	// but totally reworked for our purposes
	private static int run(NGContext context, Launcher launcher) throws Exception {
		// but for classloading issues it would be so easy
		// MavenCli cli = new MavenCli(launcher.getWorld());
		// return cli.doMain(context.getArgs(), context.getWorkingDirectory(), context.out, context.err);

		ClassRealm mainRealm = launcher.getMainRealm();
		Class<?> cwClass = mainRealm.loadClass(ClassWorld.class.getName());

		Class<?> mainClass = mainRealm.loadClass("org.apache.maven.cli.MavenCli");
		Constructor<?> mainConstructor = mainClass.getConstructor(cwClass);
		Object mainInstance = mainConstructor.newInstance(launcher.getWorld());

		Method mainMethod = mainClass.getMethod("doMain", String[].class, String.class, PrintStream.class, PrintStream.class);

		Thread.currentThread().setContextClassLoader(mainRealm);

		return (Integer) mainMethod.invoke(mainInstance, context.getArgs(), context.getWorkingDirectory(), context.out, context.err);
	}

}
