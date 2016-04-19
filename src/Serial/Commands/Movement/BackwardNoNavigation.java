package Serial.Commands.Movement;

public class BackwardNoNavigation extends Movements
{
    private String command = "c";

    private int distance;

    public BackwardNoNavigation(int distance)
    {
        this.distance = distance;
        command += distance;
    }

    @Override
    public String getCommand()
    {
        return command;
    }
}
