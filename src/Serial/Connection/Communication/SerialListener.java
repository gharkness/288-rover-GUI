package Serial.Connection.Communication;

import Serial.Connection.Connection;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;

public class SerialListener implements SerialPortEventListener
{
    private Connection parent;

    public SerialListener(Connection parent)
    {
        this.parent = parent;
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent)
    {
        if (serialPortEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try
            {
                byte receivedByte = (byte)(parent.getInputStream().read());

                if (receivedByte != parent.endChar)
                {

                }

            }
            catch (IOException e)
            {

            }
        }
    }
}
