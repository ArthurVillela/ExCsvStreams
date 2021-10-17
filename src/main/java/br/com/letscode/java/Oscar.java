package br.com.letscode.java;

import lombok.Value;

import static java.lang.Integer.parseInt;

@Value
public class Oscar {
    //Index; Year; Age; Name; Movie
    Integer index;
    Integer year;
    Integer age;
    String name;
    String movie;

    public static Oscar listaSplit(String line) {
        String[] split = line.split("; ");
        return new Oscar(
                parseInt(split[0]),
                parseInt(split[1]),
                parseInt(split[2]),
                split[3],
                split[4]
        );
    }
}
