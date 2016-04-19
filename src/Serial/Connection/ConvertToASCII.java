package Serial.Connection;

import java.nio.charset.StandardCharsets;

public class ConvertToASCII
{
    public static byte[] convertForTransmit(String command)
    {
        byte[] converted = command.getBytes(StandardCharsets.US_ASCII);
        return converted;
    }
}
