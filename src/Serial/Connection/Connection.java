package Serial.Connection;

import Controller.Controller;
import Serial.Connection.GeneralUtil.ConvertToASCII;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class Connection implements SerialPortEventListener
{
    private Controller parent;

    private InputStream inputStream;

    private OutputStream outputStream;

    private SerialPort serialPort;

    private CommPortIdentifier portID;

    private boolean connectionActive;

    private boolean connectionClosed;

    private String portName;

    /**
     * Signifies the end of the command being transmitted.
     * Most likely <ENTER>, or '\r'
     */
    public final static byte endChar = 13;


    /**
     * Amount of bytes that make up the command
     */
    private final static int commandSize = 3;

    public Connection(String portName, Controller parent)
    {
        connectionActive = false;
        connectionClosed = false;

        this.portName = portName;

        this.parent = parent;
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

                //  ARDUINO
                //serialPort.setSerialPortParams(9600, 8, 1, 0);

                //  Rover
                serialPort.setSerialPortParams(57600, 8, 2, 0);

                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
                setSerialListener();

                connectionClosed = false;
                connectionActive = true;

                return true;
            }
        }
        catch (NoSuchPortException | PortInUseException | IOException e)
        {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean disconnect()
    {
        try
        {
            serialPort.removeEventListener();
            inputStream.close();
            outputStream.close();
            connectionActive = false;
            parent.writeToLogBox("Connection disconnected.\n");
            return true;
        } catch (Exception e)
        {
            parent.writeToLogBox("Unable to disconnect.\n");
        }
        return false;
    }

    private void setSerialListener()
    {
        try
        {
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        }
        catch (TooManyListenersException ignored){}
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
        for (int i = 0; i < commandSize; i++)
        {
            if (command.charAt(i) == endChar)
            {
                wasSuccessful = false;
                break;
            }
        }
        if (wasSuccessful && isConnectionActive())
        {
            try
            {
                for (int i = 0; i < commandSize; i++)
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

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent)
    {
        String data = "";
        while(serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                byte streamChar = (byte)inputStream.read();
                if (streamChar != endChar)
                {
                    data += new String(new byte[] {streamChar});
                }
                else
                {
                    break;
                }
            }
            catch (Exception ignored) {}
        }
        parent.writeToLogBox(data + '\n');
        parent.writeToLogBox("\n");
    }
}
