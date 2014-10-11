package moe.zenbu.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import moe.zenbu.app.services.rmi.ZenbuServerImpl;

import moe.zenbu.app.services.rmi.ZenbuServerInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server
{
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    private static final int RMI_PORT = 19362;
    private static final String APP_RMI = "application";

    public static void main(String[] args)
    {
        log.info("Server started");

        try
        {
            ZenbuServerInterface stub = new ZenbuServerImpl();
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.bind(APP_RMI, stub);
        }
        catch(Exception e)
        {
            log.error("Could not start server", e);
        }
    }
}
