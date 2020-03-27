import processing.core.PApplet;

import java.util.concurrent.ThreadLocalRandom;

public class Model extends PApplet {
    private static Manager manager = new Manager();
    private static Interface interf;
    private static float time;
    private float dt; //time since previous call

    private static int minSpeed;
    private static int maxSpeed;
    private static int minInterval;
    private static int maxInterval;
    private static int valueReducingSpeed;
    private static int timeReducingSpeed;

    private static float intervalCounter;

    private static float lengthCar = 40;
    private static float heightCar = 40;
    private static int windowWidth = 1000;
    private static int windowHeight = 500;

    private static int parametersNumber = 6;
    private static int parametersCounter = 0;

    private static boolean start = false;

    public void settings() {
        size(windowWidth, windowHeight);
    } // runs once to specify the size of the window

    public void setup() { //runs once to set the necessary parameters(interface parameters and time at the beginning) before the main cycle of simulation
        interf = new Interface(this, parametersNumber, lengthCar);
        time = 0;
    }

    public void draw() { //runs in a loop - the main cycle of simulation
        if (start) {
            interf.drawRoad(this, lengthCar);
            if (time == 0)
                dt = time / 1000;
            else
                dt = (millis() - time) / 1000;
            if (!manager.getCarsList().isEmpty()) {
                for (int i = 0; i < manager.getCarsList().size(); i++) {
                    Car currentCar = manager.getCar(i);
                    manager.updateSpeeds(i - 1, i, dt);
                    currentCar.check(this.mousePressed, this.mouseX, this.mouseY, valueReducingSpeed, timeReducingSpeed);
                    currentCar.move(dt);
                    interf.drawCar(this, currentCar);

                    if (currentCar.isAccidentHappened()) {
                        if (currentCar.getCurrentSpeed() == 0)
                            currentCar.setAcceleration(0);
                        if (Math.floor(currentCar.getTimeInAccident()) >= timeReducingSpeed) {
                            if ((i == 0) || (i > 0 && (manager.getCar(i - 1).getCoordX() - currentCar.getCoordX() - currentCar.getLength() > 3 * currentCar.getLength()))) {
                                currentCar.setAccidentHappened(false);
                                currentCar.setTimeInAccident(0);
                            }
                        }
                        currentCar.setTimeInAccident(currentCar.getTimeInAccident() + dt);
                    }
                    if (currentCar.isInDelay()) {
                        if (currentCar.getActualTimeReducingSpeed() <= 0) {
                            currentCar.setActualTimeReducingSpeed(0);
                            currentCar.setInDelay(false);
                        }
                        currentCar.setActualTimeReducingSpeed(currentCar.getActualTimeReducingSpeed() - dt);
                    }
                }
            }
            if (intervalCounter <= 0 && (manager.getCarsList().isEmpty() || manager.getCarsList().getLast().getCoordX() + manager.getCarsList().getLast().getLength() - interf.getPnlWidth() > 3 * manager.getCarsList().getLast().getLength())) {
                if (!manager.getCarsList().isEmpty()) {
                    manager.getCarsList().add(new Car(manager.getCarsList().size(), lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY() + heightCar / 2, getRnd(minSpeed, maxSpeed)));
                } else {
                    manager.getCarsList().add(new Car(0, lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY() + heightCar / 2, getRnd(minSpeed, maxSpeed)));
                }
                intervalCounter = getRnd(minInterval, maxInterval);
            }
            intervalCounter -= dt;
            time = millis();
        }
    }

    public static void main(String[] args) {
        PApplet.main("Model");
    }

    private static int getRnd(int min, int max) { //gives a random value between min and max
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static void deleteCars() {
        while (!manager.getCarsList().isEmpty()) {
            manager.getCarsList().removeLast();
        }
    }

    public static void addFirstCar() {
        intervalCounter = getRnd(minInterval, maxInterval);
        manager.getCarsList().addFirst(new Car(0, lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY() + heightCar / 2, getRnd(minSpeed, maxSpeed)));
    }

    public static void setMinSpeed(int minSpeed) {
        Model.minSpeed = minSpeed;
    }


    public static void setMaxSpeed(int maxSpeed) {
        Model.maxSpeed = maxSpeed;
    }


    public static void setMinInterval(int minInterval) {
        Model.minInterval = minInterval;
    }

    public static void setMaxInterval(int maxInterval) {
        Model.maxInterval = maxInterval;
    }


    public static void setValueReducingSpeed(int valueReducingSpeed) {
        Model.valueReducingSpeed = valueReducingSpeed;
    }


    public static void setTimeReducingSpeed(int timeReducingSpeed) {
        Model.timeReducingSpeed = timeReducingSpeed;
    }

    public static int getParametersNumber() {
        return parametersNumber;
    }

    public static int getParametersCounter() {
        return parametersCounter;
    }

    public static void setParametersCounter(int parametersCounter) {
        Model.parametersCounter = parametersCounter;
    }

    public static void setStart(boolean start) {
        Model.start = start;
    }

    public static void setTime(float time) {
        Model.time = time;
    }
}
