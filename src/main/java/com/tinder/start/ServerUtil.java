package com.tinder.start;

import com.tinder.controller.*;
import com.tinder.exception.ErrorConnectionToDataBase;
import lombok.SneakyThrows;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

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


        handler.addServlet(new ServletHolder(new ChatController()), "/api/v0/chat");
        handler.addServlet(new ServletHolder(new LikedController()), "/api/v0/liked");
        handler.addServlet(new ServletHolder(new LoginController()), "/api/v0/login");
        handler.addServlet(new ServletHolder(new MessagesController()), "/api/v0/messages");
        handler.addServlet(new ServletHolder(new UserController()), "/api/v0/users");
        handler.addServlet(new ServletHolder(new GoogleController()), "/api/v0/google");


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
