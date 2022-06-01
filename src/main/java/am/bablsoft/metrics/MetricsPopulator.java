package am.bablsoft.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class MetricsPopulator {

    private static final String SFTP_UP = "sftp_up";
    private static final String SFTP_FREE_SPACE = "sftp_filesystem_free_space_bytes";
    private static final String SFTP_TOTAL_SPACE = "sftp_filesystem_total_space_bytes";
    private static final String SFTP_OBJECTS = "sftp_objects_available";
    private static final String SFTP_OBJECTS_SIZE = "sftp_objects_total_size_bytes";

    @Inject
    MeterRegistry registry;

    Map<String, AtomicLong> gaugeData = new ConcurrentHashMap<>();

    /**
     * Creates the Gauges for the predefined set if metrics and registers them globally.
     * @param sftpMetrics The calculated the metrics received from SFTP
     */
    public void updateRegistry(SFTPMetrics sftpMetrics){
        gaugeData.computeIfAbsent(SFTP_UP, k-> addGauge(k, null)).set(sftpMetrics.systemUp() ? 1 : 0);

        // free space
        for(var entry : sftpMetrics.freeSpace().entrySet()){
            gaugeData.computeIfAbsent(SFTP_FREE_SPACE + entry.getKey(), k-> addGauge(SFTP_FREE_SPACE, entry.getKey())).set(entry.getValue());
        }

        // total space
        for(var entry : sftpMetrics.totalSpace().entrySet()){
            gaugeData.computeIfAbsent(SFTP_TOTAL_SPACE + entry.getKey(), k-> addGauge(SFTP_TOTAL_SPACE, entry.getKey())).set(entry.getValue());
        }

        // sftp objects
        for(var entry : sftpMetrics.objectCount().entrySet()){
            gaugeData.computeIfAbsent(SFTP_OBJECTS + entry.getKey(), k-> addGauge(SFTP_OBJECTS, entry.getKey())).set(entry.getValue());
        }

        // sftp objects
        for(var entry : sftpMetrics.objectSize().entrySet()){
            gaugeData.computeIfAbsent(SFTP_OBJECTS_SIZE + entry.getKey(), k-> addGauge(SFTP_OBJECTS_SIZE, entry.getKey())).set(entry.getValue());
        }

    }

    /**
     * Creates the gauge for the given data and sets the default value.
     * @param key The key for the metrics.
     * @param path The "path" Tag for the metric.
     * @return The reference to the value of the gauge.
     */
    AtomicLong addGauge(String key, String path){
        return registry.gaugeCollectionSize(key, Objects.isNull(path) ? Tags.empty() : Tags.of("path", path), new AtomicLong(0));
    }

}
