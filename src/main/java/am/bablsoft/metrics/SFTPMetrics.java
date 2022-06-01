package am.bablsoft.metrics;

import java.util.Map;

public record SFTPMetrics(boolean systemUp, Map<String, Long> totalSpace, Map<String, Long> freeSpace, Map<String, Long> objectCount, Map<String, Long> objectSize) {
}
