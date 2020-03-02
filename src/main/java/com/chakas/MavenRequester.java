package com.chakas;

import org.apache.maven.plugin.logging.Log;

public interface MavenRequester {
    DepWithTime request(Log log, DepWithTime depWithTime);
}
