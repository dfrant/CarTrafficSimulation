import processing.core.PApplet;
import java.awt.*;

public class Model extends PApplet{
    Manager manager = new Manager();
    Interface interf;
    float time;
    float dt; //time since previous call

    int minSpeed, maxSpeed;
    int minInterval, maxInterval;
    public int valueReducingSpeed, timeReducingSpeed;

    int rndSpeed, rndIntervalNumber;
    float intervalCounter;

    int parametersNumber = 6;
    int parametersCounter = 0;

    boolean start = false;

    public void settings(){
        size(1000,500);
    }
    public void setup() {
        interf = new Interface(this);
        time = 0;
    }
    public void draw(){
        background(167);
        if(interf.clicked[0] && !start){
            minSpeed = interf.getAnswer();
            parametersCounter ++;
            interf.clicked[0] = false;
        }
        if (interf.clicked[1] && !start){
            maxSpeed = interf.getAnswer();
            parametersCounter ++;
            interf.clicked[1] = false;
        }
        if (interf.clicked[2] && !start){
            minInterval = interf.getAnswer();
            parametersCounter ++;
            interf.clicked[2] = false;
        }
        if (interf.clicked[3] && !start){
            maxInterval = interf.getAnswer();
            parametersCounter ++;
            interf.clicked[3] = false;
        }
        if (interf.clicked[4] && !start){
            valueReducingSpeed = interf.getAnswer();
            parametersCounter ++;
            interf.clicked[4] = false;
        }
        if (interf.clicked[5] && !start){
            timeReducingSpeed = interf.getAnswer();
            parametersCounter ++;
            interf.clicked[5] = false;
        }
        if (parametersCounter == parametersNumber)
            interf.allParameters = true;
        if (interf.clicked[6] && !start){
            rndSpeed = getRnd(minSpeed,maxSpeed);
            rndIntervalNumber = getRnd(minInterval, maxInterval);
            intervalCounter = rndIntervalNumber;
            manager.cars.addFirst(new Car(this, 0, 180, rndSpeed));
            interf.clicked[6] = false;
            start = true;
        }

        if(start) {
            if (time == 0)
                dt = time / 1000;
            else
                dt = (millis() - time) / 1000;
            if (!manager.cars.isEmpty()) {
                for (int i = 0; i <manager.cars.size(); i++) { // order
                    manager.updateSpeeds(i - 1, i, dt);
                    manager.cars.get(i).check(this, valueReducingSpeed, timeReducingSpeed);
                    manager.cars.get(i).move(dt);
                    manager.cars.get(i).display(this);

                    if (manager.cars.get(i).getAccidentHappened()) {
                        if (manager.cars.get(i).getCurrentSpeed() == 0) //initialSpeed
                            manager.cars.get(i).setAcceleration(0);
                        if (Math.floor(manager.cars.get(i).timeInAccident) >= timeReducingSpeed) {
                            if ((i==0)||(i > 0 && (manager.cars.get(i - 1).getCoordX() - manager.cars.get(i).getCoordX() - manager.cars.get(i).getLength() > 3 * manager.cars.get(i).getLength()))) {
                                manager.cars.get(i).increaseSpeed = true;
                                manager.cars.get(i).setAccidentHappened(false);
                                manager.cars.get(i).timeInAccident = 0;
                            } else {
                                manager.cars.get(i).increaseSpeed = false;
                            }
                        }
                        manager.cars.get(i).timeInAccident += dt;
                    }
                    if (manager.cars.get(i).inDelay){
                        if (Math.floor(manager.cars.get(i).timeInDelay) >= manager.cars.get(i).actualTimeReducingSpeed){
                            manager.cars.get(i).timeInDelay = 0;
                            manager.cars.get(i).actualTimeReducingSpeed = 0;
                            manager.cars.get(i).inDelay = false;
                        }
                        manager.cars.get(i).timeInDelay += dt;
                    }
                }
//                if (manager.cars.get(0).getCoordX() >= width) {
//                    manager.cars.remove(0);
//                }
            }
            if (intervalCounter<=0 && ((manager.cars.getLast().getCoordX()+manager.cars.getLast().getLength()-200>3*manager.cars.getLast().getLength()) || manager.cars.isEmpty())) {
                if (!manager.cars.isEmpty())
                    //manager.cars.add(new Car(this, manager.cars.size(), manager.cars.getLast().initialCoord - 4*manager.cars.getLast().getLength(), getRnd(minSpeed, maxSpeed)));
                    manager.cars.add(new Car(this, manager.cars.size(), 180, rndSpeed * 5)); //only 2 cars will be created
                else
                    manager.cars.add(new Car(this, 0, 180, rndSpeed));
                //intervalCounter = 0;
                intervalCounter = getRnd(minInterval, maxInterval);
            }
            intervalCounter -= dt;
            time = millis();
        }
    }
    public static void main(String[] args){
        PApplet.main("Model");
    }
    private static int getRnd(int min, int max){
        max -= min;
        return (int)(Math.random() * ++max) + min;
    }
}
