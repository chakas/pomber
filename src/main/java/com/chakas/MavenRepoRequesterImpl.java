package com.chakas;

import okhttp3.*;
import org.apache.maven.plugin.logging.Log;
import org.jetbrains.annotations.NotNull;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Named
@Singleton
public class MavenRepoRequesterImpl implements MavenRequester {
    private final OkHttpClient.Builder builder = new OkHttpClient.Builder();
    private final Pattern pattern = Pattern.compile("\\d{13}");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private final OkHttpClient client;

    public MavenRepoRequesterImpl() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(3);

        Interceptor interceptor = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return chain.proceed(chain.request());
            }
        };

        builder.addNetworkInterceptor(interceptor);
        builder.dispatcher(dispatcher);
        client = builder.build();
    }

    @Override
    public DepWithTime request(Log log, DepWithTime depWithTime) {
        Request request = new Request.Builder()
                .url(
                        String.format("https://search.maven.org/solrsearch/select?q=g:\"%s\"+AND+a:\"%s\"+AND+v:\"%s\"&core=gav&rows=20&wt=json",
                                depWithTime.getGroupId(),
                                depWithTime.getArtifactId(),
                                depWithTime.getVersion()
                        )
                )
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Unable to contact maven central.Please try again : " + request.url().toString());
            }

            String responseString = response.body().string();
            Matcher matcher = pattern.matcher(responseString);
            if (matcher.find()) {
//                    log.info(" Timestamp " + sdf.format(new Date())));
                depWithTime.setTimeStamp(new Timestamp(Long.valueOf(matcher.group(0))));
            } else {
                log.error("Version not found on maven repo");
            }

        } catch (IOException e) {
            log.error("Unable to contact maven central.Please try again");
        }
        return depWithTime;
    }
}
