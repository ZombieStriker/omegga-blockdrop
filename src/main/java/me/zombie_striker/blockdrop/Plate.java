package me.zombie_striker.blockdrop;

import com.kmschr.brs.SaveData;
public class Plate {

    private SaveData saveData;
    private ColorEnum color;

    public Plate(ColorEnum colorEnum, SaveData saveData){
        this.saveData = saveData;
        this.color = colorEnum;
    }

    public ColorEnum getColor() {
        return color;
    }
}
