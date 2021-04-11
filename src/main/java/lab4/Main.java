package lab4;

import static util.FileUtil.readAsMatrix;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) {
    var strMatrix = readAsMatrix("src/main/resources/l4-2.txt");
    var matrix = Arrays.stream(strMatrix)
        .map(array -> Arrays.stream(array)
            .mapToInt(Integer::parseInt)
            .toArray()
        )
        .toArray(int[][]::new);
    var maxFlow = new FordFulkerson(matrix.length);
    var result = maxFlow.fordFulkerson(matrix, 0, matrix.length - 1);
    System.out.println("Tree: " + maxFlow.getResultTree());
    System.out.println("Weight: " + result);
  }
}
