package com.chakas;


import com.google.common.collect.ImmutableSortedMap;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


@Mojo(name = "search", defaultPhase = LifecyclePhase.COMPILE)
public class MavenMojo extends AbstractMojo {

    @Inject
    private MavenRequester mavenRequester;

    @Inject
    private DateFilter filter;

    @Parameter(property = "ts",defaultValue = "")
    private String timefilter;

    @Parameter(property = "project", readonly = true)
    private MavenProject project;
    private Date filterDate;

    public void execute() throws MojoExecutionException, MojoFailureException {

        if(timefilter != null) {
            filterDate = filter.dateFilter(timefilter);
            getLog().info(" Filtering based on the timestamp : " + filterDate.toString());
        }

        Map<String, DepWithTime> depWithTimeMap = project.getDependencies()
                .stream()
                .map(dependency -> new DepWithTime(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()))
                .map(depWithTime -> mavenRequester.request(getLog(), depWithTime))
                .filter(depWithTime -> {
                    if(timefilter == null || timefilter.length() == 0)
                        return true;
                    return depWithTime.timeStamp.after(filterDate);
                })
                .collect(Collectors.toMap(depWithTime -> depWithTime.getGroupId() + ":" + depWithTime.getArtifactId() + ":" + depWithTime.getVersion(), Function.identity(),
                        (v1, v2) -> {
                            throw new RuntimeException(String.format("Duplicate key for values %s and %s", v1, v2));
                        },
                        TreeMap::new));
//                .collect(Collectors.toMap(depWithTime -> depWithTime.getGroupId() + ":" + depWithTime.getArtifactId() + ":" + depWithTime.getVersion(), Function.identity()));

        depWithTimeMap
                .forEach((s, depWithTime) -> {
                    if (depWithTime.getTimeStamp() != null)
                        getLog().info(String.format("%-32s|%-41s|%-15s|%-15s",
                                depWithTime.groupId.substring(0, Math.min(depWithTime.groupId.length(), 30)),
                                depWithTime.artifactId.substring(0, Math.min(depWithTime.artifactId.length(), 40)),
                                depWithTime.version,
                                depWithTime.timeStamp));
                });
    }
}
