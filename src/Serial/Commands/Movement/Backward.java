package Serial.Commands.Movement;

import Serial.Commands.ICommand;

public class Backward implements ICommand
{
    private String command = "b";

    private int distance;

    public Backward(int distance)
    {
        this.distance = distance;
        command += distance;
    }

    public int getDistance()
    {
        return distance;
    }

    @Override
    public String getCommand()
    {
       return command;
    }
}
