package com.weiyuhan.rss.sender.service;

import com.linecorp.bot.model.message.Message;

import java.util.List;

public interface LineService {

    void replyText(String replyToken, String text);

    void reply(String replyToken, Message message);

    void reply(String replyToken, List<Message> messages);


    void pushText(String pushLineUid, String text);

    void push(String pushLineUid, Message message);

    void push(String pushLineUid, List<Message> messages);
}
