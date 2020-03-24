import g4p_controls.*;
import processing.core.PApplet;

public class Interface {
    private GButton[] btnOK;
    private GButton btnStart;
    private GButton btnPause;
    private GButton btnRun;
    private GButton btnRestart;
    private GButton btnExit;
    private GPanel pnl;
    private GPanel pnlInfo;
    private GTextField[] textField;
    private GLabel[] label;
    private int answer;
    boolean allParameters = false;
    boolean[] clicked;
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
        btnOK = new GButton[parametersNumber];
        textField = new GTextField[parametersNumber];
        label = new GLabel[parametersNumber];
        int allButtonsNumber = parametersNumber + 4;
        clicked = new boolean[allButtonsNumber];

        pnlWidth = (float)obj.width/5;
        pnlHeight = obj.height;
        elemWidth = 3*pnlWidth/4;
        elemHeight = 20;
        gapX = 10;
        gapY = 40;

        roadX = pnlWidth;
        roadY = pnlHeight/2 - lengthCar;

        pnl = new GPanel(obj, 0, 0, pnlWidth, pnlHeight, "parameters");
        pnl.setCollapsible(false);
        pnl.setDraggable(false);
        for(int i = 0; i < textField.length; i++) {
            textField[i] = new GTextField(obj, gapX, gapY + (i*gapY), elemWidth, elemHeight);
            textField[i].setTextEditEnabled(true);
            pnl.addControl(textField[i]);
        }
        for (int i = 0; i < btnOK.length; i++) {
            clicked[i] = false;
            btnOK[i] = new GButton(obj, gapX + elemWidth , gapY + (i*gapY), pnlWidth - elemWidth - 2*gapX, elemHeight, "OK");
            pnl.addControl(btnOK[i]);
            btnOK[i].addEventHandler(this, "buttonHandler");
        }
        int j = 0;
        btnStart = new GButton(obj, gapX, gapY + (btnOK.length + j)* gapY, pnlWidth - 2*gapX, elemHeight, "start the simulation");
        btnStart.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnStart);
        btnStart.setEnabled(true);
        j++;
        btnPause = new GButton(obj, gapX, gapY + (btnOK.length + j) * gapY, pnlWidth - 2*gapX, elemHeight, "pause");
        btnPause.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnPause);
        btnPause.setEnabled(false);
        j++;
        btnRun = new GButton(obj, gapX, gapY + (btnOK.length + j) * gapY,pnlWidth - 2*gapX, elemHeight, "restart");
        btnRun.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnRun);
        btnRun.setEnabled(false);
        j++;
        btnRestart = new GButton(obj, gapX, gapY + (btnOK.length + j) * gapY, pnlWidth - 2*gapX, elemHeight, "restart all");
        btnRestart.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnRestart);
        btnRestart.setEnabled(false);
        j++;
        btnExit = new GButton(obj, gapX, gapY + (btnOK.length + j) * gapY, pnlWidth - 2*gapX, elemHeight, "exit");
        btnExit.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnExit);
        for (int i = 0; i < label.length; i++) {
            label[i] = new GLabel(obj, (float)gapX/2, (-1)*gapX + (i*gapY), pnlWidth, pnlHeight/((float)gapX/2) -2*gapX);
            label[i].setLocalColorScheme(G4P.CYAN_SCHEME);
            pnl.addControl(label[i]);
        }
        label[label.length - 6].setText("минимальная скорость");
        label[label.length - 5].setText("максимальная скорость");
        label[label.length - 4].setText("минимальный интервал");
        label[label.length - 3].setText("максимальный интервал");
        label[label.length - 2].setText("dv уменьшения скорости");
        label[label.length - 1].setText("dt движения с меньшей скоростью");

        pnlInfoWidth = (float)(pnlWidth * (1.5));
        pnlInfoHeight = pnlHeight/4;
        pnlInfo = new GPanel(obj, pnlWidth + gapX, 0, pnlInfoWidth, pnlInfoHeight, "information");
        pnlInfo.setCollapsible(false);
        pnlInfo.setDraggable(false);

        GLabel labelInfo = new GLabel(obj, (float)gapX/2, gapX, pnlInfoWidth, pnlInfoHeight);
        labelInfo.setLocalColorScheme(G4P.CYAN_SCHEME);
        labelInfo.setText("параметры - целые,положительные\n" + "зеленый - постоянная скорость\n" + "синий - торможение\n" + "розовый - ускорение\n"+"красный - авария\n"+"желтый - задержка по щелчку мыши\n"+"оранжевый - задерка и авария\n");
        pnlInfo.addControl(labelInfo);
    }

    public void buttonHandler(GButton button, GEvent event){ //reacts to pressing certain buttons
        int i;
        for (i = 0; i < btnOK.length; i++)
            if (button == btnOK[i] && event == GEvent.CLICKED){
                textField[i].setTextEditEnabled(false);
                clicked[i] = true;
                setAnswer(Integer.parseInt(textField[i].getText()));
            }
        if (button == btnStart && event == GEvent.CLICKED && allParameters){
            clicked[i] = true;
            btnStart.setEnabled(false);
            btnPause.setEnabled(true);
            btnRestart.setEnabled(true);
        }
        i++;
        if (button == btnPause && event == GEvent.CLICKED && allParameters){
            clicked[i] = true;
            btnPause.setEnabled(false);
            btnRun.setEnabled(true);
            btnRestart.setEnabled(false);
        }
        i++;
        if (button == btnRun && event == GEvent.CLICKED && allParameters){
            clicked[i] = true;
            btnRun.setEnabled(false);
            btnPause.setEnabled(true);
            btnRestart.setEnabled(true);
        }
        i++;
        if (button == btnRestart && event == GEvent.CLICKED && allParameters){
            clicked[i] = true;
        }
        if (button == btnExit && event == GEvent.CLICKED){
            System.exit(0);
        }
    }

    public void drawRoad(PApplet obj, float lengthCar){ //displays the road
        obj.background(backgroundColor);
        obj.fill(roadColor);
        obj.rect(roadX, roadY,obj.width - pnlWidth,2*lengthCar);
    }

    //getters and setters

    public int getAnswer(){
        return this.answer;
    }
    private void setAnswer(int answer) {
        this.answer = answer;
    }

    public float getPnlWidth() {
        return pnlWidth;
    }

    public float getRoadY() {
        return roadY;
    }
}
