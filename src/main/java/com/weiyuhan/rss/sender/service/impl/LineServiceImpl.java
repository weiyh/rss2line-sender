package com.weiyuhan.rss.sender.service.impl;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.weiyuhan.rss.sender.service.LineService;

@Component
public class LineServiceImpl implements LineService {

    private static final Logger logger = LoggerFactory.getLogger(LineServiceImpl.class);

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Override
    public void replyText(String replyToken, String replyText) {
        if (StringUtils.isEmpty(replyText)) {
            return;
        }

        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }

        reply(replyToken, new TextMessage(replyText));
    }

    @Override
    public void replySticker(String replyToken, String packageId, String stickerId) {
        reply(replyToken, new StickerMessage(packageId, stickerId));
    }

    @Override
    public void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    @Override
    public void reply(String replyToken, List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
            logger.info("Reply messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pushText(String pushLineUid, String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }

        push(pushLineUid, new TextMessage(text));
    }

    @Override
    public void push(String pushLineUid, Message message) {
        push(pushLineUid, Collections.singletonList(message));
    }

    @Override
    public void push(String pushLineUid, List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .pushMessage(new PushMessage(pushLineUid, messages))
                    .get();
            logger.info("Push messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
