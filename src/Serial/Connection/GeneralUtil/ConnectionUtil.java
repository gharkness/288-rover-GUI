package Serial.Connection.GeneralUtil;

import gnu.io.CommPortIdentifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Enumeration;

public class ConnectionUtil
{
    /**
     * Scans for serial ports and returns a list to be used in the GUI window
     * @return
     *  Array list of serial ports
     */
    public static ObservableList<String> scanForPorts()
    {
        ObservableList<String> serialPorts = FXCollections.observableArrayList();

        Enumeration ports = CommPortIdentifier.getPortIdentifiers();

        while (ports.hasMoreElements())
        {
            CommPortIdentifier currentPort = (CommPortIdentifier)ports.nextElement();

            if (currentPort.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                serialPorts.add(currentPort.getName());
            }
        }
        return serialPorts;
    }
}
