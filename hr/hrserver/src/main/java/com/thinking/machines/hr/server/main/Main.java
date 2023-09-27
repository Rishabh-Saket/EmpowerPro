package com.thinking.machines.hr.server.main;
import com.thinking.machines.hr.server.*;
import com.thinking.machines.network.common.exceptions.*;
import com.thinking.machines.network.server.*;
public class Main 
{
    public static void main(String[] args) 
    {
        try 
        {
            RequestHandler requestHandler;
            requestHandler=new RequestHandler();
            NetworkServer networkServer;
            networkServer=new NetworkServer(requestHandler);
            networkServer.start();
        }catch(NetworkException networkException) {
            System.out.println(networkException);
        }     
    }
}