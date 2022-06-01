# SFTP Exporter

[Prometheus Exporter](https://prometheus.io/docs/instrumenting/exporters/) for [SFTP](https://www.ssh.com/ssh/sftp/) server based on [Quarkus](https://quarkus.io/).

![Status](https://github.com/babltiga/sftp-exporter/actions/workflows/build-and-publish.yml/badge.svg?branch=master)

![Status](https://github.com/babltiga/sftp-exporter/actions/workflows/codeql-analysis.yml/badge.svg?branch=master)


## Configurations

Configurations can be provided via environment variables.

### Environment Variables

| Environment Variable Name |  Type  | Default Value |                                                         Description |
|---------------------------|:------:|--------------:|--------------------------------------------------------------------:|
| SFTP_HOST                 | string |               |                                         The host of the SFTP server |
| SFTP_PORT                 | string |            22 |                          The port of which SFTP server is listening |
| SFTP_USERNAME             | string |               |                                     The username of the SFTP server |
| SFTP_KEY                  | string |               |            Base64 encoded private key for accessing the SFTP server |
| SFTP_KEY_PASS             | string |               |                                   The password for SFTP private key |
| SFTP_PASSWORD             | string |               | The password for SFTP server in case username/password auth is used |
| SFTP_PATHS                | string |             / |                The double comma separated list of paths to monitor. |


### Running the application

The easiest way to run the exporter is to use official [docker image](https://hub.docker.com/repository/docker/babltiga/sftp-exporter). We use [GraalVM](https://www.graalvm.org
) based native build, so it has a small memory footprint and supersonic startup time.

```sh
$ docker run -e SFTP_HOST=127.0.0.1 -e SFTP_USERNAME=admin -e SFTP_PASSWORD=XXX -e SFTP_PATHS=/home/users/admin -i --rm -p 8080:8080 babltiga/sftp-exporter:latest
```

and navigate to http://localhost:8080/q/metrics

## Metrics
Metrics ara available under the `/q/metrics` path.

```
# HELP sftp_filesystem_free_space_bytes Free space in the filesystem containing the path
# TYPE sftp_filesystem_free_space_bytes gauge
sftp_filesystem_free_space_bytes{path="/upload1"} 7.370901504e+10
sftp_filesystem_free_space_bytes{path="/upload2"} 7.370901504e+10
# HELP sftp_filesystem_total_space_bytes Total space in the filesystem containing the path
# TYPE sftp_filesystem_total_space_bytes gauge
sftp_filesystem_total_space_bytes{path="/upload1"} 8.4281810944e+10
sftp_filesystem_total_space_bytes{path="/upload2"} 8.4281810944e+10
# HELP sftp_objects_available Number of objects in the path
# TYPE sftp_objects_available gauge
sftp_objects_available{path="/upload1"} 1
sftp_objects_available{path="/upload2"} 3
# HELP sftp_objects_total_size_bytes Total size of all the objects in the path
# TYPE sftp_objects_total_size_bytes gauge
sftp_objects_total_size_bytes{path="/upload1"} 312
sftp_objects_total_size_bytes{path="/upload2"} 2337
# HELP sftp_up Tells if exporter is able to connect to SFTP
# TYPE sftp_up gauge
sftp_up 1
```

## Grafana Dashboard

[Grafana Dashoard](https://grafana.com/grafana/dashboards/12828)
