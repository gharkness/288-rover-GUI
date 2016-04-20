package Serial.Connection.Communication;


/*
public class SerialReader implements Runnable
{
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
        int length = 0;
        int tmpByte;
        try
        {
            while (!parentConnection.isConnectionClosed())
            {
                if(inputStream.available() > 0)
                {
                    length = parentConnection.getInputStream().read(buffer);
                    if (length > -1)
                    {
                        for (int i = 0; i < length; i++)
                        {
                            tmpByte = buffer[i];
                            if(tmpByte < 0)
                            {
                                tmpByte += 256;
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
*/
