package lab3;

import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class Main {

  @SneakyThrows
  public static void main(String[] args) {
    var size = Files.lines(Path.of("src/main/resources/l3-2.txt"))
        .limit(1)
        .mapToInt(Integer::parseInt)
        .max()
        .orElseThrow(() -> new RuntimeException("There is no number of nodes!"));
    int[][] travelPrices = Files.lines(Path.of("src/main/resources/l3-2.txt"))
        .skip(1)
        .map(e -> e.split(" "))
        .map(e -> Arrays.stream(e)
            .mapToInt(Integer::valueOf)
            .toArray())
        .toArray(int[][]::new);

    var tsp = new TSP(travelPrices, size);
    tsp.compute();
  }
}
