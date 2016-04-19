package RoverFX.MainGUI;

import gnu.io.SerialPort;

public class WindowUtil
{
    public static int convertBaudSelection(String selectedBAUD)
    {
        return Integer.parseInt(selectedBAUD);
    }

    public static int convertDataSelection(String selectedDATA)
    {
        return SerialPort.DATABITS_8;
    }

    public static int convertParity(String selectedParity)
    {
        switch (selectedParity)
        {
            case "EVEN":
                return SerialPort.PARITY_EVEN;
            case "ODD":
                return SerialPort.PARITY_ODD;
            case "NONE":
                return SerialPort.PARITY_NONE;
            default:
                return -1;
        }
    }

    public static int convertStopBits(String selectedSTOP)
    {
        switch (selectedSTOP)
        {
            case "2":
                return SerialPort.STOPBITS_2;
            case "1":
                return SerialPort.STOPBITS_1;
            default:
                return -1;
        }
    }
}
