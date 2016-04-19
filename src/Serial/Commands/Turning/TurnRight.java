package Serial.Commands.Turning;

public class TurnRight
{
    private int angle;

    private String command = "r";

    public TurnRight(int angle)
    {
        this.angle = angle;
        command += this.angle;
    }

}
