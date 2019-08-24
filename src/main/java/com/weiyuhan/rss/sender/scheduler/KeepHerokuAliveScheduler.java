package com.weiyuhan.rss.sender.scheduler;

import com.weiyuhan.rss.sender.service.LineService;
import com.weiyuhan.rss.sender.util.DateUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class KeepHerokuAliveScheduler {

    private static final Logger logger = LoggerFactory.getLogger(KeepHerokuAliveScheduler.class);

    private static final String HEROKU_APP_URL = "https://rss-line-sender.herokuapp.com";
    private static final String UUID = "Uadd579112ca5db174262efcd24a8143f";

    private HttpClient httpClient = HttpClients.createDefault();

    @Autowired
    private LineService lineService;

    @Scheduled(cron = "${cron.keep.heroku.alive}")
    public void keepHerokuAlive() throws IOException {
        HttpGet get = new HttpGet(HEROKU_APP_URL);
        get.setConfig(RequestConfig.custom().setConnectTimeout(20000).setConnectionRequestTimeout(20000).setSocketTimeout(20000).build());
        HttpResponse response = httpClient.execute(get);
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        logger.info(body);
        lineService.pushText(UUID, "heroku.alive: " + DateUtil.formatDateToString(new Date()));
    }
}
