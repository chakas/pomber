package com.chakas;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import sun.net.www.http.HttpClient;

import java.util.Properties;

class DepWithTime {
    String groupId;
    String version;
    String artifactId;
    String timeStamp;

    @Override
    public String toString() {
        return "DepWithTime{" +
                "groupId='" + groupId + '\'' +
                ", version='" + version + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}

@Mojo(name="search",defaultPhase = LifecyclePhase.INITIALIZE)
public class MavenMojo extends AbstractMojo {

    @Parameter(property = "project",readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException, MojoFailureException {
        project.getDependencies()
                .parallelStream()
                .forEach(dependency -> {
                    getLog().info(String.format("https://search.maven.org/solrsearch/select?q=g:\"%s\"+AND+a:\"%s\"+AND+v:\"%s\"&core=gav&rows=20&wt=json",
                            dependency.getGroupId(),
                            dependency.getArtifactId(),
                            dependency.getVersion()
                    ));
                });
    }
}
