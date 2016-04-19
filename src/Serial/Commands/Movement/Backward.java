package Serial.Commands.Movement;

public class Backward extends Movements
{
    private String command = "b";

    private int distance;

    public Backward(int distance)
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
