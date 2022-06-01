package am.bablsoft.config;

import java.util.List;
import java.util.Optional;

public record SFTPConfig(String sftpHost, String sftpUsername,
                         Optional<String> sftpKey, Optional<String> sftpKeyPass,
                         Optional<String> sftpPassword, List<String> sftpPaths, Integer sftpPort) {
}
