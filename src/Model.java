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
    float intervalCounter = 0;

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
        background(100);
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
            manager.cars.addFirst(new Car(this, 0, 200, rndSpeed));
            interf.clicked[6] = false;
            start = true;
        }

        if(start) {
            if (time == 0)
                dt = time / 1000;
            else
                dt = (millis() - time) / 1000;
            if (!manager.cars.isEmpty()) {
                for (int i = 0; i < manager.cars.size(); i++) {
                    int res = 1;
                    if (i > 0) {
                        res = manager.updateSpeeds(i - 1, i, dt);
                    }
                    //manager.cars.get(i).check(this, valueReducingSpeed, timeReducingSpeed);
                    if (res != -1) {
                        if (res == 0) {
                            manager.cars.get(i).setAccidentHappened(true);
                            manager.cars.get(i - 1).setAccidentHappened(true);
                        }
                        manager.cars.get(i).move(dt);
                    }
                    manager.cars.get(i).display(this);
                    if (res != -1) {
                        if (manager.cars.get(i).getAccidentHappened()) {
                            if (manager.cars.get(i).getInitialSpeed() == 0)
                                manager.cars.get(i).setAcceleration(0);
                            if (Math.floor(manager.cars.get(i).timeInAccident) >= timeReducingSpeed) {
                                if ((i > 0 && (manager.cars.get(i - 1).getCoordX() - manager.cars.get(i).getCoordX() - manager.cars.get(i).getLength() > 3 * manager.cars.get(i).getLength())) || (i == 0)) {
                                    manager.cars.get(i).increaseSpeed = true;
                                } else {
                                    manager.cars.get(i).increaseSpeed = false;
                                }
                                manager.cars.get(i).timeInAccident = 0;
                                manager.cars.get(i).setAccidentHappened(false);
                            }
                            manager.cars.get(i).timeInAccident += dt;
                        }
                        if ((i > 0 && (manager.cars.get(i - 1).getCoordX() - manager.cars.get(i).getCoordX() - manager.cars.get(i).getLength() > 3 * manager.cars.get(i).getLength())) || (i == 0)) {
                            if (manager.cars.get(i).increaseSpeed) {
                                float acl = manager.getBrakeLow() * (-1);
                                manager.cars.get(i).increaseSpeed(acl, dt);
                                manager.cars.get(i).color = Color.magenta;
                                if (manager.cars.get(i).getAcceleration() == 0) {
                                    manager.cars.get(i).increaseSpeed = false;
                                    manager.cars.get(i).color = Color.green;
                                }
                            }
                        }
                    }
                }
                if (manager.cars.get(0).getCoordX() >= width) {
                    manager.cars.remove(0);
                }
            }
            intervalCounter += dt;
            if (Math.floor(intervalCounter) == rndIntervalNumber) {
                if (!manager.cars.isEmpty())
                    //manager.cars.add(new Car(this, manager.cars.size(), manager.cars.getLast().initialCoord - 4*manager.cars.getLast().getLength(), getRnd(minSpeed, maxSpeed)));
                    manager.cars.add(new Car(this, manager.cars.size(), manager.cars.getLast().initialCoord - 4 * manager.cars.getLast().getLength(), rndSpeed * 5)); //only 2 cars will be created
                else
                    manager.cars.add(new Car(this, 0, 200, rndSpeed));
                //intervalCounter = 0;
                intervalCounter += 2;
                rndIntervalNumber = getRnd(minInterval, maxInterval);
            }
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
