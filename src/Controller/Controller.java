package Controller;

import Serial.Commands.General.Scan;
import Serial.Commands.Movement.Backward;
import Serial.Commands.Movement.BackwardNoNavigation;
import Serial.Commands.Movement.Forward;
import Serial.Commands.Movement.ForwardNoNavigation;
import Serial.Connection.Connection;
import Serial.Connection.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Controller
{
    @FXML
    private VBox mainWindow;

    @FXML
    private ChoiceBox<String> scannedSerialPorts;

    @FXML
    private Button setCommButton;

    @FXML
    private Button portScanButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button scanButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button backwardButton;

    @FXML
    private Button forwardNoNavButton;

    @FXML
    private Button backNoNavButton;

    @FXML
    private TextField moveDistanceField;

    @FXML
    private ChoiceBox<String> moveSelectBox;

    @FXML
    private TextArea cmdViewBox;

    @FXML
    private ArrayList<String> commandList;

    private Connection serialConnection;

    private boolean portNameFlag;

    @FXML
    private void initialize()
    {
        commandList = new ArrayList<String>();

        scannedSerialPorts.setItems(ConnectionUtil.scanForPorts());

        moveSelectBox = new ChoiceBox<>();

        moveSelectBox.getItems().addAll("Forward", "Forward (No Navigation)", "Backward", "Backward (No Navigation)");

        setListeners();
    }

    @FXML
    private void setListeners()
    {
        scannedSerialPorts.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            portNameFlag = true;
        });
    }

    @FXML
    private void connectionButtonClicked()
    {
        if (!portNameFlag)
        {
            return;
        }
        else
        {
            serialConnection = new Connection(scannedSerialPorts.getSelectionModel().getSelectedItem());
            serialConnection.connect();
            System.out.println(serialConnection.getOutputStream());
        }
    }

    @FXML
    private void moveCommand(ActionEvent e)
    {
        if (moveDistanceField.getText().length() == 0)
        {
            return;
        }

        for (int i = 0; i < moveDistanceField.getText().length(); i++)
        {
            if (!Character.isDigit(moveDistanceField.getText().charAt(i)))
            {
                return;
            }
        }

        if (e.getSource() == forwardButton)
        {
            int dist = Integer.parseInt(moveDistanceField.getText());
            if (dist < 10)
            {
                dist = 10;
            }

            Forward cmd = new Forward(Integer.parseInt(Integer.toString(dist)));
            moveDistanceField.clear();
            commandList.add(cmd.toString());
            System.out.println(cmd.getCommand());
            cmdViewBox.appendText(cmd.getCommand() + '\n');
            sendCommand(cmd.getCommand());
        }
        else if (e.getSource() == backwardButton)
        {
            Backward cmd = new Backward(Integer.parseInt(moveDistanceField.getText()));
            moveDistanceField.clear();
            commandList.add(cmd.toString());
            System.out.println(cmd.getCommand());
            cmdViewBox.appendText(cmd.getCommand() + '\n');
            sendCommand(cmd.getCommand());
        }
        else if (e.getSource() == forwardNoNavButton)
        {
            ForwardNoNavigation cmd = new ForwardNoNavigation(Integer.parseInt(moveDistanceField.getText()));
            moveDistanceField.clear();
            commandList.add(cmd.toString());
            System.out.println(cmd.getCommand());
            cmdViewBox.appendText(cmd.getCommand() + '\n');
            sendCommand(cmd.getCommand());
        }
        else if (e.getSource() == backNoNavButton)
        {
            BackwardNoNavigation cmd = new BackwardNoNavigation(Integer.parseInt(moveDistanceField.getText()));
            moveDistanceField.clear();
            commandList.add(cmd.toString());
            System.out.println(cmd.getCommand());
            cmdViewBox.appendText(cmd.getCommand() + '\n');
            sendCommand(cmd.getCommand());
        }
    }

    @FXML
    private void scanCommand()
    {
        Scan cmd = new Scan();

        System.out.println(cmd.getCommand());

        commandList.add(cmd.getCommand());

        cmdViewBox.appendText("Scan" + '\n');

        sendCommand(cmd.getCommand());
    }

    @FXML
    private void portScanClicked()
    {
        scannedSerialPorts.setItems(ConnectionUtil.scanForPorts());
    }

    @FXML
    private void sendCommand(String command)
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e){}
        serialConnection.send(command);
    }

}
