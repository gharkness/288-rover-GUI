package Serial.Commands.Movement;

public class ForwardNoNavigation extends Movements
{
    private String command = "g";

    private int distance;

    public ForwardNoNavigation(int distance)
    {
        this.distance = distance;
        command += this.distance;
    }

    @Override
    public String getCommand()
    {
        return this.command;
    }
}
