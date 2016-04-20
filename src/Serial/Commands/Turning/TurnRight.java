package Serial.Commands.Turning;

import Serial.Commands.ICommand;

public class TurnRight implements ICommand
{
    private final int angle;

    private String command = "r";

    public TurnRight(int angle)
    {
        this.angle = angle;
        command += this.angle;
    }

    public int getAngle()
    {
        return this.angle;
    }

    public String getCommand()
    {
        return this.command;
    }
}
