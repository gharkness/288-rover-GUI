package Serial.Commands.Turning;


import Serial.Commands.ICommand;

public class TurnLeft implements ICommand
{
    private final int angle;

    private String command = "l";

    public TurnLeft(int angle)
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
