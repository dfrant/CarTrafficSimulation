import g4p_controls.*;
import processing.core.PApplet;

public class Interface {
    GButton[] btnOK = new GButton[6];
    GButton btnStart;
    GButton btnExit;
    GPanel pnl;
    GTextField[] textField = new GTextField[6];
    GLabel[] label = new GLabel[6];
    private int answer;
    boolean allParameters = false;
    boolean[] clicked = new boolean[8];

    public Interface(PApplet obj) {
        G4P.setCtrlMode(GControlMode.CORNER);
        pnl = new GPanel(obj, 0, 0, obj.width/5, obj.height, "Parameters");
        pnl.setCollapsible(false);
        for(int i = 0; i < textField.length; i++) {
            textField[i] = new GTextField(obj, 10, 40 + (i*40), 3 * obj.width / 20, 20);
            textField[i].setTextEditEnabled(true);
            pnl.addControl(textField[i]);
        }
        for (int i = 0; i < btnOK.length; i++) {
            clicked[i] = false;
            btnOK[i] = new GButton(obj, 10 + 3 * obj.width / 20 , 40 + (i*40), obj.width / 5 - 3 * obj.width / 20 - 10 - 10, 20, "OK");
            pnl.addControl(btnOK[i]);
            btnOK[i].addEventHandler(this, "buttonHandler");
        }
        btnStart = new GButton(obj, 10, 40 + (btnOK.length * 40), obj.width/5 - 20, 20, "start the simulation");
        btnStart.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnStart);
        btnExit = new GButton(obj, 10, 40 + (btnOK.length + 2) * 40, obj.width/5 - 20 , 20, "exit");
        btnExit.addEventHandler(this, "buttonHandler");
        pnl.addControl(btnExit);
        for (int i = 0; i < label.length; i++) {
            label[i] = new GLabel(obj, 5, -10 + (i*40), obj.width/5, obj.height / 5 - 20);
            label[i].setLocalColorScheme(G4P.CYAN_SCHEME);
            pnl.addControl(label[i]);
        }
        label[0].setText("минимальная скорость");
        label[1].setText("максимальная скорость");
        label[2].setText("минимальный интервал");
        label[3].setText("максимальный интервал");
        label[4].setText("dv уменьшения скорости");
        label[5].setText("dt движения с меньшей скоростью");
    }

    public void buttonHandler(GButton button, GEvent event){
        for (int i = 0; i < btnOK.length; i++)
            if (button == btnOK[i] && event == GEvent.CLICKED){
                textField[i].setTextEditEnabled(false);
                clicked[i] = true;
                setAnswer(Integer.parseInt(textField[i].getText()));
            }
        if (button == btnStart && event == GEvent.CLICKED && allParameters){
            clicked[6] = true;
            btnStart.setEnabled(false);
        }
        if (button == btnExit && event == GEvent.CLICKED){
            System.exit(0);
        }
    }
    public int getAnswer(){
        return this.answer;
    }
    public void setAnswer(int answer) {
        this.answer = answer;
    }

}
