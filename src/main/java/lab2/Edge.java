package lab2;

import lombok.*;

@Data
@AllArgsConstructor
public class Edge {
    private int v1;
    private int v2;
    private int weight;

    public Edge getDefaultEdge(){
     return new Edge(v2, v1, weight);
    }
}
