package com.thinking.machines.network.client;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
public class NetworkClient
{
    public Response send(Request request) throws NetworkException
    {
        /*
         * Wrap all the network/socket programming code over here
         * 1. Serialize Request Object
         * 2. Connect to server
         * 3. Send header and then the serialized form of chunks 
         * 4. Recieve back header and then the serialized form of Response then, deserialized it
         * 5. return the reference of Response object
         */
        return null; 
    }
}