package me.zombie_striker.blockdrop;

import com.kmschr.brs.User;

import java.util.UUID;

public enum ColorEnum {

    RED(13, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), "BD_RED"), "f00","RED","BP_Red"),
    BLUE(19, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"), "BD_BLUE"),"00F","BLUE","BP_BLue"),
    GREEN(67, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"), "BD_GREEN"),"0b0","GREEN","BP_Green"),
    YELLOW(16,new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaad"), "BD_YELLOW"),"ff0","YELLOW","BP_Yellow"),
    LIGHT_BLUE(20, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaae"), "BD_LIGHTBLUE"),"0ff","LIGHT BLUE","BP_LightBlue"),
    PINK(21, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaf"), "BD_PINK"),"faf","PINK","BP_Pink"),
    PURPLE(22, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaba"), "BD_PURPLE"),"a2f","PURPLE","BP_Purple"),
    ORANGE(14, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaabb"), "BD_ORANGE"),"fa0","ORANGE","Bp_Orange"),
    WHITE(0, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaabc"), "BD_WHITE"),"fff","WHITE","BP_White"),
    GRAY(7, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaabd"), "BD_GRAY"),"aaa","GRAY","BP_Gray"),
    BLACK(11, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaabe"), "BD_BLACK"),"555","BLACK","BP_Black"),
    MAROON(12, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaabf"), "BD_MAROON"), "b00","MAROON","BP_Maroon"),
    LIME(18, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaca"), "BD_LIME"),"0f0","LIME","BP_Lime"),
    BROWN(36, new User(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaacb"), "BD_Brown"),"a86","BROWN","BP_Brown"),
    ;

    private int colorid;
    private User user;
    private String hex;
    private String name;
    private String savename;

    ColorEnum(int colorid, User user, String hex, String name,String savename) {
        this.colorid =colorid;
        this.user = user;
        this.hex= hex;
        this.name = name;
        this.savename = savename;
    }

    public String getSavename() {
        return savename;
    }

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }

    public User getUser() {
        return user;
    }

    public int getColorID() {
        return colorid;
    }
}
