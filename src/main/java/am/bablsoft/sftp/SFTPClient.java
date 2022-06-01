package am.bablsoft.sftp;

import am.bablsoft.config.SFTPConfig;
import am.bablsoft.metrics.SFTPMetrics;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class SFTPClient {

    public SFTPMetrics getSFTPMetrics(SFTPConfig sftpConfig){
        Session session = null;
        ChannelSftp channel = null;
        try {
            var jsch = new JSch();
            sftpConfig.sftpKey().ifPresent(s->{
                addIdentity(jsch, s, sftpConfig.sftpKeyPass());
            });
            session = jsch.getSession(sftpConfig.sftpUsername(), sftpConfig.sftpHost(), sftpConfig.sftpPort());
            session.setConfig("StrictHostKeyChecking", "no");
            sftpConfig.sftpPassword().ifPresent(session::setPassword);
            session.connect();
            channel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            var totalSpaces = new HashMap<String, Long>();
            var freeSpaces  = new HashMap<String, Long>();
            var objectCounts = new HashMap<String, Long>();
            var objectSizes  = new HashMap<String, Long>();
            for(var path : sftpConfig.sftpPaths()){
                var stats = channel.statVFS(path);
                var objectCount = channel.ls(path).stream().filter(s-> !s.getAttrs().isDir()).count();
                var objectSize = channel.ls(path).stream().filter(s-> !s.getAttrs().isDir()).filter(s->!s.getAttrs().isDir()).mapToLong(s->s.getAttrs().getSize()).sum();
                freeSpaces.computeIfAbsent(path, k -> stats.getAvail());
                totalSpaces.computeIfAbsent(path, k -> stats.getSize());
                objectCounts.putIfAbsent(path, objectCount);
                objectSizes.putIfAbsent(path, objectSize);
            }
            return new SFTPMetrics(true, totalSpaces, freeSpaces, objectCounts, objectSizes);
        } catch (Exception ex) {
            log.warn("Error while connecting to SFTP", ex);
            return new SFTPMetrics(false, Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

    void addIdentity(JSch jsch, String key, Optional<String> pass){
        try {
            jsch.addIdentity(key, pass.orElse(null));
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }


}
