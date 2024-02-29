package com.company.bot;

import com.company.Model.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboard {

    public static InlineKeyboardMarkup getCaption() {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("⏩");
        button1.setCallbackData("next");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("♥");
        button2.setCallbackData("like");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("\uD83D\uDDE8️");
        button3.setCallbackData("comment");

        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        buttonList1.add(button3);
        buttonList1.add(button2);
        buttonList1.add(button1);

        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        list.add(buttonList1);
        list.add(buttonList2);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getUserList(List<User> userList) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        for (User user : userList) {
            InlineKeyboardButton button = new InlineKeyboardButton(
                    user.getFullName() + " -> " + user.getUsername()
            );
            button.setCallbackData(user.getId().toString());

            List<InlineKeyboardButton> rowButtons = new ArrayList<>();
            rowButtons.add(button);
            list.add(rowButtons);
            if (list.size() % 9 == 0) {
                InlineKeyboardButton next = new InlineKeyboardButton(
                        "⏩"
                );
                next.setCallbackData("nextUser");
                List<InlineKeyboardButton> nextRowButtons = new ArrayList<>();
                nextRowButtons.add(next);
                list.add(nextRowButtons);
                break;
            }
        }
        return inlineKeyboardMarkup;
    }


    public static InlineKeyboardMarkup getProfil(int countPhoto, int countFollower, int countFollowee) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Posts\n" + countPhoto);
        button1.setCallbackData("posts");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Followers\n" + countFollower);
        button2.setCallbackData("Followers");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Following️\n" + countFollowee);
        button3.setCallbackData("Following");

        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Edite Profile");
        button5.setCallbackData("Edite Profile");

        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        buttonList1.add(button1);
        buttonList1.add(button2);
        buttonList1.add(button3);

        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        buttonList2.add(button5);
        list.add(buttonList1);
        list.add(buttonList2);

        return inlineKeyboardMarkup;
    }


    public static InlineKeyboardMarkup getEditProfile(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Change username");
        button1.setCallbackData("change_username");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Change password");
        button2.setCallbackData("Change password");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Change main photo");
        button3.setCallbackData("Change main photo");

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Delete account");
        button4.setCallbackData("Delete account");

        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        buttonList1.add(button1);
        buttonList1.add(button2);
        buttonList1.add(button3);

        List<InlineKeyboardButton> buttonList2 = new ArrayList<>();
        buttonList2.add(button4);
        list.add(buttonList1);
        list.add(buttonList2);

        return inlineKeyboardMarkup;
    }


    public static InlineKeyboardMarkup YesNoButton(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> list = new ArrayList<>();
        inlineKeyboardMarkup.setKeyboard(list);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Yes");
        button1.setCallbackData("Yes delete account");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("No");
        button2.setCallbackData("no");

        List<InlineKeyboardButton> buttonList1 = new ArrayList<>();
        buttonList1.add(button1);
        buttonList1.add(button2);

        list.add(buttonList1);

        return inlineKeyboardMarkup;
    }
}
