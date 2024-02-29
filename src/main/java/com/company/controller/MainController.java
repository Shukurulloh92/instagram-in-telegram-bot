package com.company.controller;

import com.company.Model.*;
import com.company.bot.InlineKeyboard;
import com.company.bot.ReplyKeyboard;
import com.company.service.*;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainController {

    static HashMap<User, Status> userStatusMap = new HashMap<>();
    static HashMap<Long, Integer> sendPhotoId = new HashMap<>();

    public static void handleMessage(User user, Message message) {
        Status userStatus = userStatusMap.get(user);

        if (message.hasText()) {
            MainController.handleText(user, message, userStatus);

        } else if (userStatus != null) {
            if (message.hasPhoto() && userStatus.equals(Status.ADD_MEDIA)) {
                MainController.handlePhoto(user, message);

            } else if (message.hasPhoto() && (userStatus.equals(Status.CHANGE_MAIN_PHOTO))) {
                MainController.handleAvatarPhoto(user, message);

            } else if (message.hasVideo() && userStatus.equals(Status.ADD_MEDIA)) {
                MainController.handleVideo(user, message);

            } else if (message.hasAnimation() && userStatus.equals(Status.ADD_MEDIA)) {
                MainController.handleAnimation(user, message);

            } else if (message.hasDocument() && userStatus.equals(Status.ADD_MEDIA)) {
                MainController.handleDocument(user, message);

            } else userStatusMap.remove(user);
        }
    }

    private static void handleAnimation(User user, Message message) {
        PhotosService photosService = new PhotosService();
        Photos photos = new Photos();

        photos.setImageUrl(message.getAnimation().getFileId());
        photos.setUser_id(message.getChatId());
        photosService.add(photos);

        userStatusMap.remove(user);
        System.out.println("Animation added: " + photos);
        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Animation added");
    }

    private static void handleVideo(User user, Message message) {
        PhotosService photosService = new PhotosService();
        Photos photos = new Photos();

        photos.setImageUrl(message.getVideo().getFileId());
        photos.setUser_id(message.getChatId());
        photosService.add(photos);

        userStatusMap.remove(user);
        System.out.println("Video added: " + photos);
        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Video added");
    }

    private static void handleDocument(User user, Message message) {
        PhotosService photosService = new PhotosService();
        Photos photos = new Photos();

        photos.setImageUrl(message.getDocument().getFileId());
        photos.setUser_id(message.getChatId());
        photosService.add(photos);
//        System.out.println(message.getDocument());

        userStatusMap.remove(user);
        System.out.println("Document added: " + photos);
        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Document added");
    }


    private static void handlePhoto(User user, Message message) {
        PhotosService photosService = new PhotosService();
        Photos photos = new Photos();

        System.out.println(message.getPhoto());
        photos.setImageUrl(message.getPhoto().get(
                message.getPhoto().size() - 1).getFileId());
        photos.setUser_id(message.getChatId());

        photosService.add(photos);

        userStatusMap.remove(user);
        System.out.println("Photo added: " + photos);
        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Photo added");
    }

    private static void handleAvatarPhoto(User user, Message message) {
        UserService userService = new UserService();

        user.setAvatarUrl(message.getPhoto().get(
                message.getPhoto().size() - 1).getFileId());

        userService.set(user);

        userStatusMap.remove(user);
        System.out.println("Changed Avatar Photo: " + message.getPhoto());
        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Main photo changed!");
    }


    private static void handleText(User user, Message message, Status userStatus) {
        UserService userService = new UserService();
        InlineKeyboardMarkup inlineKeyboardMarkup;
        String text = message.getText();
        ComponentContainer.TELEGRAM_BOT.deleteMsg(message);

        switch (text) {
            case "/start":
                userStatusMap.remove(user);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Welcome " + message.getFrom().getFirstName(),
                        ReplyKeyboard.menu());
                break;

            case "\uD83C\uDFE0️️":  // Home
                userStatusMap.remove(user);
                MainController.sendMedia(message, null);
                break;

            case "\uD83D\uDD0E️":   // Search
                userStatusMap.remove(user);
                List<User> userList = new UserService().getList(null);
                inlineKeyboardMarkup = InlineKeyboard.getUserList(userList);

                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Users: ", inlineKeyboardMarkup);
                break;

            case "\uD83D\uDE4D️️":  // My Profile
                userStatusMap.remove(user);
                List<Photos> photoList = new PhotosService().getList(user.getId().toString());
                int countFollower = new FollowsServise().getFollowerCount(user.getId());
                int countFollowee = new FollowsServise().getFolloweeCount(user.getId());
                int countPhoto = 0;
                if (!photoList.isEmpty())
                    countPhoto = photoList.size();

                inlineKeyboardMarkup = InlineKeyboard.getProfil(countPhoto, countFollower, countFollowee);
                SendPhoto sendPhoto = new SendPhoto(message.getChatId().toString(),
                        new InputFile().setMedia(user.getAvatarUrl()));
                sendPhoto.setCaption("Username: " + user.getUsername() + "\nName: " + user.getFullName());
                sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
                ComponentContainer.TELEGRAM_BOT.sendMsg(sendPhoto);
                break;


            case "️️➕":    // Add Media
                userStatusMap.put(user, Status.ADD_MEDIA);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Send Photo, Video or Animation");

                break;


            case "Log out️️":
                user.set_log_out(true);
                userService.set(user);

                ReplyKeyboardMarkup markup = ReplyKeyboard.getMarkup(ReplyKeyboard.getRowList(
//                        ReplyKeyboard.getRow(ReplyKeyboard.getButton("Create Account")),
                        ReplyKeyboard.getRow(ReplyKeyboard.getButton("Login"))));
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Log out!", markup);
                break;

            default:
                if (userStatus != null) {
                    if (userStatus.equals(Status.CHANGE_USERNAME)) {

                        String username = userService.checkUsername(message.getText());
                        System.out.println("username = " + username);
                        if (username == null) {
                            if (message.hasText()) {
                                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Username changed!");
                                user.setUsername(message.getText());
                                userService.set(user);
                            }
                        } else {
                            ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                                    "This username is already taken \nEnter new username");
                        }

                    } else if (userStatus.equals(Status.CHANGE_PASSWORD)) {
                        user.setPassword(message.getText());
                        userService.set(user);
                        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Password changed!");

                    } else if (userStatus.equals(Status.ADD_COMMENT)) {
                        Comments comments = new Comments();
                        comments.setCommentText(message.getText());
                        comments.setUser_id(message.getChatId());
                        comments.setPhoto_id(sendPhotoId.get(message.getChatId()));

                        CommentService commentService = new CommentService();
                        commentService.add(comments);

                        ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Successfully comment added!");
                        sendMedia(message, sendPhotoId.get(message.getChatId()));
                        userStatusMap.remove(user);

                    } else if (userStatus.equals(Status.DELETE_ACCOUNT)) {
                        String username = userService.checkPassword(message.getChatId(), message.getText());
                        System.out.println("username = " + username);
                        if (username != null) {
                            if (message.hasText()) {
                                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                                        "Are you sure you want to delete your account",
                                        InlineKeyboard.YesNoButton());
                            }
                        } else {
                            ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                                    "Password mismatch \nPlease enter password");
                        }
                    } else userStatusMap.remove(user);
                    break;
                }
        }
    }

    public static void sendMedia(Message message, Integer photoId) {
        List<User> userList = new UserService().getList(null);
        Photos photos;

        if (photoId == null) {
            List<Photos> photoList = new ArrayList<>(new PhotosService().getList(null));
            photoId = (int) (Math.random() * (photoList.size()));
            photos = photoList.get(photoId);
        } else {
            photos = new PhotosService().get(photoId.toString());
        }
        sendPhotoId.put(message.getChatId(), photos.getId());

        int like = new LikeServise().getLike(photos.getId());
        List<Comments> comments = new CommentService().getList(String.valueOf(photos.getId()));

        String fullname = "non name";
        for (User user1 : userList) {
            if (user1.getId().equals(photos.getUser_id())) {
                fullname = user1.getFullName();
                break;
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboard.getCaption();

        if (photos.getImageUrl().startsWith("BAACA")) {

            SendVideo sendVideo = new SendVideo();
            sendVideo.setChatId(message.getChatId().toString());
            sendVideo.setCaption("Created on " + photos.getCreatedAt().toString() + " by " +
                    fullname + "\n❤️ Likes: " + like + "    \uD83D\uDCAC Comments: " + comments.size());
            sendVideo.setReplyMarkup(inlineKeyboardMarkup);
            sendVideo.setVideo(new InputFile().setMedia(photos.getImageUrl()));
            ComponentContainer.TELEGRAM_BOT.sendMsg(sendVideo);

            System.out.println("Sent Video id: " + photos.getId());
        } else if (photos.getImageUrl().startsWith("BQACA") | photos.getImageUrl().startsWith("CgACA")) {

            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(message.getChatId().toString());
            sendDocument.setCaption("Created on " + photos.getCreatedAt().toString() + " by " +
                    fullname + "\n❤️ Likes: " + like + "    \uD83D\uDCAC Comments: " + comments.size());
            sendDocument.setReplyMarkup(inlineKeyboardMarkup);
            sendDocument.setDocument(new InputFile().setMedia(photos.getImageUrl()));
            ComponentContainer.TELEGRAM_BOT.sendMsg(sendDocument);

            System.out.println("Sent Document id: " + photos.getId());
        } else if (photos.getImageUrl().startsWith("CgACA") | photos.getImageUrl().startsWith("CgACA")) {

            SendAnimation sendAnimation = new SendAnimation();
            sendAnimation.setChatId(message.getChatId().toString());
            sendAnimation.setCaption("Created on " + photos.getCreatedAt().toString() + " by " +
                    fullname + "\n❤️ Likes: " + like + "    \uD83D\uDCAC Comments: " + comments.size());
            sendAnimation.setReplyMarkup(inlineKeyboardMarkup);
            sendAnimation.setAnimation(new InputFile().setMedia(photos.getImageUrl()));
            ComponentContainer.TELEGRAM_BOT.sendMsg(sendAnimation);

            System.out.println("Sent Animation id: " + photos.getId());
        } else {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setCaption("Created on " + photos.getCreatedAt().toString() + " by " +
                    fullname + "\n❤️ Likes: " + like + "    \uD83D\uDCAC Comments: " + comments.size());
            sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
            sendPhoto.setPhoto(new InputFile().setMedia(photos.getImageUrl()));
            ComponentContainer.TELEGRAM_BOT.sendMsg(sendPhoto);

            System.out.println("Sent Image id: " + photos.getId());
        }
    }


    public static void editMedia(Message message, Integer photoId) {
        List<User> userList = new UserService().getList(null);
        Photos photos = new PhotosService().get(photoId.toString());

        int like = new LikeServise().getLike(photos.getId());
        List<Comments> comments = new CommentService().getList(String.valueOf(photos.getId()));

        String fullname = "non name";
        for (User user1 : userList) {
            if (user1.getId().equals(photos.getUser_id())) {
                fullname = user1.getFullName();
                break;
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboard.getCaption();

            EditMessageCaption editMedia = new EditMessageCaption();
            editMedia.setChatId(message.getChatId().toString());
            editMedia.setCaption("Created on " + photos.getCreatedAt().toString() + " by " +
                    fullname + "\n❤️ Likes: " + like + "    \uD83D\uDCAC Comments: " + comments.size());
            editMedia.setReplyMarkup(inlineKeyboardMarkup);
            editMedia.setMessageId(message.getMessageId());
            ComponentContainer.TELEGRAM_BOT.sendMsg(editMedia);

            System.out.println("Sent Image id: " + photos.getId());

    }


    public static void handleCallBack(User user, CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();
        String data = callbackQuery.getData();
        deleteMessage.setChatId(chatId);
        userStatusMap.remove(user);

        System.out.println(user.getFullName() + ": " + data);


        switch (data) {
            case "next":
                ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
                sendMedia(message, null);
                break;

            case "comment":
                List<Comments> comments = new CommentService()
                        .getList(String.valueOf(sendPhotoId.get(message.getChatId())));
                System.out.println("len=" + sendPhotoId.get(message.getChatId()) + " " + comments);

                if (comments.size() > 3) {
                    for (int i = comments.size() - 1; i > comments.size() - 4; i--) {
                        List<User> userList = new UserService().getList(String.valueOf(comments.get(i).getUser_id()));
                        ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                                " " + userList.get(0).getFullName() + ":\n" +
                                        comments.get(i).getCommentText() + "\n" +
                                        "Created date: " + comments.get(i).getCreatedAt());
                    }
                } else if (comments.isEmpty()) {
                    ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                            "Comment is not found");
                } else {
                    for (Comments comment : comments) {
                        List<User> userList = new UserService().getList(String.valueOf(comment.getUser_id()));
                        ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                                " " + userList.get(0).getFullName() + ":\n" +
                                        comment.getCommentText() + "\n" +
                                        "Created date: " + comment.getCreatedAt());

                    }
                }
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Enter new comments: ");
                userStatusMap.put(user, Status.ADD_COMMENT);
                break;

            case "like":
                Integer photoId = sendPhotoId.get(message.getChatId());
                Like like = new Like(message.getChatId(), photoId);
                new LikeServise().add(like);
                editMedia(message, photoId);
                break;

            case "nextUser":
                System.out.println("nextUser");
                break;

            case "Edite Profile":
            case "no":
                ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Edite Profile", InlineKeyboard.getEditProfile());
                break;

            case "change_username":
                ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Enter new username: ");
                userStatusMap.put(user, Status.CHANGE_USERNAME);
                break;

            case "Change password":
                ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Enter new password: ");
                userStatusMap.put(user, Status.CHANGE_PASSWORD);
                break;

            case "Change main photo":
                ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Send new photo: ");
                userStatusMap.put(user, Status.CHANGE_MAIN_PHOTO);
                break;

            case "Delete account":
                ComponentContainer.TELEGRAM_BOT.deleteMsg(message);
                ComponentContainer.TELEGRAM_BOT.sendMsg(message, "Please enter password");
                userStatusMap.put(user, Status.DELETE_ACCOUNT);
                break;

            case "Yes delete account":
                UserService userService = new UserService();
                userService.deleteAccount(user.getId());

                ReplyKeyboardMarkup markup = ReplyKeyboard.getMarkup(ReplyKeyboard.getRowList(
                        ReplyKeyboard.getRow(ReplyKeyboard.getButton("Create Account"))
//                        , ReplyKeyboard.getRow(ReplyKeyboard.getButton("Login"))
                ));
                ComponentContainer.TELEGRAM_BOT.sendMsg(message,
                        "Account deleted!", markup);
                break;

            default:
                System.out.println("default:");
                break;
        }


    }
}
