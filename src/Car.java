import java.awt.Color;

public class Car {
    private int id;
    private float coordX, coordY;
    private float length, height;
    private float initialSpeed, currentSpeed;
    private float acceleration;
    private boolean accidentHappened;
    private boolean inDelay;
    private float timeInAccident;
    private float actualTimeReducingSpeed;
    public boolean clicked;
    private Color color;


    public Car(int id, float length, float height, float initialCoord, float coordY, float initialSpeed) {
        setId(id);
        setLength(length);
        setHeight(height);
        setCoordX(initialCoord);
        setCoordY(coordY);
        setInitialSpeed(initialSpeed);
        setCurrentSpeed(initialSpeed);
        setAcceleration(0);
        setAccidentHappened(false);
        setInDelay(false);
        setTimeInAccident(0);
        setClicked(false);
        setColor(Color.green);
        setActualTimeReducingSpeed(0);
    }

    public void move(float dt) { //changes the coordinate,speed and color of the car in accordinance with specified acceleration
        if (getCurrentSpeed() + getAcceleration() * dt >= 0) {
            if (isAccidentHappened() && isInDelay()) {
                color = Color.orange;
            } else if (isAccidentHappened()) {
                color = Color.red;
            } else if (inDelay) {
                color = Color.yellow;
            } else {
                if (getCurrentSpeed() + getAcceleration() * dt > getCurrentSpeed()) {
                    color = Color.magenta;
                } else if (getCurrentSpeed() + getAcceleration() * dt == getCurrentSpeed()) {
                    color = Color.green;
                } else if (getCurrentSpeed() + getAcceleration() * dt < getCurrentSpeed()) {
                    color = Color.blue;
                }
            }
            setCurrentSpeed(getCurrentSpeed() + getAcceleration() * dt);
        } else {
            setCurrentSpeed(0);
        }
        if (getCurrentSpeed() * dt + getAcceleration() * dt * dt / 2 >= 0)
            shiftCarForward(dt);
    }

    public void check(boolean mousePressed,float mouseX,float mouseY, float dspeed, int timeReducingSpeed) { //checks whether the pushing of the mouse was on the car
        if (mousePressed && !isClicked()) {
            if (mouseX <= getCoordX() + getLength() && mouseX >= getCoordX() &&
                    mouseY <= getCoordY() + getHeight() && mouseY > getCoordY()) {
                setActualTimeReducingSpeed(getActualTimeReducingSpeed() + timeReducingSpeed);
                if (getCurrentSpeed() - dspeed >= 0) {
                    setCurrentSpeed(getCurrentSpeed() - dspeed);
                } else {
                    setCurrentSpeed(0);
                }
                setColor(Color.yellow);
                setAcceleration(0);
                setClicked(true);
                setInDelay(true);
            }
        }
        if (!mousePressed && isClicked())
            setClicked(false);
    }

    public void reduceSpeed(float acceleration, float speedFront, float dt) { //sets the acceleration according to the front car speed in case of braking
        if (getCurrentSpeed() + acceleration * dt >= speedFront) {
            setAcceleration(acceleration);
        } else {
            setCurrentSpeed(speedFront);
            setAcceleration(0);
        }
    }

    public void increaseSpeed(float acceleration, float dt) { //sets the acceleration according to the car initial speed in case of acceleration
        if (getCurrentSpeed() + acceleration * dt < getInitialSpeed()) {
            setAcceleration(acceleration);
        } else {
            setCurrentSpeed(getInitialSpeed());
            setAcceleration(0);
        }
    }

    private void shiftCarForward (float dt){
        setCoordX(getCoordX() + getCurrentSpeed() * dt + getAcceleration() * dt * dt / 2);
    }
    //getters and setters

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public float getCoordX() {
        return coordX;
    }

    private void setCoordX(float coordX) {
        this.coordX = coordX;
    }

    public float getCoordY() {
        return coordY;
    }

    private void setCoordY(float coordY) {
        this.coordY = coordY;
    }

    public float getLength() {
        return length;
    }

    private void setLength(float length) {
        this.length = length;
    }

    public float getHeight() {
        return height;
    }

    private void setHeight(float height) {
        this.height = height;
    }

    public float getInitialSpeed() {
        return initialSpeed;
    }

    private void setInitialSpeed(float initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    private float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isAccidentHappened() {
        return accidentHappened;
    }

    public void setAccidentHappened(boolean accidentHappened) {
        this.accidentHappened = accidentHappened;
    }

    public boolean isInDelay() {
        return inDelay;
    }

    public void setInDelay(boolean inDelay) {
        this.inDelay = inDelay;
    }


    public float getTimeInAccident() {
        return timeInAccident;
    }

    public void setTimeInAccident(float timeInAccident) {
        this.timeInAccident = timeInAccident;
    }

    public float getActualTimeReducingSpeed() {
        return actualTimeReducingSpeed;
    }

    public void setActualTimeReducingSpeed(float actualTimeReducingSpeed) {
        this.actualTimeReducingSpeed = actualTimeReducingSpeed;
    }

    private boolean isClicked() {
        return clicked;
    }

    private void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public Color getColor() {
        return color;
    }

    private void setColor(Color color) {
        this.color = color;
    }
}
