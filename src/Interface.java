import com.sun.org.apache.xpath.internal.operations.Mod;
import g4p_controls.*;
import processing.core.PApplet;

import javax.swing.JOptionPane;

public class Interface {
    private GButton btnStart;
    private GButton btnPause;
    private GButton btnRun;
    private GButton btnRestart;
    private GButton btnExit;
    private GPanel pnl;
    private GPanel pnlInfo;
    private GTextField[] textField;
    private GLabel[] label;
    private float pnlWidth;
    private float pnlHeight;
    private float pnlInfoWidth;
    private float pnlInfoHeight;
    private int gapX;
    private int gapY;
    private float elemWidth;
    private float elemHeight;

    private int backgroundColor = 167;
    private int roadColor = 50;
    private float roadX;
    private float roadY;

    public Interface(PApplet obj, int parametersNumber, float lengthCar) {

        G4P.setCtrlMode(GControlMode.CORNER);
        textField = new GTextField[parametersNumber];
        label = new GLabel[parametersNumber];

        pnlWidth = (float) obj.width / 5;
        pnlHeight = obj.height;
        gapX = 10;
        gapY = 40;
        elemWidth = pnlWidth - 2*gapX;
        elemHeight = 20;

        roadX = pnlWidth;
        roadY = pnlHeight / 2 - lengthCar;

        pnl = new GPanel(obj, 0, 0, pnlWidth, pnlHeight, "parameters");
        pnl.setCollapsible(false);
        pnl.setDraggable(false);
        for (int i = 0; i < textField.length; i++) {
            textField[i] = new GTextField(obj, gapX, gapY + (i * gapY), elemWidth, elemHeight);
            textField[i].setTextEditEnabled(true);
            pnl.addControl(textField[i]);
        }

        int j = 0;
        btnStart = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "start the simulation");
        btnStart.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnStart);
        btnStart.setEnabled(true);
        j++;
        btnPause = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "pause");
        btnPause.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnPause);
        btnPause.setEnabled(false);
        j++;
        btnRun = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "resume");
        btnRun.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnRun);
        btnRun.setEnabled(false);
        j++;
        btnRestart = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "restart all");
        btnRestart.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnRestart);
        btnRestart.setEnabled(false);
        j++;
        btnExit = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "exit");
        btnExit.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnExit);
        for (int i = 0; i < label.length; i++) {
            label[i] = new GLabel(obj, (float) gapX / 2, (-1) * gapX + (i * gapY), pnlWidth, pnlHeight / ((float) gapX / 2) - 2 * gapX);
            label[i].setLocalColorScheme(G4P.CYAN_SCHEME);
            pnl.addControl(label[i]);
        }
        label[label.length - 6].setText("0)минимальная скорость");
        label[label.length - 5].setText("1)максимальная скорость");
        label[label.length - 4].setText("2)минимальный интервал появления");
        label[label.length - 3].setText("3)максимальный интервал появления");
        label[label.length - 2].setText("4)dv уменьшения скорости");
        label[label.length - 1].setText("5)dt движения с меньшей скоростью");

        pnlInfoWidth = (float) (pnlWidth * (1.5));
        pnlInfoHeight = pnlHeight / 4;
        pnlInfo = new GPanel(obj, pnlWidth + gapX, 0, pnlInfoWidth, pnlInfoHeight, "information");
        pnlInfo.setCollapsible(false);
        pnlInfo.setDraggable(false);

        GLabel labelInfo = new GLabel(obj, (float) gapX / 2, gapX, pnlInfoWidth, pnlInfoHeight);
        labelInfo.setLocalColorScheme(G4P.CYAN_SCHEME);
        labelInfo.setText("параметры - целые,положительные\n" + "зеленый - постоянная скорость\n" + "синий - торможение\n" + "розовый - ускорение\n" + "красный - авария\n" + "желтый - задержка по щелчку мыши\n" + "оранжевый - задерка и авария\n");
        pnlInfo.addControl(labelInfo);
    }

    public void buttonHandler(GButton button, GEvent event) { //reacts to pressing certain buttons
        if (button == btnStart && event == GEvent.CLICKED) {
            String answer;
            int[] answerNumber = new int[textField.length];
            Model.setParametersCounter(0);
            for (int j = 0; j < textField.length; j++) {
                answer = textField[j].getText();
                try {
                    answerNumber[j] = Integer.parseInt(answer);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Некорректный ввод параметра " + j + "\nПараметры должны быть целыми и положительными числами", "", JOptionPane.ERROR_MESSAGE);
                    textField[j].setText("");
                    return;
                }
                if (answerNumber[j] > 0) {
                    Model.setParametersCounter(Model.getParametersCounter() + 1);
                }else{
                    JOptionPane.showMessageDialog(null, "Некорректный ввод параметра " + j + "\nПараметры должны быть целыми и положительными числами", "", JOptionPane.ERROR_MESSAGE);
                    textField[j].setText("");
                    return;
                }
            }
            if (Model.getParametersCounter() == Model.getParametersNumber()) {
                if (answerNumber[0] > answerNumber[1]){
                    JOptionPane.showMessageDialog(null, "Минимальное значение скорости должно быть меньше или равно максимальному", "", JOptionPane.ERROR_MESSAGE);
                    textField[0].setText("");
                    textField[1].setText("");
                    return;
                }
                if (answerNumber[2] > answerNumber[3]){
                    JOptionPane.showMessageDialog(null, "Минимальное значение интервала появления должно быть меньше или равно максимальному", "", JOptionPane.ERROR_MESSAGE);
                    textField[2].setText("");
                    textField[3].setText("");
                    return;
                }
                Model.setMinSpeed(answerNumber[0]);
                Model.setMaxSpeed(answerNumber[1]);
                Model.setMinInterval(answerNumber[2]);
                Model.setMaxInterval(answerNumber[3]);
                Model.setValueReducingSpeed(answerNumber[4]);
                Model.setTimeReducingSpeed(answerNumber[5]);
                for (GTextField gTextField : textField) gTextField.setTextEditEnabled(false);
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnRestart.setEnabled(true);
                Model.addFirstCar();
                Model.setStart(true);
            }
        }
        if (button == btnPause && event == GEvent.CLICKED) {
            btnPause.setEnabled(false);
            btnRun.setEnabled(true);
            btnRestart.setEnabled(false);
            Model.setStart(false);
        }
        if (button == btnRun && event == GEvent.CLICKED) {
            btnRun.setEnabled(false);
            btnPause.setEnabled(true);
            btnRestart.setEnabled(true);
            Model.setStart(true);
            Model.setTime(0);
        }
        if (button == btnRestart && event == GEvent.CLICKED) {
            Model.setTime(0);
            Model.deleteCars();
            Model.addFirstCar();
        }
        if (button == btnExit && event == GEvent.CLICKED) {
            System.exit(0);
        }
    }

    public void drawCar(PApplet obj, Car car){ //displays a car as a rectangle with given color
        obj.fill(car.getColor().getRGB());
        obj.rect(car.getCoordX(), car.getCoordY(), car.getLength(), car.getHeight());
        obj.fill(0); //black color
        obj.text(car.getId(), car.getCoordX() + car.getLength() / 2, car.getCoordY() + car.getLength() / 2);
        obj.fill(255); //white color
        obj.text(roundSpeedValueToString(car.getCurrentSpeed()), car.getCoordX(), car.getCoordY() - car.getLength() / 2, car.getCoordX(), car.getCoordY() + car.getHeight() / 2);
        if (car.isInDelay()){
            obj.fill(255); //white color
            double time = Math.ceil(car.getActualTimeReducingSpeed());
            if (time > 0) {
                obj.text(String.valueOf(time), car.getCoordX(), car.getCoordY() + car.getLength(), car.getCoordX(), car.getCoordY() + car.getHeight() / 2);
            }else{
                obj.text(String.valueOf(0), car.getCoordX(), car.getCoordY() + car.getLength(), car.getCoordX(), car.getCoordY() + car.getHeight() / 2);
            }
        }
    }
    private String roundSpeedValueToString(float speed){
        return String.valueOf(Math.round(speed * 100.0) / 100.0);
    }
    public void drawRoad(PApplet obj, float lengthCar) { //displays the road
        obj.background(backgroundColor);
        obj.fill(roadColor);
        obj.rect(roadX, roadY, obj.width - pnlWidth, 2 * lengthCar);
    }

    //getters and setters

    public float getPnlWidth() {
        return pnlWidth;
    }

    public float getRoadY() {
        return roadY;
    }
}
