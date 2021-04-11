package lab3;

import java.util.Arrays;

class TSP {

  private final int[][] adj;

  private final int matrixSize;

  private final int[] finalPath;

  private final boolean[] visited;

  private int finalResult = Integer.MAX_VALUE;


  TSP(int[][] inputArray, int size) {
    this.matrixSize = size;
    this.adj = inputArray;
    this.finalPath = new int[matrixSize + 1];
    this.visited = new boolean[matrixSize];
  }

  private void copyToFinal(int[] curr_path) {
    if (matrixSize >= 0) {
      System.arraycopy(curr_path, 0, finalPath, 0, matrixSize);
    }
    finalPath[matrixSize] = curr_path[0];
  }

  private int firstMin(int[][] adj, int i) {
    int min = Integer.MAX_VALUE;
    for (int k = 0; k < matrixSize; k++) {
      if (adj[i][k] < min && i != k) {
        min = adj[i][k];
      }
    }
    return min;
  }

  private int secondMin(int[][] adj, int i) {
    int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
    for (int j = 0; j < matrixSize; j++) {
      if (i == j) {
        continue;
      }

      if (adj[i][j] <= first) {
        second = first;
        first = adj[i][j];
      } else if (adj[i][j] <= second && adj[i][j] != first) {
        second = adj[i][j];
      }
    }
    return second;
  }

  private void TSPRec(int[][] adj, int curr_bound, int curr_weight, int level, int[] curr_path) {
    if (level == matrixSize) {
      if (adj[curr_path[level - 1]][curr_path[0]] != 0) {
        int curr_res = curr_weight +
            adj[curr_path[level - 1]][curr_path[0]];
        if (curr_res < finalResult) {
          copyToFinal(curr_path);
          finalResult = curr_res;
        }
      }
      return;
    }

    for (int i = 0; i < matrixSize; i++) {
      if (adj[curr_path[level - 1]][i] != 0 &&
          !visited[i]) {
        int temp = curr_bound;
        curr_weight += adj[curr_path[level - 1]][i];
        if (level == 1) {
          curr_bound -= ((firstMin(adj, curr_path[level - 1]) +
              firstMin(adj, i)) / 2);
        } else {
          curr_bound -= ((secondMin(adj, curr_path[level - 1]) +
              firstMin(adj, i)) / 2);
        }
        if (curr_bound + curr_weight < finalResult) {
          curr_path[level] = i;
          visited[i] = true;
          TSPRec(adj, curr_bound, curr_weight, level + 1,
              curr_path);
        }

        curr_weight -= adj[curr_path[level - 1]][i];
        curr_bound = temp;

        Arrays.fill(visited, false);
        for (int j = 0; j <= level - 1; j++) {
          visited[curr_path[j]] = true;
        }
      }
    }
  }

  public void compute() {
    int[] curr_path = new int[matrixSize + 1];
    int curr_bound = 0;

    Arrays.fill(curr_path, -1);
    Arrays.fill(visited, false);

    for (int i = 0; i < matrixSize; i++) {
      curr_bound += (firstMin(adj, i) + secondMin(adj, i));
    }

    curr_bound = (curr_bound == 1) ? 1 : curr_bound / 2;

    visited[0] = true;
    curr_path[0] = 0;

    TSPRec(adj, curr_bound, 0, 1, curr_path);

    System.out.printf("Minimum cost : %d\n", finalResult);
    System.out.print("Path Taken : ");
    for (int i = 0; i <= matrixSize; i++) {
      System.out.printf("%d ", finalPath[i]);
    }
  }
}
