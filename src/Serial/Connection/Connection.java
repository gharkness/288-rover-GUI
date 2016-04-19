package Serial.Connection;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Connection
{
    private InputStream inputStream;

    private OutputStream outputStream;

    private Thread receiver;

    private SerialPort serialPort;

    private CommPortIdentifier portID;

    private boolean connectionActive;

    private boolean connectionClosed;

    private String portName;

    private static int baudRate = 57600;

    private static int parity = 0;

    private static int dataBits = 8;

    private static int stopBits = 2;

    private int numTempBytes;

    private int[] tempBytes;

    /**
     * Signifies the end of the command being transmitted.
     * Most likely <ENTER>, or '\r'
     */
    public final static byte endChar = 13;

    /**
     * Amount of bytes that make up the command
     */
    //private final static int commandSize = 3;

    public Connection(String portName)
    {
        connectionActive = false;
        connectionClosed = false;

        numTempBytes = 0;
        tempBytes = new int[1024];
    }

    public void setConnectionClosed(boolean boolVal)
    {
        connectionClosed = boolVal;
    }

    public OutputStream getOutputStream()
    {
        return this.outputStream;
    }

    public InputStream getInputStream()
    {
        return this.inputStream;
    }

    public SerialPort getSerialPort()
    {
        return this.serialPort;
    }

    public int getNumTempBytes()
    {
        return this.numTempBytes;
    }

    public int[] getTempBytes()
    {
        return this.tempBytes;
    }

    public boolean connect()
    {
        try
        {
            portID = CommPortIdentifier.getPortIdentifier(portName);
            if (!portID.isCurrentlyOwned())
            {
                serialPort = (SerialPort)portID.open(this.getClass().getName(), 2000);

                if (serialPort == null)
                {
                    System.out.println("null");
                }

                serialPort.setSerialPortParams(57600, 8, 2, 0);

                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();

                receiver = (new Thread(new SerialReader(inputStream, this)));
                connectionClosed = false;
                receiver.start();
                connectionActive = true;
                return true;
            }
        }
        catch (NoSuchPortException e)
        {
            e.printStackTrace();
        }
        catch (PortInUseException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedCommOperationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean disconnect()
    {
        boolean disconnected = true;

        connectionClosed = true;

        try
        {
            receiver.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            disconnected = true;
        }

        try
        {
            outputStream.close();
            inputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            disconnected = false;
        }

        serialPort.close();
        connectionActive = false;

        return disconnected;
    }

    public boolean isConnectionActive()
    {
        return connectionActive;
    }

    public boolean isConnectionClosed()
    {
        return connectionClosed;
    }

    public boolean send(String command)
    {
        byte[] comm = ConvertToASCII.convertForTransmit(command);
        boolean wasSuccessful = true;
        for (int i = 0; i < comm.length; i++)
        {
            if (comm[i] == endChar)
            {
                wasSuccessful = false;
                break;
            }
        }
        if (wasSuccessful && isConnectionActive())
        {
            try
            {
                for (int i = 0; i < command.length(); i++)
                {
                    outputStream.write(comm[i]);
                }
                outputStream.write(endChar);
            }
            catch (IOException e)
            {
                wasSuccessful = false;
                disconnect();
            }
        }
        return wasSuccessful;
    }

    /*
    private byte[] convertCommand(String command)
    {
        ArrayList converted = new ArrayList();

        for (int i = 0; i < command.length(); i++)
        {
            if (command.charAt(i) > 255)
            {
                converted.add(255);
                continue;
            }
            else if(command.charAt(i) < 0)
            {
                converted.add(0);
                continue;
            }
            else if (command.charAt(i) == endChar)
            {
                continue;
            }
            else
            {
                converted.add(command.charAt(i));
            }
        }

        byte[] comm = new byte[converted.size()];
        for (int i = 0; i < converted.size(); i++)
        {
            comm[i] = (byte)converted.get(i);
        }
        return comm;
    }
    */
}
