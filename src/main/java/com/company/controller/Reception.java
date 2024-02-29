package com.company.controller;

import com.company.Model.Status;
import com.company.Model.User;
import com.company.bot.ReplyKeyboard;
import com.company.service.UserService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Reception {

    static HashMap<Long, User> newUser = new HashMap<>();

    public static void reception(Message message) {
        User user1 = newUser.get(message.getChatId());

        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                loginMenu(message);

            } else if (message.getText().equals("Create Account")) {
                newUser.remove(message.getChatId());
                Reception.register(message);

            } else if (message.getText().equals("Login")) {
                newUser.remove(message.getChatId());
                Reception.login(message);

            } else if (user1 != null) {
                if (Objects.equals(user1.getStatus(), Status.FULL_NAME.name())
                        || Objects.equals(user1.getStatus(), Status.PASSWORD.name())
                        || Objects.equals(user1.getStatus(), Status.USERNAME.name())
                        || Objects.equals(user1.getStatus(), Status.AVATAR_URL.name()))
                    Reception.register(message);
                else if (Objects.equals(user1.getStatus(), Status.LOGIN.name())
                        || Objects.equals(user1.getStatus(), Status.CHECK_PASSWORD.name()))
                    Reception.login(message);
            } else {
                loginMenu(message);
            }
        } else if (message.hasPhoto()) {
            if (user1.getStatus().equals(Status.AVATAR_URL.name())) {
                user1.setAvatarUrl(message.getPhoto().get(
                        message.getPhoto().size() - 1).getFileId());

                newUser.remove(message.getChatId());
                UserService userService = new UserService();
                userService.add(user1);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Successfully registered!", ReplyKeyboard.menu());
                newUser.remove(message.getChatId());
            }
        } else {
            loginMenu(message);
        }

    }

    private static void loginMenu(Message message) {
        List<User> userList = new UserService().getList(message.getChatId().toString());
        newUser.remove(message.getChatId());
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        sendPhoto.setPhoto(new InputFile(new File(String.valueOf(ComponentContainer.InstaFile))));
        sendPhoto.setCaption("<b>WELCOME TO INSTAGRAM</b>");
        sendPhoto.setParseMode("HTML");
        ComponentContainer.TELEGRAM_BOT.sendMsg(sendPhoto);

        ReplyKeyboardMarkup markup;
        if (userList.isEmpty()) {
            markup = ReplyKeyboard.getMarkup(ReplyKeyboard.getRowList(
                    ReplyKeyboard.getRow(ReplyKeyboard.getButton("Create Account"))));
        }else {
            markup = ReplyKeyboard.getMarkup(ReplyKeyboard.getRowList(
                    ReplyKeyboard.getRow(ReplyKeyboard.getButton("Login"))));
        }


        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("from 🚀<b><i>G6 Meta</i></b>");
        sendMessage.setParseMode("HTML");
        sendMessage.setChatId(String.valueOf(message.getChatId()));
        sendMessage.setReplyMarkup(markup);
        ComponentContainer.TELEGRAM_BOT.sendMsg(sendMessage);
    }


    private static void login(Message message) {
        User user1 = newUser.get(message.getChatId());


        if (newUser.get(message.getChatId()) == null) {
            List<User> userList = new UserService().getList(message.getChatId().toString());
            System.out.println(userList);
            if (!userList.isEmpty()) {
                user1 = userList.get(0);
            }

            if (user1 == null) {
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "You don not have an account yet");
                return;
            }

            user1.setStatus(Status.LOGIN.name());
            user1.setId(message.getChatId());
            newUser.put(message.getChatId(), user1);

            ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Please enter your login username");
        } else if (user1.getStatus().equals(Status.LOGIN.name())) {
            UserService userService = new UserService();
            String username = userService.checkUsername(message.getText());
            System.out.println(username);
            if (username != null) {
                if (user1.getStatus().equals(Status.LOGIN.name())
                        && message.hasText()) {
                    user1.setUsername(message.getText());
                    user1.setStatus(Status.CHECK_PASSWORD.name());
                    newUser.put(message.getChatId(), user1);
                    ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                            "Enter password: ");
                }
            } else {
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Wrong username!\nEnter username: ");

            }

        } else if (user1.getStatus().equals(Status.CHECK_PASSWORD.name())) {
            UserService userService = new UserService();
            String username = userService.checkPassword(message.getChatId(), message.getText());

            if (username != null) {
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Successfully logined!", ReplyKeyboard.menu());
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("<b><i>Instagram Page</i></b>");
                sendMessage.setParseMode("HTML");
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                ComponentContainer.TELEGRAM_BOT.sendMsg(sendMessage);
                newUser.remove(message.getChatId());
                MainController.sendMedia(message, null);

                if (user1.getStatus().equals(Status.CHECK_PASSWORD.name())
                        && message.hasText()) {
                    user1.setPassword(message.getText());
                    user1.set_log_out(false);
                    user1.setStatus(Status.SUCCESSFUL.name());
                    userService.set(user1);
                    newUser.put(message.getChatId(), user1);
                }
            } else {
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Wrong password!\nEnter username: ");
                user1.setStatus(Status.LOGIN.name());

            }
        }

    }


    private static void register(Message message) {
        User user = newUser.get(message.getChatId());

        if (user != null && (Objects.equals(user.getStatus(), Status.FULL_NAME.name())
                || Objects.equals(user.getStatus(), Status.PASSWORD.name())
                || Objects.equals(user.getStatus(), Status.USERNAME.name())
                || Objects.equals(user.getStatus(), Status.AVATAR_URL.name()))) {

            if (user.getStatus().equals(Status.FULL_NAME.name())) {
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Enter username");
                if (user.getStatus().equals(Status.FULL_NAME.name())
                        && message.hasText()) {
                    user.setFullName(message.getText());
                    user.setStatus(Status.USERNAME.name());
                    newUser.put(message.getChatId(), user);
                }
            } else if (user.getStatus().equals(Status.USERNAME.name())) {
                UserService userService = new UserService();
                String username = userService.checkUsername(message.getText());

                if (username == null) {
                    ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                            "Enter password");
                    if (user.getStatus().equals(Status.USERNAME.name())
                            && message.hasText()) {
                        user.setUsername(message.getText());
                        user.setStatus(Status.PASSWORD.name());
                        newUser.put(message.getChatId(), user);
                    }
                } else {
                    ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                            "This username is already taken" +
                                    "\nEnter username");

                }
            } else if (user.getStatus().equals(Status.PASSWORD.name())) {
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Send photo (not necessary)\nOr enter any text");
                if (user.getStatus().equals(Status.PASSWORD.name())
                        && message.hasText()) {
                    user.setPassword(message.getText());
                    user.setStatus(Status.AVATAR_URL.name());
                    newUser.put(message.getChatId(), user);
                }
            } else if (user.getStatus().equals(Status.AVATAR_URL.name())
                    && message.hasText()) {
                user.setAvatarUrl("https://www.advancience.io/wp-content/uploads/2020/02/placeholderorund-1.jpg");
                user.setStatus(Status.SUCCESSFUL.name());
                newUser.put(message.getChatId(), user);
                UserService userService = new UserService();
                userService.add(user);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Successfully registered!", ReplyKeyboard.menu());
                newUser.remove(message.getChatId());
            }
        } else {
            User user1 = new User();
            user1.setStatus(Status.FULL_NAME.name());
            user1.set_log_out(false);
            user1.setId(message.getChatId());

            newUser.put(message.getChatId(), user1);
            System.out.println(newUser.get(message.getChatId()));

            ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                    "Please enter your full name to register");
        }
        ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
    }
}
