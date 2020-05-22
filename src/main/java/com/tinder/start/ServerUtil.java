package com.tinder.start;


import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;


public final class ServerUtil {

    private static volatile ServerUtil instance;
    private Server server;

    private ServerUtil() {
    }

    public static ServerUtil getInstance() throws Exception {
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


    private void startServer() throws Exception {
        long t = System.currentTimeMillis();
        server = new Server(8080);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("./src/main/resources/webapp");

        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);

        try {
            ConfigFile.getInstance();
            DataSource.getDataSource();
            server.start();
            server.join();

            System.out.println(System.currentTimeMillis() - t);
        } catch (ErrorConnectionToDataBase | Exception | ConfigFileException errorConnectionToDataBase) {
            errorConnectionToDataBase.printStackTrace();
            stop();
        }
    }

    public void stop() throws Exception {
        server.stop();
    }
}
