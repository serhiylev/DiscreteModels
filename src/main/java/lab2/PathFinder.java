package lab2;

import es.usc.citius.hipster.algorithm.Hipster;
import es.usc.citius.hipster.graph.GraphBuilder;
import es.usc.citius.hipster.graph.GraphSearchProblem;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

@AllArgsConstructor
public class PathFinder {

  private final Set<Edge> edges;

  protected Connection getWay(Set<Integer> graph1, Queue<Set<Integer>> setOfGraphs) {
    List<Connection> weightsList = new ArrayList<>();
    for (Set<Integer> innerSet : setOfGraphs) {
      weightsList.add(getWay(graph1, innerSet));
    }
    return weightsList.stream()
        .min(Comparator.comparing(Connection::getWeight))
        .orElse(null);
  }

  protected Connection getWay(Set<Integer> graph1, Set<Integer> graph2) {
    List<Connection> weightsList = new ArrayList<>();
    for (Integer vertexG1 : graph1) {
      for (Integer vertexG2 : graph2) {
        weightsList.add(getWay(vertexG1, vertexG2));
      }
    }
    return weightsList.stream()
        .min(Comparator.comparing(Connection::getWeight))
        .orElse(null);
  }

  @SneakyThrows
  protected Connection getWay(int startVertex, int endVertex) {
    GraphBuilder<Integer, Integer> graphBuilder = GraphBuilder.create();
    for (Edge ed : edges) {
      graphBuilder.connect(ed.getV1())
          .to(ed.getV2())
          .withEdge(ed.getWeight());
    }
    var graph = graphBuilder.createUndirectedGraph();
    var components = GraphSearchProblem
        .startingFrom(startVertex)
        .in(graph)
        .takeCostsFromEdges()
        .build();
    var res = Hipster.createDijkstra(components).search(endVertex);
    var field = res.getGoalNode().getClass().getDeclaredField("cost");
    field.setAccessible(true);
    var totalWeight = (Double) field.get(res.getGoalNode());
    return new Connection(startVertex, endVertex, totalWeight, res.getOptimalPaths().toString());
  }
}
