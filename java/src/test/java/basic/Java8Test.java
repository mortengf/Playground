package dk.untangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Car {
    String name;
    int topSpeed;
    boolean pollutes;

    Car(String name, int topSpeed, boolean pollutes) {
        this.name = name;
        this.topSpeed = topSpeed;
        this.pollutes = pollutes;
    }

    int getTopSpeed() { return topSpeed; }

    @Override
    public String toString() { return name; }
}

/**
 * Examples from: http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818?pgno=2
 *
 */
public class Java8Test {

    public static void main(String[] args) {
	    List<Car> cars = new ArrayList<Car>();
        cars.add(new Car("BMW", 270, true));
        cars.add(new Car("Mercedes", 260, true));
        cars.add(new Car("Tesla", 230, false));

        Stream<Car> polluters = cars.stream().filter(c -> c.pollutes == true);
        System.out.println("Polluters are: " + polluters.collect(Collectors.toList()));

        int sumOfMaxSpeeds = cars.stream().mapToInt(Car::getTopSpeed).sum();
        System.out.println("Sum of max speeds are: " + sumOfMaxSpeeds);

        Map<String, Integer> carsBySpeed = cars.stream().filter(c -> c.topSpeed > 250).collect(Collectors.toMap(c -> c.name, c -> c.topSpeed));
        System.out.println("Fast cars and their top speeds: " + carsBySpeed);

    }
}
