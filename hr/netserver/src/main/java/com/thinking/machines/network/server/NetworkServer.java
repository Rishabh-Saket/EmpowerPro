package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import com.thinking.machines.network.server.*;
import java.net.*;
public class NetworkServer
{
    private RequestHandlerInterface requestHandler;
    public NetworkServer(RequestHandlerInterface requestHandler) throws NetworkException
    {
        if(requestHandler==null)
        {
            throw new NetworkException("Request handler is missing");
        }
        this.requestHandler=requestHandler;
    }

    public void start() throws NetworkException // will run on main thread
    {
        ServerSocket serverSocket=null;
        int port=Configuration.getPort();
        try
        {
            serverSocket=new ServerSocket(port);
        }catch(Exception exception)
        {
            throw new NetworkException("Unable to start server on port: "+port);
        }
        try 
       {
            Socket socket;
            RequestProcessor requestProcessor;
            while(true)
            {
                System.out.println("Server started and is listening on port: "+port);
                socket=serverSocket.accept();
                requestProcessor=new RequestProcessor(socket,requestHandler);
            }
       }catch (Exception e) 
       {
            System.out.println(e);
       }
    }
}