package com.thinking.machines.network.server;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import com.thinking.machines.network.server.*;
import java.io.*;
import java.net.*;
class RequestProcessor extends Thread
{
    private RequestHandlerInterface requestHandler;
    private Socket socket;
    RequestProcessor(Socket socket, RequestHandlerInterface requestHandler)
    {
        this.requestHandler=requestHandler;
        this.socket=socket;
        start();
    }

    public void run()
    {
        try
        {
            InputStream is=socket.getInputStream();            
            OutputStream os=socket.getOutputStream();
            int bytesToRecieve=1024;
            byte header[]=new byte[1024];
            byte tmp[]=new byte[1024];
            int bytesReadCount;
            int i,j,k;
            j=0;
            i=0;            
            while(j<bytesToRecieve)
            {
                bytesReadCount=is.read(tmp);
                if(bytesReadCount==-1) continue;
                for(k=0;k<bytesReadCount;k++)
                {
                    header[i]=tmp[k];
                    i++;
                }
                j+=bytesReadCount;
            }

            int requestLength=0;
            i=1;
            j=1023;
            while(j>=0)
            {
                requestLength+=header[j]*i;
                i*=10;
                j--;
            }
            byte ack[]=new byte[1];
            ack[0]=1;
            os.write(ack, 0, 1);
            os.flush();

            byte request[]=new byte[requestLength];
            bytesToRecieve=requestLength;
            j=0;
            i=0;
            while(j<bytesToRecieve)
            {
                bytesReadCount=is.read(tmp);
                if(bytesReadCount==-1) continue;
                for(k=0;k<bytesReadCount;k++)
                {
                    request[i]=tmp[k];
                    i++;
                }
                j+=bytesReadCount;
            }

            ByteArrayInputStream bais=new ByteArrayInputStream(request);
            ObjectInputStream ois=new ObjectInputStream(bais);
            Request req=(Request)ois.readObject();
            //"Object Deserialized"
            
            Response response=requestHandler.process(req);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();
            byte objectBytes[]=baos.toByteArray();
            int responseLength=objectBytes.length;
            int x;
            x=responseLength;
            i=1023;
            header=new byte[1024];
            while(x>0)
            {
                header[i]=(byte)(x%10);
                x/=10;
                i--;
            }
            os.write(header, 0, 1024); //from which index , how many 
            os.flush();
            System.out.println("Response Header sent");
            while(true)
            {
                bytesReadCount=is.read(ack);
                if(bytesReadCount==-1) continue;
                break;
            }
            System.out.println("Acknowledgement Recieved");
            int bytesToSend=responseLength;
            int chunkSize;
            chunkSize=1024;
            j=0;
            while(j<bytesToSend)
            {
                if((bytesToSend-j)<1024) chunkSize=bytesToSend-j;
                os.write(objectBytes, j, chunkSize);
                os.flush();
                j+=chunkSize;
            }
            System.out.println("Reposne sent");
            while(true)
            {
                bytesReadCount=is.read(ack);
                if(bytesReadCount==-1) continue;
                break;
            }
            System.out.println("Acknowledgement Recieved");
            socket.close();
        }catch(Exception e)
        {
            System.out.println(e);
        }

    }
}