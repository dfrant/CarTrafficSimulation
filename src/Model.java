import processing.core.PApplet;

public class Model extends PApplet{
    private Manager manager = new Manager();
    private Interface interf;
    private float time;
    private float dt; //time since previous call

    private int minSpeed, maxSpeed;
    private int minInterval, maxInterval;
    private int valueReducingSpeed, timeReducingSpeed;

    private float intervalCounter;

    private float lengthCar = 40, heightCar = 40;
    private int windowWidth = 1000;
    private int windowHeight = 500;

    private int parametersNumber = 6;
    private int parametersCounter = 0;

    private boolean start = false;

    public void settings(){
        size(windowWidth,windowHeight);
    } // runs once to specify the size of the window
    public void setup() { //runs once to set the necessary parameters(interface parameters and time at the beginning) before the main cycle of simulation
        interf = new Interface(this, parametersNumber, lengthCar);
        time = 0;
    }
    public void draw(){ //runs in a loop - the main cycle of simulation
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
            intervalCounter = getRnd(minInterval, maxInterval);
            manager.cars.addFirst(new Car(this, 0, lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY()+heightCar/2, getRnd(minSpeed,maxSpeed)));
            interf.clicked[6] = false;
            start = true;
        }
        if (interf.clicked[7] && start) {
            start = false;
            interf.clicked[7] = false;
        }
        if (interf.clicked[8] && !start){
            start = true;
            interf.clicked[8] = false;
            time = 0;
        }
        if (interf.clicked[9]){
            time = 0;
            while (!manager.cars.isEmpty()){
                manager.cars.removeLast();
            }
            manager.cars.addFirst(new Car(this, 0, lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY()+heightCar/2, getRnd(minSpeed,maxSpeed)));
            interf.clicked[9] = false;
        }
        if(start) {
            interf.drawRoad(this, lengthCar);
            if (time == 0)
                dt = time / 1000;
            else
                dt = (millis() - time) / 1000;
            if (!manager.cars.isEmpty()) {
                for (int i = 0; i <manager.cars.size(); i++) {
                    manager.updateSpeeds(i - 1, i, dt);
                    manager.cars.get(i).check(this, valueReducingSpeed, timeReducingSpeed);
                    manager.cars.get(i).move(dt);
                    manager.cars.get(i).display(this);

                    if (manager.cars.get(i).isAccidentHappened()) {
                        if (manager.cars.get(i).getCurrentSpeed() == 0)
                            manager.cars.get(i).setAcceleration(0);
                        if (Math.floor(manager.cars.get(i).getTimeInAccident()) >= timeReducingSpeed) {
                            if ((i==0)||(i > 0 && (manager.cars.get(i - 1).getCoordX() - manager.cars.get(i).getCoordX() - manager.cars.get(i).getLength() > 3 * manager.cars.get(i).getLength()))) {
                                manager.cars.get(i).setAccidentHappened(false);
                                manager.cars.get(i).setTimeInAccident(0);
                            }
                        }
                        manager.cars.get(i).setTimeInAccident(manager.cars.get(i).getTimeInAccident() + dt);
                    }
                    if (manager.cars.get(i).isInDelay()){
                        if (Math.floor(manager.cars.get(i).getTimeInDelay()) >= manager.cars.get(i).getActualTimeReducingSpeed()){
                            manager.cars.get(i).setTimeInDelay(0);
                            manager.cars.get(i).setActualTimeReducingSpeed(0);
                            manager.cars.get(i).setInDelay(false);
                        }
                        manager.cars.get(i).setTimeInDelay(manager.cars.get(i).getTimeInDelay() + dt);
                    }
                }
            }
            if (intervalCounter<=0 && (manager.cars.isEmpty() || manager.cars.getLast().getCoordX()+manager.cars.getLast().getLength()-interf.getPnlWidth()>3*manager.cars.getLast().getLength())) {
                if (!manager.cars.isEmpty()) {
                    manager.cars.add(new Car(this, manager.cars.size(), lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY()+heightCar/2, getRnd(minSpeed, maxSpeed)));
                }else {
                    manager.cars.add(new Car(this, 0, lengthCar, heightCar, interf.getPnlWidth() - lengthCar, interf.getRoadY() + heightCar/2, getRnd(minSpeed,maxSpeed)));
                }
                intervalCounter = getRnd(minInterval, maxInterval);
            }
            intervalCounter -= dt;
            time = millis();
        }
    }
    public static void main(String[] args){
        PApplet.main("Model");
    }
    private static int getRnd(int min, int max){ //gives a random value between min and max
        max -= min;
        return (int)(Math.random() * ++max) + min;
    }
}
