package com.company.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReplyKeyboard {

    public static ReplyKeyboardMarkup menu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        replyKeyboardMarkup.setKeyboard(keyboardRows);

        KeyboardRow keyboardButtons1 = new KeyboardRow();
        KeyboardRow keyboardButtons3 = new KeyboardRow();


        KeyboardButton keyboardButton1 = new KeyboardButton();
        keyboardButton1.setText("\uD83C\uDFE0️️");

        KeyboardButton keyboardButton2 = new KeyboardButton();
        keyboardButton2.setText("\uD83D\uDD0E️");

        KeyboardButton keyboardButton3 = new KeyboardButton();
        keyboardButton3.setText("️️➕");

        KeyboardButton keyboardButton4 = new KeyboardButton();
        keyboardButton4.setText("\uD83D\uDE4D️️");

        KeyboardButton keyboardButton5 = new KeyboardButton();
        keyboardButton5.setText("Log out️️");

        keyboardButtons1.add(keyboardButton1);
        keyboardButtons1.add(keyboardButton2);
        keyboardButtons1.add(keyboardButton3);
        keyboardButtons1.add(keyboardButton4);
        keyboardButtons3.add(keyboardButton5);

        keyboardRows.add(keyboardButtons1);
        keyboardRows.add(keyboardButtons3);

        return replyKeyboardMarkup;
    }


    public static KeyboardButton getButton(String demo) {
        return new KeyboardButton(demo);
    }

    public static KeyboardRow getRow(KeyboardButton... buttons) {
        return new KeyboardRow(Arrays.asList(buttons));
    }

    public static List<KeyboardRow> getRowList(KeyboardRow... rows) {
        return Arrays.asList(rows);
    }

    public static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> rowList) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rowList);
        markup.setResizeKeyboard(true);
        markup.setSelective(true);
        return markup;
    }

}
