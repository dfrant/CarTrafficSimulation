import java.util.LinkedList;

public class Manager {
    LinkedList<Car> cars = new LinkedList<Car>();
    private float brakeLow = -200;

    private float getBrakeLow() {
        return brakeLow;
    }

    //"updateSpeeds" calculates the distance between a pair of cars with numbers carFrontNumber and carNextNumber
    //calls the necessary braking,acceleration or accident functions
    public void updateSpeeds(int carFrontNumber, int carNextNumber, float dt){
        if (carNextNumber != 0) {
            float dist = cars.get(carFrontNumber).getCoordX() + cars.get(carFrontNumber).getLength() / 2 - cars.get(carNextNumber).getCoordX() - cars.get(carNextNumber).getLength() / 2;
            if (!cars.get(carNextNumber).isAccidentHappened() && !cars.get(carNextNumber).isInDelay()) {
                if (dist <= 3 * cars.get(carNextNumber).getLength() && dist > cars.get(carNextNumber).getLength()) {
                    cars.get(carNextNumber).reduceSpeed(getBrakeLow(), cars.get(carFrontNumber).getCurrentSpeed(), dt);
                    return;
                }
            }
            if (cars.get(carNextNumber).getCurrentSpeed() > 0) {
                if (dist <= cars.get(carNextNumber).getLength()) {
                    isAccident(carFrontNumber, carNextNumber);
                    return;
                }
            }
        }
        if (!cars.get(carNextNumber).isAccidentHappened() && !cars.get(carNextNumber).isInDelay()) {
            if (cars.get(carNextNumber).getCurrentSpeed() < cars.get(carNextNumber).getInitialSpeed()) {
                float acl = getBrakeLow() * (-1);
                cars.get(carNextNumber).increaseSpeed(acl, dt);
            }
        }
    }
    //"isAccident" changes the speeds and accelerations of cars in case of accident
    private void isAccident(int carFront, int carNext) {
        float avSpeed = cars.get(carNext).getCurrentSpeed();
        int next = carNext, front = carFront;
        int carsInAccident = 1;
        cars.get(carNext).setAccidentHappened(true);
        cars.get(carFront).setAccidentHappened(true);
        while (front >= 0 && cars.get(front).isAccidentHappened() && (cars.get(front).getCoordX() + cars.get(front).getLength() / 2 - cars.get(next).getCoordX() - cars.get(next).getLength() / 2 <= cars.get(next).getLength())){
            avSpeed += cars.get(front).getCurrentSpeed();
            next--;
            front--;
            carsInAccident ++;
        }
        avSpeed /= carsInAccident;
        next = carNext;
        front = carFront;
        cars.get(next).setCurrentSpeed(avSpeed);
        cars.get(next).setAcceleration(getBrakeLow());
        while (front >= 0 && cars.get(front).isAccidentHappened() && (cars.get(front).getCoordX() + cars.get(front).getLength() / 2 - cars.get(next).getCoordX() - cars.get(next).getLength() / 2 <= cars.get(next).getLength())){
            cars.get(front).setCurrentSpeed(avSpeed);
            cars.get(front).setAcceleration(getBrakeLow());
            next--;
            front--;
        }
    }
}
