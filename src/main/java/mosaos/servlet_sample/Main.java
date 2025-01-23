package mosaos.servlet_sample;

import java.io.File;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;

import mosaos.servlet_sample.conf.AppConfig;

public class Main {

    public static void main(String[] args) throws LifecycleException {
        // TODO 自動生成されたメソッド・スタブ
        String contextPath = "/";
        String appBase = ".";
        Tomcat tomcat = new Tomcat();
        //define port, host, contextpath
        tomcat.setPort(Integer.valueOf(AppConfig.getTomcatPort()));
        tomcat.setHostname("localhost");
        tomcat.getHost().setAppBase(appBase);
        tomcat.addWebapp(contextPath, appBase);

        //annotation scanning
        Context ctx = tomcat.addContext("", new File(".").getAbsolutePath());
        ctx.addLifecycleListener(new ContextConfig());
        // Add the JAR/folder containing this class to PreResources
        final WebResourceRoot root = new StandardRoot(ctx);
        final URL url = findClassLocation(Main.class);
        root.createWebResourceSet(WebResourceRoot.ResourceSetType.PRE, "/WEB-INF/classes", url, "/");
        ctx.setResources(root);

        //create connection
        tomcat.getConnector();
        tomcat.start();
        tomcat.getServer().await();
    }

    private static URL findClassLocation(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }
}
