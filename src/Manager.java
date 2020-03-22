import java.util.LinkedList;

public class Manager {
    public LinkedList<Car> cars = new LinkedList<Car>();
    private float brakeLow = -200;

    public float getBrakeLow() {
        return brakeLow;
    }
    public void setBrakeLow(float brakeLow) {
        this.brakeLow = brakeLow;
    }

    public int updateSpeeds(int carFrontNumber, int carNextNumber, float dt){
        if (carNextNumber != 0) {
            float dist = cars.get(carFrontNumber).getCoordX() + cars.get(carFrontNumber).getLength() / 2 - cars.get(carNextNumber).getCoordX() - cars.get(carNextNumber).getLength() / 2;
            if (!cars.get(carNextNumber).getAccidentHappened() && !cars.get(carNextNumber).inDelay) {
                if (dist <= 3 * cars.get(carNextNumber).getLength() && dist > cars.get(carNextNumber).getLength()) {
                    cars.get(carNextNumber).reduceSpeed(getBrakeLow(), cars.get(carFrontNumber).getCurrentSpeed(), dt);
                    return 1;
                }
            }
            if (cars.get(carNextNumber).getCurrentSpeed() > 0) {
                if (dist <= cars.get(carNextNumber).getLength()) {
                    isAccident(carFrontNumber, carNextNumber);
                    return 0;
                }
            }
        }
        if (!cars.get(carNextNumber).getAccidentHappened() && !cars.get(carNextNumber).inDelay) {
            if (cars.get(carNextNumber).getCurrentSpeed() < cars.get(carNextNumber).getInitialSpeed()) {
                float acl = getBrakeLow() * (-1);
                cars.get(carNextNumber).increaseSpeed(acl, dt);
                return 1;
            }
        }
        return 1;
    }

    private void isAccident(int carFront, int carNext) {
        float avSpeed = cars.get(carNext).getCurrentSpeed();
        int next = carNext, front = carFront;
        int carsInAccident = 1;
        cars.get(carNext).setAccidentHappened(true);
        cars.get(carFront).setAccidentHappened(true);
        while (front >= 0 && cars.get(front).getAccidentHappened() && (cars.get(front).getCoordX() + cars.get(front).getLength() / 2 - cars.get(next).getCoordX() - cars.get(next).getLength() / 2 <= cars.get(next).getLength())){
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
        while (front >= 0 && cars.get(front).getAccidentHappened() && (cars.get(front).getCoordX() + cars.get(front).getLength() / 2 - cars.get(next).getCoordX() - cars.get(next).getLength() / 2 <= cars.get(next).getLength())){
            cars.get(front).setCurrentSpeed(avSpeed);
            cars.get(front).setAcceleration(getBrakeLow());
            next--;
            front--;
        }
    }
}
