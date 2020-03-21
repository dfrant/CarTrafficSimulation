import processing.core.PApplet;

import java.awt.*;

public class Car {
    private int id;
    private float coordX, coordY = 200;
    public float initialCoord;
    private float length = 20, height = 20;
    private float initialSpeed, currentSpeed;
    private float acceleration;
    private boolean accidentHappened;
    public float timeInDelay;
    public float timeInAccident;
    public int actualTimeReducingSpeed;

    boolean increaseSpeed;

    boolean inDelay;
    public boolean clicked;
    public Color color;

    public int getId() {
        return id;
    }

    public float getCoordX() {
        return coordX;
    }
    public float getCoordY() {
        return coordY;
    }
    public float getLength() {
        return length;
    }

    public float getHeight() {
        return height;
    }

    public float getInitialSpeed() {
        return initialSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInitialSpeed(float initialSpeed) {
        this.initialSpeed = initialSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public void setCoordX(float coordX) {
        this.coordX = coordX;
    }

    public boolean getAccidentHappened() {
        return accidentHappened;
    }

    public void setAccidentHappened(boolean accidentHappened) {
        this.accidentHappened = accidentHappened;
    }


    public Car(PApplet obj, int id, float initialCoord, float initialSpeed){
        setId(id);
        this.initialCoord = initialCoord;
        setCoordX(initialCoord);
        setInitialSpeed(initialSpeed);
        setCurrentSpeed(initialSpeed);
        setAcceleration(0);
        setAccidentHappened(false);
        increaseSpeed = false;
        inDelay = false;
        timeInDelay = 0;
        timeInAccident = 0;
        clicked = false;
        color = Color.green;
        actualTimeReducingSpeed = 0;
    }

    public void display(PApplet obj) {
        obj.fill(color.getRGB());
        obj.rect(getCoordX(), getCoordY(), getLength(), getHeight());
    }
    public void move(float dt){
        if (getCurrentSpeed() + getAcceleration() * dt >= 0) {
            if (!getAccidentHappened()) {
                if (getCurrentSpeed() + getAcceleration() * dt > getCurrentSpeed()) {
                    color = Color.magenta;
                }else if (getCurrentSpeed() + getAcceleration() * dt == getCurrentSpeed()) {
                    color = Color.green;
                }else if (getCurrentSpeed() + getAcceleration() * dt < getCurrentSpeed()) {
                    color = Color.blue;
                }
            }else{
                color = Color.red;
            }
            setCurrentSpeed(getCurrentSpeed() + getAcceleration() * dt);
        }else {
            setCurrentSpeed(0);
        }
        if (getCurrentSpeed() * dt + getAcceleration() * dt * dt/2 >= 0)
            setCoordX(getCoordX() + getCurrentSpeed() * dt + getAcceleration() * dt * dt/2);
    }

    public void check(PApplet obj, float dspeed, int timeReducingSpeed){
        if (obj.mousePressed && !clicked){
            if (obj.mouseX<=getCoordX()+getLength() && obj.mouseX>=getCoordX() &&
                    obj.mouseY<=getCoordY() + getHeight() && obj.mouseY>getCoordY()){
                actualTimeReducingSpeed += timeReducingSpeed;
                if (getCurrentSpeed() - dspeed >= 0) {
                    setCurrentSpeed(getCurrentSpeed() - dspeed);
                }else {
                    setCurrentSpeed(0);
                }
                color = Color.blue;
                setAcceleration(0);
                clicked = true;
                inDelay = true;
            }
        }
        if (!obj.mousePressed && clicked)
            clicked = false;
    }
    public void reduceSpeed(float acceleration, float speedFront, float dt){
        if (getCurrentSpeed() + acceleration*dt >= speedFront){
            setAcceleration(acceleration);
        }else{
            setAcceleration(0);
        }
    }
    public void increaseSpeed(float acceleration, float dt){
        if (getCurrentSpeed() + acceleration*dt < getInitialSpeed()) {
            setAcceleration(acceleration);
        }else {
            setAcceleration(0);
        }
    }
}
