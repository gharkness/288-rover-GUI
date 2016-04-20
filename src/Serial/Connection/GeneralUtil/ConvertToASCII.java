package Serial.Connection.GeneralUtil;

import java.nio.charset.StandardCharsets;

public class ConvertToASCII
{
    public static byte[] convertForTransmit(String command)
    {
        return command.getBytes(StandardCharsets.US_ASCII);
    }
}
