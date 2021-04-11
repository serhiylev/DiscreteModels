package lab4;

import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

@Data
public class FordFulkerson {

  private final Map<String, Integer> resultTree = new TreeMap<>();
  private final int matrixSize;

  public FordFulkerson(int matrixSize) {
    this.matrixSize = matrixSize;
  }

  public int fordFulkerson(int[][] matrix, int vertexTo, int vertexFrom) {
    int currentVertex;
    int nextVertex;
    int pathFlow;
    int maxFlow = 0;
    var parent = new int[matrixSize];
    var operationGraph = Arrays.stream(matrix)
        .map(int[]::clone)
        .toArray(int[][]::new);

    while (bfs(operationGraph, vertexTo, vertexFrom, parent)) {
      var key = new StringBuilder();
      pathFlow = Integer.MAX_VALUE;
      for (nextVertex = vertexFrom; nextVertex != vertexTo; nextVertex = parent[nextVertex]) {
        currentVertex = parent[nextVertex];
        pathFlow = Math.min(pathFlow, operationGraph[currentVertex][nextVertex]);
      }
      for (nextVertex = vertexFrom; nextVertex != vertexTo; nextVertex = parent[nextVertex]) {
        currentVertex = parent[nextVertex];
        operationGraph[currentVertex][nextVertex] -= pathFlow;
        operationGraph[nextVertex][currentVertex] += pathFlow;
        key.insert(0, (currentVertex + 1) + " - " + (nextVertex + 1) + " ");
      }
      if (resultTree.containsKey(key.toString())) {
        resultTree.put(key.toString(), resultTree.get(key.toString()) + pathFlow);
      } else {
        resultTree.put(key.toString(), (pathFlow));
      }
      maxFlow += pathFlow;
    }
    return maxFlow;
  }

  private boolean bfs(int[][] rGraph, int vertexTo, int vertexFrom, int[] parent) {
    var visited = new boolean[matrixSize];
    Arrays.fill(visited, false);
    var queue = new LinkedList<Integer>();
    queue.add(vertexTo);
    visited[vertexTo] = true;
    parent[vertexTo] = -1;
    while (queue.size() != 0) {
      var u = queue.poll();
      for (int v = 0; v < matrixSize; v++) {
        if (!visited[v] && rGraph[u][v] > 0) {
          if (v == vertexFrom) {
            parent[v] = u;
            return true;
          }
          queue.add(v);
          parent[v] = u;
          visited[v] = true;
        }
      }
    }
    return false;
  }
}
