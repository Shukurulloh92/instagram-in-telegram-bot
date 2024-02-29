package com.company;

import com.company.Model.User;
import com.company.controller.MainController;
import com.company.controller.Reception;
import com.company.service.UserService;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.*;

import static com.company.common.Constants.BOT_TOKEN;
import static com.company.common.Constants.BOT_USERNAME;

public class Bot extends TelegramLongPollingBot {

    public static HashMap<String, ArrayList<Integer>> deleteListList = new HashMap<>();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            List<User> userList = new UserService().getList(message.getChatId().toString());

            System.out.println("userList = " + userList + userList.isEmpty());

            if (!userList.isEmpty() && !userList.get(0).is_log_out())
                MainController.handleMessage(userList.get(0), message);

            else
                Reception.reception(message);

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            List<User> userList = new UserService().getList(callbackQuery.getMessage().getChatId().toString());

            System.out.println("userList = " + userList + userList.isEmpty());

            if (!userList.isEmpty() && !userList.get(0).is_log_out())
                MainController.handleCallBack(userList.get(0), callbackQuery);

            else
                sendMsg(callbackQuery.getMessage(), "Please enter your login username");

        }
    }


    @SneakyThrows
    public void sendMsg(SendMessage sendMessage) {
        addToDelete(execute(sendMessage).getMessageId(), sendMessage.getChatId());
    }

    @SneakyThrows
    public void sendMsg(SendPhoto sendPhoto) {
        addToDelete(execute(sendPhoto).getMessageId(), sendPhoto.getChatId());
    }

    @SneakyThrows
    public void sendMsg(SendDocument sendDocument) {
        addToDelete(execute(sendDocument).getMessageId(), sendDocument.getChatId());
    }

    @SneakyThrows
    public void sendMsg(SendVideo sendVideo) {
        addToDelete(execute(sendVideo).getMessageId(), sendVideo.getChatId());
    }

    @SneakyThrows
    public void sendMsg(SendAnimation sendAnimation) {
        addToDelete(execute(sendAnimation).getMessageId(), sendAnimation.getChatId());
    }

    @SneakyThrows
    public void sendMsg(EditMessageCaption editMedia) {
        execute(editMedia);
    }


    @SneakyThrows
    public void sendMsg(Message message, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        addToDelete(
                execute(SendMessage.builder()
                        .text(text)
                        .chatId(message.getChatId().toString())
                        .replyMarkup(inlineKeyboardMarkup)
                        .build()).getMessageId(),
                message.getChatId().toString());
    }

    @SneakyThrows
    public void sendMsg(Message message, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        execute(SendMessage.builder()
                .text(text)
                .chatId(message.getChatId().toString())
                .replyMarkup(replyKeyboardMarkup)
                .build()).getMessageId();
    }

    @SneakyThrows
    public void sendMsg(Message message, String text) {
        execute(SendMessage.builder()
                .text(text)
                .chatId(message.getChatId().toString())
                .build()).getMessageId();
    }


    @SneakyThrows
    public void deleteMsg(Message message) {
        ArrayList<Integer> integers = deleteListList.get(message.getChatId().toString());
        if (integers != null) {
            DeleteMessage deleteMessage = new DeleteMessage(message.getChatId().toString(), integers.get(0));
            execute(deleteMessage);

            for (int i = 1; i < integers.size(); i++) {
                if (!Objects.equals(integers.get(i - 1), integers.get(i))) {
                    deleteMessage = new DeleteMessage(message.getChatId().toString(), integers.get(i));
                    execute(deleteMessage);
                }
            }
            deleteListList.remove(message.getChatId().toString());
        }
    }


    @SneakyThrows
    private void addToDelete(Integer messageId, String chatId) {
        if (!deleteListList.isEmpty() && deleteListList.containsKey(chatId)) {
            deleteListList.get(chatId).add(messageId);
        } else {
            deleteListList.put(chatId, new ArrayList<>(Collections.singletonList(messageId)));
        }
    }

}
