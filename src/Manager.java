import java.util.LinkedList;

public class Manager {
    private LinkedList<Car> cars = new LinkedList<Car>();
    private float brakeLow = -100;

    public LinkedList<Car> getCarsList() {
        return cars;
    }

    public Car getCar(int i) {
        return cars.get(i);
    }

    private float getBrakeLow() {
        return brakeLow;
    }

    //"updateSpeeds" calculates the distance between a pair of cars with numbers carFrontNumber and carNextNumber
    //calls the necessary braking,acceleration or accident functions
    public void updateSpeeds(int carFrontNumber, int carNextNumber, float dt) {
        Car carNext = getCar(carNextNumber);
        if (carNextNumber != 0) {
            Car carFront = getCar(carFrontNumber);
            float dist = carFront.getCoordX() + carFront.getLength() / 2 - carNext.getCoordX() - carNext.getLength() / 2;
            if (!carNext.isAccidentHappened() && !carNext.isInDelay()) {
                if (dist <= 3 * carNext.getLength() && dist > carNext.getLength()) {
                    carNext.reduceSpeed(getBrakeLow(), carFront.getCurrentSpeed(), dt);
                    return;
                }
            }
            if (carNext.getCurrentSpeed() > 0) {
                if (dist <= carNext.getLength()) {
                    isAccident(carFrontNumber, carNextNumber);
                    return;
                }
            }
        }
        if (!carNext.isAccidentHappened() && !carNext.isInDelay()) {
            if (carNext.getCurrentSpeed() < carNext.getInitialSpeed()) {
                float acl = getBrakeLow() * (-1);
                carNext.increaseSpeed(acl, dt);
            }
        }
    }

    //"isAccident" changes the speeds and accelerations of cars in case of accident
    private void isAccident(int carFrontN, int carNextN) {
        Car carFront = getCar(carFrontN);
        Car carNext = getCar(carNextN);
        float avSpeed = carNext.getCurrentSpeed();
        int next = carNextN, front = carFrontN;
        int carsInAccident = 1;
        carNext.setAccidentHappened(true);
        carFront.setAccidentHappened(true);
        while (front >= 0 && carFront.isAccidentHappened() && (carFront.getCoordX() + carFront.getLength() / 2 - carNext.getCoordX() - carNext.getLength() / 2 <= carNext.getLength())) {
            avSpeed += carFront.getCurrentSpeed();
            next--;
            front--;
            carsInAccident++;
            carNext = getCar(next);
            if (front >= 0)
                carFront = getCar(front);
        }
        avSpeed /= carsInAccident;
        next = carNextN;
        front = carFrontN;
        carNext = getCar(next);
        carFront = getCar(front);
        carNext.setCurrentSpeed(avSpeed);
        carNext.setAcceleration(getBrakeLow());
        while (front >= 0 && carFront.isAccidentHappened() && (carFront.getCoordX() + carFront.getLength() / 2 - carNext.getCoordX() - carNext.getLength() / 2 <= carNext.getLength())) {
            carFront.setCurrentSpeed(avSpeed);
            carFront.setAcceleration(getBrakeLow());
            next--;
            front--;
            carNext = getCar(next);
            if (front >= 0)
                carFront = getCar(front);
        }
    }
}
