package am.bablsoft.schedule;

import am.bablsoft.metrics.MetricsPopulator;
import am.bablsoft.repository.ConfigRepo;
import am.bablsoft.sftp.SFTPClient;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SFTPScheduler {

    @Inject
    ConfigRepo configRepo;

    @Inject
    SFTPClient sftpClient;

    @Inject
    MetricsPopulator metricsPopulator;

    @Scheduled(every="10s")
    void calculateMetrics() {
        var sftpConfig = configRepo.getSFTPConfig();
        var metrics = sftpClient.getSFTPMetrics(sftpConfig);
        metricsPopulator.updateRegistry(metrics);
    }
}
