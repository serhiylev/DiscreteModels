package lab2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Connection {
    private Integer from;
    private Integer to;
    private Double weight;
    private String path;
}
