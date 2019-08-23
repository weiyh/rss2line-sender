package com.weiyuhan.rss.sender.controller;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.weiyuhan.rss.sender.service.LineService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class LineEventController {

    private static final Logger logger = LoggerFactory.getLogger(LineEventController.class);

    @Autowired
    private LineService lineService;

    @EventMapping
    public void listenTextMessage(MessageEvent<TextMessageContent> event) {
        String replyToken = event.getReplyToken();
        TextMessageContent message = event.getMessage();
        String text = message.getText();
        logger.info("Got text message from replyToken:{}: text:{}", replyToken, text);

        switch (text) {
            case "hi":
                lineService.replyText(replyToken, "Hi!");
                break;
        }
    }

    @EventMapping
    public void listenStickerMessage(MessageEvent<StickerMessageContent> event) {
        lineService.replySticker(event.getReplyToken(), "2", "179");
    }
}
