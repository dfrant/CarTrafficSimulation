import g4p_controls.*;
import processing.core.PApplet;

import javax.swing.JOptionPane;

public class Interface {
    private GButton btnStart;
    private GButton btnPause;
    private GButton btnExit;
    private GPanel pnl;
    private GPanel pnlInfo;
    private GPanel pnlRule;
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
        gapY = 45;
        elemWidth = pnlWidth - 2*gapX;
        elemHeight = 20;

        roadX = pnlWidth;
        roadY = pnlHeight / 2 - lengthCar;

        pnl = new GPanel(obj, 0, 0, pnlWidth, pnlHeight, "ввод параметров");
        pnl.setCollapsible(false);
        pnl.setDraggable(false);
        for (int i = 0; i < textField.length; i++) {
            textField[i] = new GTextField(obj, gapX, gapY + (i * gapY), elemWidth, elemHeight);
            textField[i].setTextEditEnabled(true);
            pnl.addControl(textField[i]);
        }

        int j = 0;
        btnStart = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "начать симуляцию");
        btnStart.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnStart);
        btnStart.setEnabled(true);
        j++;
        btnPause = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "пауза");
        btnPause.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnPause);
        btnPause.setEnabled(false);
        j++;
        btnExit = new GButton(obj, gapX, gapY + (textField.length + j) * gapY, pnlWidth - 2 * gapX, elemHeight, "выход");
        btnExit.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnExit);
        for (int i = 0; i < label.length; i++) {
            label[i] = new GLabel(obj, (float) gapX / 2, (-1) * gapX + (i * gapY), pnlWidth, pnlHeight / ((float) gapX / 2) - 2 * gapX);
            label[i].setLocalColorScheme(G4P.CYAN_SCHEME);
            pnl.addControl(label[i]);
        }
        label[label.length - 6].setText("0)минимальная скорость (мм/с)");
        label[label.length - 5].setText("1)максимальная скорость (мм/с)");
        label[label.length - 4].setText("2)минимальный интервал появления (сек)");
        label[label.length - 3].setText("3)максимальный интервал появления (сек)");
        label[label.length - 2].setText("4)dv уменьшения скорости (мм/c)");
        label[label.length - 1].setText("5)dt движения с меньшей скоростью (cек)");

        pnlInfoWidth = (float) (pnlWidth * (1.5));
        pnlInfoHeight = pnlHeight / 4;
        pnlInfo = new GPanel(obj, pnlWidth + gapX, 0, pnlInfoWidth, pnlInfoHeight, "значения цветов");
        pnlInfo.setCollapsible(false);
        pnlInfo.setDraggable(false);

        pnlRule = new GPanel(obj, pnlWidth + pnlInfoWidth + 2*gapX, 0, pnlInfoWidth, pnlInfoHeight, "правила симуляции");
        pnlRule.setCollapsible(false);
        pnlRule.setDraggable(false);

        GLabel labelInfo = new GLabel(obj, (float) gapX / 2, gapX, pnlInfoWidth, pnlInfoHeight);
        GLabel labelRuleInfo = new GLabel(obj, (float) gapX / 2, gapX, pnlInfoWidth, pnlInfoHeight);
        labelInfo.setLocalColorScheme(G4P.CYAN_SCHEME);
        labelRuleInfo.setLocalColorScheme(G4P.CYAN_SCHEME);
        labelInfo.setText("зеленый - постоянная скорость\n" + "синий - торможение\n" + "розовый - ускорение\n" + "красный - авария\n" + "желтый - задержка по щелчку мыши\n" + "оранжевый - задерка и авария\n");
        labelRuleInfo.setText("Параметры: вводите целые и положительные числа\nСкорость-величина,задаваемая в диапазоне(например,от 10 до 50 мм/c)\nВременной интервал между появлениями машин задается в диапазоне(например, от 1 до 5 секунд)\nЛюбую машину можно неоднократно искусственно затормозить на dv нажатием мыши");
        pnlInfo.addControl(labelInfo);
        pnlRule.addControl(labelRuleInfo);
    }

    public void buttonHandler(GButton button, GEvent event) { //reacts to pressing certain buttons
        if (button == btnStart && event == GEvent.CLICKED) {
                String answer;
                int[] answerNumber = new int[textField.length];
            if (!Model.isStart()) {
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
                    } else {
                        JOptionPane.showMessageDialog(null, "Некорректный ввод параметра " + j + "\nПараметры должны быть целыми и положительными числами", "", JOptionPane.ERROR_MESSAGE);
                        textField[j].setText("");
                        return;
                    }
                }
                if (Model.getParametersCounter() == Model.getParametersNumber()) {
                    if (answerNumber[0] > answerNumber[1]) {
                        JOptionPane.showMessageDialog(null, "Минимальное значение скорости должно быть меньше или равно максимальному", "", JOptionPane.ERROR_MESSAGE);
                        textField[0].setText("");
                        textField[1].setText("");
                        return;
                    }
                    if (answerNumber[2] > answerNumber[3]) {
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
                    for (GTextField gTextField : textField) {
                        gTextField.setTextEditEnabled(false);
                    }
                    btnStart.setText("новая симуляция");
                    btnPause.setEnabled(true);
                    Model.addFirstCar();
                    Model.setStart(true);
                    Model.setTime(0);
                }
            }else{
                for (GTextField gTextField : textField) {
                    gTextField.setTextEditEnabled(true);
                    gTextField.setText(" ");
                }
                Model.setStart(false);
                Model.setParametersCounter(0);
                btnPause.setEnabled(false);
                btnStart.setText("начать симуляцию");
                Model.deleteCars();
            }
        }
        if (button == btnPause && event == GEvent.CLICKED) {
            if (Model.isStart()) {
                btnPause.setText("продолжить");
                btnStart.setEnabled(false);
                Model.setStart(false);
                Model.setPause(true);
            }else{
                btnStart.setEnabled(true);
                Model.setStart(true);
                Model.setPause(false);
                Model.setTime(0);
                btnPause.setText("пауза");
            }
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
