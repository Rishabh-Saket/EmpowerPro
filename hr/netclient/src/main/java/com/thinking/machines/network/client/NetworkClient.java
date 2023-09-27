package com.thinking.machines.network.client;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;
import java.io.*;
import java.net.*;
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
        
        try 
        {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();

            byte bytes[]=baos.toByteArray();
            int requestLength=bytes.length;
            byte header[]=new byte[1024];
            int x;
            int i;
            i=1023;
            x=requestLength;
            while(x>0)
            {
                header[i]=(byte)(x%10);
                x=x/10;
                i--;
            }

            Socket socket=new Socket("localhost", 5502);
            OutputStream os=socket.getOutputStream();
            os.write(header, 0, 1024);
            os.flush();

            InputStream is=socket.getInputStream();

            byte ack[]=new byte[1];
            int bytesReadCount;
            while(true)
            {
                bytesReadCount=is.read(ack);
                if(bytesReadCount==-1) continue;
                break;
            }

            int bytesToSend=requestLength;
            int chunkSize=1024;
            int j=0;
            while(j<bytesToSend)
            {
                if((bytesToSend-j)<1024) chunkSize=bytesToSend-j;
                os.write(bytes, j, chunkSize);
                os.flush();
                j=j+chunkSize;
            }

            int bytesToRecieve=1024;
            byte temp[]=new byte[1024];
            int k;
            i=0;
            j=0;
            while(j<bytesToRecieve)
            {
                bytesReadCount=is.read(temp);
                if(bytesReadCount==-1) continue;
                for(k=0;k<bytesReadCount;k++)
                {
                    header[i]=temp[k];
                    i++;
                }
                j=j+bytesReadCount;
            }

            int responseLength=0;
            i=1;
            j=1023;
            while(j>=0)
            {
                responseLength=responseLength+(header[j]*i);
                i=i*10;
                j--;
            }
            System.out.println("Header recieved: "+responseLength);
            ack[0]=1;
            os.write(ack, 0, 1);
            os.flush();
            System.out.println("Acknowledgement sent");

            byte response[]=new byte[responseLength];
            bytesToRecieve=responseLength;
            i=0;
            j=0;
            System.out.println("Now recieving response");
            
            while(j<bytesToRecieve)
            {
                bytesReadCount=is.read(temp);
                if(bytesReadCount==-1) continue;
                for(k=0;k<bytesReadCount;k++)
                {
                    response[i]=temp[k];
                    i++;
                }
                j=j+bytesReadCount;
            }

            System.out.println("Response recieved: ");
            ack[0]=1;
            os.write(ack, 0, 1);
            os.flush();
            socket.close();

            ByteArrayInputStream bais=new ByteArrayInputStream(response);
            ObjectInputStream ois=new ObjectInputStream(bais);
            Response responseObject=(Response)ois.readObject();
            return responseObject;
        }catch (Exception e) 
        {
        throw new NetworkException(e.getMessage());
        }
    }
}