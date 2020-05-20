package com.tinder.start;

import com.tinder.controller.*;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.filter.CheckJWT;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public final class ServerUtil {

    private static volatile ServerUtil instance;
    private Server server;

    private ServerUtil() {
    }

    public static ServerUtil getInstance() {
        if (instance == null) {
            synchronized (ServerUtil.class) {
                if (instance == null) {
                    instance = new ServerUtil();
                    instance.startServer();
                }
            }
        }
        return instance;
    }

    @SneakyThrows
    private void startServer() {
        long t = System.currentTimeMillis();
        server = new Server(8080);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("./src/main/resources/webapp");

        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, handler, new DefaultHandler()});
        gzip.setHandler(handlers);


        handler.addServlet(ChatController.class, "/api/v0/chat");
        handler.addServlet(LikedController.class, "/api/v0/liked");
        handler.addServlet(LoginController.class, "/api/v0/login");
        handler.addServlet(MessagesController.class, "/api/v0/messages");
        handler.addServlet(UserController.class, "/api/v0/users");
        handler.addServlet(GoogleSignUpController.class, "/api/v0/google-sign-up");
        handler.addServlet(GoogleSignInController.class, "/api/v0/google-sign-in");
        handler.addFilter(CheckJWT.class, "*", EnumSet.of(DispatcherType.REQUEST));


        try {
            ConfigFile.getInstance();
            DataSource.getDataSource();
            server.start();
            server.join();

            System.out.println(System.currentTimeMillis() - t);
        } catch (ErrorConnectionToDataBase | Exception errorConnectionToDataBase) {
            errorConnectionToDataBase.printStackTrace();
            stop();
        }
    }

    public void stop() throws Exception {
        server.stop();
    }
}
