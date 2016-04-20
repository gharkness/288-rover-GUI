package Serial.Commands.General;

import Serial.Commands.ICommand;

public class Scan implements ICommand
{
    private final String command = "sca";

    public String getCommand()
    {
        return command;
    }
}
