# pomber
Pomber is a maven, which searches all the active dependencies and print the information

## How to use

`mvn com.chakas:pomber:search`

which outputs

<pre>
       <b>GroupId</b>                          <b>ArtifactId</b>                                <b>Version</b>         <b>Timestamp</b>
[INFO] com.squareup.okhttp3            |okhttp                                   |4.4.0          |2020-02-17 23:43:49.0
[INFO] org.apache.maven.plugin-tools   |maven-plugin-annotations                 |3.6.0          |2018-10-29 01:45:33.0
[INFO] org.apache.maven                |maven-core                               |3.6.3          |2019-11-19 20:33:57.0
[INFO] org.apache.maven                |maven-plugin-api                         |3.6.3          |2019-11-19 20:30:03.0
</pre>    


`mvn com.chakas:pomber:search -Dts=2y`

Ts - Timestamp filter which will check which are older than 2year

`
 y - year
 m - month
 d - day
`


## Roadmap
- Stop the build if it fails any of the condition
- Output as JSON response
