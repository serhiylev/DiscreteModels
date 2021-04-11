package lab3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class GFG {
  public static void main(String[] args) throws IOException {
//    N = Files.lines(Path.of("src/main/resources/l3-2.txt"))
//        .limit(1)
//        .mapToInt(Integer::parseInt)
//        .max()
//        .orElseThrow(() -> new RuntimeException("There is no number of nodes!"));
    int[][] travelPrices = Files.lines(Path.of("src/main/resources/l3-2.txt"))
        .skip(1)
        .map(e -> e.split(" "))
        .map(e -> Arrays.stream(e)
            .mapToInt(Integer::valueOf)
            .toArray())
        .toArray(int[][]::new);

    var tsp = new TSP(travelPrices);

    System.out.printf("Minimum cost : %d\n", tsp.final_res);
    System.out.print("Path Taken : ");
    for (int i = 0; i <= tsp.N; i++) {
      System.out.printf("%d ", tsp.final_path[i]);
    }
  }
}
