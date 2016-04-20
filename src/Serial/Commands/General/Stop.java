package Serial.Commands.General;

import Serial.Commands.ICommand;

public class Stop implements ICommand
{
   private final String command = "x";

    public String getCommand()
    {
        return command;
    }
}
