package lab1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Lab1_Kruskal {

  static class Edge implements Comparable<Edge> {

    int firstNodeIndex, secondNodeIndex, cost;

    public Edge(int firstNodeIndex, int secondNodeIndex, int cost) {
      this.firstNodeIndex = firstNodeIndex;
      this.secondNodeIndex = secondNodeIndex;
      this.cost = cost;
    }

    @Override
    public int compareTo(Edge other) {
      return cost - other.cost;
    }
  }

  private final int numberOfNodes;
  private final List<Edge> edges;

  private boolean solved;
  private boolean mstExists;

  private Edge[] mst;
  private long mstCost;

  public Lab1_Kruskal(List<Edge> edges, int numberOfNodes) {
    if (edges == null || numberOfNodes <= 1) {
      throw new IllegalArgumentException();
    }
    this.edges = edges;
    this.numberOfNodes = numberOfNodes;
  }

  public Edge[] getMst() {
    kruskals();
    return mstExists ? mst : null;
  }

  public Long getMinimumSpanningTreeCost() {
    kruskals();
    return mstExists ? mstCost : null;
  }

  private void kruskals() {
    if (solved) {
      return;
    }

    PriorityQueue<Edge> pq = new PriorityQueue<>(edges);
    UnionFind uf = new UnionFind(numberOfNodes);

    int index = 0;
    mst = new Edge[numberOfNodes - 1];

    while (!pq.isEmpty()) {
      Edge edge = pq.poll();

      if (uf.connected(edge.firstNodeIndex, edge.secondNodeIndex)) {
        continue;
      }

      uf.union(edge.firstNodeIndex, edge.secondNodeIndex);
      mstCost += edge.cost;
      mst[index++] = edge;

      if (uf.size(0) == numberOfNodes) {
        break;
      }
    }

    mstExists = (uf.size(0) == numberOfNodes);
    solved = true;
  }

  private static class UnionFind {

    private int[] id, sz;

    public UnionFind(int n) {
      id = new int[n];
      sz = new int[n];
      for (int i = 0; i < n; i++) {
        id[i] = i;
        sz[i] = 1;
      }
    }

    public int find(int p) {
      int root = p;
      while (root != id[root]) {
        root = id[root];
      }
      while (p != root) {
        int next = id[p];
        id[p] = root;
        p = next;
      }
      return root;
    }

    public boolean connected(int p, int q) {
      return find(p) == find(q);
    }

    public int size(int p) {
      return sz[find(p)];
    }

    public void union(int p, int q) {
      int root1 = find(p);
      int root2 = find(q);
      if (root1 == root2) {
        return;
      }
      if (sz[root1] < sz[root2]) {
        sz[root2] += sz[root1];
        id[root1] = root2;
      } else {
        sz[root1] += sz[root2];
        id[root2] = root1;
      }
    }
  }

  public static void main(String[] args) throws IOException {
    List<Edge> edges = new ArrayList<>();

    var numNodes = Files.lines(Path.of("src/main/resources/l1_2.txt"))
        .limit(1)
        .mapToInt(Integer::parseInt)
        .max()
        .orElseThrow(() -> new RuntimeException("There is no number of nodes!"));

    var inputList = Files.lines(Path.of("src/main/resources/l1_2.txt"))
        .skip(1)
        .collect(Collectors.toList());

    for (int i = 0; i < inputList.size(); i++) {
      var numbers = Arrays.stream(inputList.get(i).split(" "))
          .mapToInt(Integer::parseInt)
          .toArray();
      for (int j = 0; j < numbers.length; j++) {
        if (numbers[j] != 0) {
          edges.add(new Edge(i, j, numbers[j]));
        }
      }
    }

    Lab1_Kruskal solver;
    solver = new Lab1_Kruskal(edges, numNodes);
    Long cost = solver.getMinimumSpanningTreeCost();

    if (cost == null) {
      System.out.println("No MST does not exists");
    } else {
      System.out.println("MST cost: " + cost);
      for (Edge e : solver.getMst()) {
        System.out.printf("Used edge (%d, %d) with cost: %d%n", e.firstNodeIndex, e.secondNodeIndex, e.cost);
      }
    }
  }
}
