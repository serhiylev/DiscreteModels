package util;

import lab2.Edge;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

  public static Map<Integer, List<Edge>> getGraphForCustom(String path) {
    var edgesList = getEdgesList(path);
    var edgeList = new ArrayList<Edge>();
    for (int i = 0; i < edgesList.size(); i++) {
      for (int j = 0; j < edgesList.get(i).length; j++) {
        if (edgesList.get(i)[j] != 0) {
          edgeList.add(new Edge(i + 1, j + 1, edgesList.get(i)[j]));
        }
      }
    }
    return edgeList.stream().collect(Collectors.groupingBy(Edge::getV1));
  }

  @SneakyThrows
  private static List<int[]> getEdgesList(String path) {
    return Files.lines(Path.of(path))
        .skip(1)
        .map(e -> e.split(" "))
        .map(e -> Arrays.stream(e)
            .mapToInt(Integer::valueOf)
            .toArray())
        .collect(Collectors.toList());
  }

  @SneakyThrows
  public static String[][] readAsMatrix(String path) {
    return Files.lines(Path.of(path))
        .skip(1)
        .map(e -> e.split(" "))
        .toArray(String[][]::new);
  }
}
