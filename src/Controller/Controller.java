package Controller;

import Serial.Commands.General.Scan;
import Serial.Commands.General.ToggleDetector;
import Serial.Commands.Movement.Backward;
import Serial.Commands.Movement.Forward;
import Serial.Commands.Turning.TurnLeft;
import Serial.Commands.Turning.TurnRight;
import Serial.Connection.Connection;
import Serial.Connection.GeneralUtil.ConnectionUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Controller
{
    @FXML
    private VBox mainWindow;

    @FXML
    private ChoiceBox<String> scannedSerialPorts;

    @FXML
    private ToggleButton toggleColorButton;

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

    @FXML
    private Button rotLeftButton;

    @FXML
    private Button rotRightButton;

    @FXML
    private Canvas sketchPad;

    @FXML
    private TextField angleTextField;

    @FXML
    private TextArea logBox;

    @FXML
    private Button eraseButton;

    @FXML
    private GraphicsContext canvasContext;

    @FXML
    private ColorPicker colorPicker;

    private Connection serialConnection;

    private boolean portNameFlag;

    @FXML
    private void initialize()
    {
        commandList = new ArrayList<>();

        scannedSerialPorts.setItems(ConnectionUtil.scanForPorts());

        moveSelectBox = new ChoiceBox<>();

        moveSelectBox.getItems().addAll("Forward", "Forward (No Navigation)", "Backward", "Backward (No Navigation)");

        canvasContext = sketchPad.getGraphicsContext2D();

        setListeners();
    }

    @FXML
    private void setListeners()
    {
        scannedSerialPorts.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            portNameFlag = true;
        });

        sketchPad.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            canvasContext.beginPath();
            canvasContext.moveTo(e.getX(), e.getY());
            canvasContext.stroke();
            canvasContext.closePath();
        });

        sketchPad.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            canvasContext.lineTo(e.getX(), e.getY());
            canvasContext.stroke();
        });

        eraseButton.setOnAction( e -> {
            canvasContext.clearRect(sketchPad.getScaleX(), sketchPad.getScaleY(), sketchPad.getWidth(), sketchPad.getHeight());
        });

        colorPicker.setOnAction( e -> {
            canvasContext.setStroke(colorPicker.getValue());
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
            serialConnection = new Connection(scannedSerialPorts.getSelectionModel().getSelectedItem(), this);
            serialConnection.connect();
            if (serialConnection.isConnectionActive())
            {
                cmdViewBox.appendText("Connection was successful.\n");
            }
            else
            {
                cmdViewBox.appendText("Unable to connect. Please try again.");
            }
        }
    }

    @FXML
    private void moveCommand(ActionEvent e)
    {
        if (serialConnection == null)
        {
            return;
        }

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

        int dist = Math.max(10, Integer.parseInt(moveDistanceField.getText()));

        if (e.getSource() == forwardButton)
        {
            Forward cmd = new Forward(dist);
            moveDistanceField.clear();
            commandList.add(cmd.toString());
            cmdViewBox.appendText("Moving Forward " + cmd.getDistance() + " cm " + '\n');
            sendCommand(cmd.getCommand());
        }
        else if (e.getSource() == backwardButton)
        {
            Backward cmd = new Backward(dist);
            moveDistanceField.clear();
            commandList.add(cmd.toString());
            cmdViewBox.appendText("Moving Backward " + cmd.getDistance() + " cm " + '\n');
            sendCommand(cmd.getCommand());
        }
    }

    @FXML
    private void toggleColorDetector()
    {
        if (serialConnection == null)
        {
            return;
        }

        ToggleDetector cmd = new ToggleDetector();
        cmdViewBox.appendText("Toggled the color detector.\n");
        sendCommand(cmd.command);
    }

    @FXML
    private void turnCommand(ActionEvent e)
    {
        if (serialConnection == null)
        {
            return;
        }

        if (angleTextField.getText().length() == 0)
        {
            return;
        }

        for (int i = 0; i < angleTextField.getText().length(); i++)
        {
            if (!Character.isDigit(angleTextField.getText().charAt(i)))
            {
                return;
            }
        }

        int angle = Math.max(10, Integer.parseInt(angleTextField.getText()));

        if (e.getSource() == rotLeftButton)
        {
            TurnLeft cmd = new TurnLeft(angle);
            angleTextField.clear();
            commandList.add(cmd.getCommand());
            cmdViewBox.appendText("Turn left " + cmd.getAngle() + " degrees.\n");
            sendCommand(cmd.getCommand());
        }
        else if (e.getSource() == rotRightButton)
        {
            TurnRight cmd = new TurnRight(angle);
            angleTextField.clear();
            commandList.add(cmd.getCommand());
            cmdViewBox.appendText("Turn right " + cmd.getAngle() + " degrees.\n");
            sendCommand(cmd.getCommand());
        }
    }

    @FXML
    private void playSong()
    {
        if (serialConnection == null || !serialConnection.isConnectionActive())
        {
            return;
        }
        sendCommand("pla");
        cmdViewBox.appendText("Play song\n");
    }

    @FXML
    private void scanCommand()
    {
        Scan cmd = new Scan();

        commandList.add(cmd.getCommand());

        cmdViewBox.appendText("Scan" + '\n');

        sendCommand(cmd.getCommand());
    }

    @FXML
    private void stopClicked()
    {
        serialConnection.disconnect();
        if (!serialConnection.isConnectionActive())
        {
            cmdViewBox.appendText("Connection was disconnected.\n");
        }
        else
        {
            cmdViewBox.appendText("Unable to disconnect.\n");
        }
    }

    @FXML
    private void portScanClicked()
    {
        scannedSerialPorts.setItems(ConnectionUtil.scanForPorts());
    }

    @FXML
    public void writeToLogBox(String dataString)
    {
        logBox.appendText(dataString);
    }

    @FXML
    private void sendCommand(String command)
    {
        serialConnection.send(command);
        try
        {
            Thread.sleep(250);
        }
        catch (InterruptedException ignored){}
    }

}
