package Serial.Commands.Movement;

public class Forward extends Movements
{
    private String command = "f";

    private int distance;

    public Forward(int distance)
    {
        this.distance = distance;
        command += distance;
    }

    @Override
    public String getCommand()
    {
        return this.command;
    }
}
