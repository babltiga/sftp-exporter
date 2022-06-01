package am.bablsoft.repository;

import am.bablsoft.config.SFTPConfig;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@ApplicationScoped
public class ConfigRepo {
    private static final String PATH_DELIMITER = ",,";

    @ConfigProperty(name = "SFTP_HOST")
    @Getter
    String sftpHost;

    @ConfigProperty(name = "SFTP_USERNAME")
    @Getter
    String sftpUsername;

    @ConfigProperty(name = "SFTP_KEY")
    Optional<String> sftpKey;

    @ConfigProperty(name = "SFTP_KEY_PASS")
    @Getter
    Optional<String> sftpKeyPass;

    @ConfigProperty(name = "SFTP_PASSWORD")
    @Getter
    Optional<String> sftpPassword;

    @ConfigProperty(name = "SFTP_PATHS", defaultValue = "/")
    String sftpPaths;

    @ConfigProperty(name = "SFTP_PORT", defaultValue = "22")
    @Getter
    Integer sftpPort;

    public SFTPConfig getSFTPConfig(){
        return new SFTPConfig(sftpHost, sftpUsername, sftpKey.map(s-> new String(Base64.getDecoder().decode(s))), sftpKeyPass, sftpPassword, getSftpPaths(), sftpPort);
    }
    List<String> getSftpPaths() {
        return Arrays.asList(sftpPaths.split(Pattern.quote(PATH_DELIMITER)));
    }
}
