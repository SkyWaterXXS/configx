package com.github.skywaterxxs.configx.client.domain;

import java.util.UUID;

/**
 * <p>ClassName:com.github.skywaterxxs.configx.client.domain.Chat</p>
 * <p>描述:  </p>
 * <p>日期: 2018/11/28 </p>
 *
 * @author xiaoshuo.xxs
 * @version 1.0.0
 * @since 1.0.0
 */
public class Chat {
    private String chatId;

    private String command;

    private Object commandContent;

    public Chat(){}

    public Chat(String chatId, String command, Object commandContent) {
        this.chatId = chatId;
        this.command = command;
        this.commandContent = commandContent;
    }

    public Chat(String command, Object commandContent) {
        this.chatId = UUID.randomUUID().toString();
        this.command = command;
        this.commandContent = commandContent;
    }

    public Chat(String command) {
        this.chatId = UUID.randomUUID().toString();
        this.command = command;
    }

    public Chat(String chatId, String command) {
        this.chatId = chatId;
        this.command = command;
    }


    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Object getCommandContent() {
        return commandContent;
    }

    public void setCommandContent(Object commandContent) {
        this.commandContent = commandContent;
    }
}
