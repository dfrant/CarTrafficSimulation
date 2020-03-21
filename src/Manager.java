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
        float dist = cars.get(carFrontNumber).getCoordX() - cars.get(carNextNumber).getCoordX() - cars.get(carNextNumber).getLength();
        if (!cars.get(carNextNumber).getAccidentHappened()) {
            if (dist <= 3 * cars.get(carNextNumber).getLength() && dist > cars.get(carNextNumber).getLength()) {
                cars.get(carNextNumber).reduceSpeed(getBrakeLow(), cars.get(carFrontNumber).getCurrentSpeed(), dt);
                return 1;
            }
            if (dist > 0 && dist <= cars.get(carNextNumber).getLength()) {
                isAccident(carFrontNumber, carNextNumber);
                return 0;
            }
            if (cars.get(carNextNumber).getCurrentSpeed() < cars.get(carNextNumber).getInitialSpeed()) {
                float acl = getBrakeLow() * (-1);
                cars.get(carNextNumber).increaseSpeed(acl, dt);
                return 1;
            }
        }
        if (dist <= 0) {
            //cars.get(carNextNumber).setCurrentSpeed(0);
            //cars.get(carNextNumber).setAcceleration(0);
            return -1;
        }
        return 1;
    }

    private void isAccident(int carFront, int carNext){
        float avSpeed = (cars.get(carFront).getCurrentSpeed() + cars.get(carNext).getCurrentSpeed())/2;
        cars.get(carFront).setCurrentSpeed(avSpeed);
        cars.get(carNext).setCurrentSpeed(avSpeed);
        cars.get(carFront).setAcceleration(getBrakeLow());
        cars.get(carNext).setAcceleration(getBrakeLow());
    }
}
