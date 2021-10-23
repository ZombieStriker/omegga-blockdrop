package me.zombie_striker.blockdrop;

import com.kmschr.brs.User;

import java.util.UUID;

public enum ColorEnum {

    RED(13, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "BD_RED")),
    BLUE(19, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "BD_BLUE")),
    GREEN(18, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"), "BD_GREEN")),
    YELLOW(16,new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad"), "BD_YELLOW")),
    TEAL(20, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaae"), "BD_TEAL")),
    PINK(21, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaf"), "BD_PINK")),
    PURPLE(22, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaba"), "BD_PURPLE")),
    ;

    private int colorid;
    private User user;

    ColorEnum(int colorid, User user) {
        this.colorid =colorid;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public int getColorID() {
        return colorid;
    }
}
