package Serial.Connection;


import java.io.IOException;
import java.io.InputStream;

public class SerialReader implements Runnable
{
    private byte[] returnSig = {13, 10};

    private InputStream inputStream;

    private Connection parentConnection;

    public SerialReader(InputStream inputStream, Connection connection)
    {
        this.inputStream = inputStream;
        this.parentConnection = connection;
    }

    @Override
    public void run()
    {
        byte[] buffer = new byte[1024];
        int length = -1;
        int tmpByte;
        try
        {
            while (!parentConnection.isConnectionClosed())
            {
                if(inputStream.available() > 0)
                {
                    length = this.inputStream.read(buffer);
                    if (length > -1)
                    {
                        for (int i = 0; i < length; i++)
                        {
                            tmpByte = buffer[i];
                            if(tmpByte < 0)
                            {
                                tmpByte += 255;
                            }
                            if (tmpByte != parentConnection.endChar)
                            {
                                parentConnection.getTempBytes()[parentConnection.getNumTempBytes()] = tmpByte;
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            parentConnection.setConnectionClosed(true);
            try
            {
                parentConnection.getOutputStream().close();
                parentConnection.getInputStream().close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            parentConnection.getSerialPort().close();
            parentConnection.setConnectionClosed(false);
        }
    }
}
