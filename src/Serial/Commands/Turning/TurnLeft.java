package Serial.Commands.Turning;


public class TurnLeft
{
    private int angle;

    private String command = "l";

    public TurnLeft(int angle)
    {
        this.angle = angle;
        command += this.angle;
    }

    public String getCommand()
    {
        return this.command;
    }
}
