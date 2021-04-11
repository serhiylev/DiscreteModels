package lab2;

import static util.FileUtil.getGraphForCustom;

import lab2.custom_сollections.UniqueQueue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

  private static final Queue<Set<Integer>> graphsToCheck = new UniqueQueue<>();
  private static final Set<Edge> edges = new LinkedHashSet<>();

  public static void main(String[] args) {
    var graph = getGraphForCustom("src/main/resources/l2-2.txt");
    initialiseVariables(graph);
    var minPathFinder = new PathFinder(edges);
    var verticesPaths = new ArrayList<Connection>();
    System.out.println("Unconnected graphs: " + graphsToCheck);
    while (graphsToCheck.size() > 1) {
      var graphToConnect = graphsToCheck.poll();
      var vp = minPathFinder.getWay(graphToConnect, graphsToCheck);
      verticesPaths.add(vp);
      var graphConnectedTo = graphsToCheck.stream()
          .filter(set -> set.contains(vp.getTo()))
          .findFirst()
          .orElse(null);
      graphsToCheck.remove(graphConnectedTo);
    }

    var graphEdgesSum = getAllGraphWeightsSum();
    var additionalEdgesSum = verticesPaths.stream()
        .map(Connection::getWeight)
        .mapToInt(Double::intValue)
        .sum();

    System.out.println("\nConnected by: ");
    verticesPaths.forEach(System.out::println);
    System.out.println("\nEdges sum: " + graphEdgesSum);
    System.out.println("Additional edges sum: " + additionalEdgesSum);
    System.out.println("Total weight: " + (additionalEdgesSum + graphEdgesSum));
  }

  private static int getAllGraphWeightsSum() {
    return edges.stream()
        .map(Edge::getWeight)
        .mapToInt(Integer::intValue)
        .sum();
  }

  private static void initialiseVariables(Map<Integer, List<Edge>> graph) {
    graph.forEach((k, v) -> v.forEach(e -> {
      if (!edges.contains(e.getDefaultEdge())) {
        edges.add(e);
      }
      if (graph.get(k).size() % 2 == 1) {
        graphsToCheck.add(Stream.of(k)
            .collect(Collectors.toCollection(HashSet::new)));
      }
    }));
  }
}
