package Serial.Commands.Movement;

import Serial.Commands.ICommand;

public class Forward implements ICommand
{
    private String command = "f";

    private int distance;

    public Forward(int distance)
    {
        this.distance = distance;
        command += distance;
    }

    public int getDistance()
    {
        return this.distance;
    }

    public String getCommand()
    {
        return this.command;
    }
}
