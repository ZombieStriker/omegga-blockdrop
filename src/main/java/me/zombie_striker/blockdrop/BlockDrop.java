package me.zombie_striker.blockdrop;

import com.kmschr.brs.*;
import me.zombie_striker.omeggajava.JOmegga;
import me.zombie_striker.omeggajava.RPCResponse;
import me.zombie_striker.omeggajava.events.*;
import me.zombie_striker.omeggajava.plugins.OmeggaMain;
import me.zombie_striker.omeggajava.plugins.OmeggaPlugin;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@OmeggaMain(author = "Zombie_Striker", pluginname = "BlockDrop")
public class BlockDrop extends OmeggaPlugin implements Listener {

    private SaveData plate;
    private RPCResponse response;

    private List<Plate> dropPlates = new LinkedList<>();

    private User underplateUser = new User(UUID.fromString("aaaaaaaa-aaab-aaaa-aaaa-aaaaaaaaaaba"),"BP_UnderPlate");

    private int tickCounter = 20 * 5;
    private ColorEnum safeTile = null;
    private boolean clearing = false;

    @Override
    public void onInit() {
        super.onInit();
        File savefolder = new File(JOmegga.getOmeggaDir(), "data/Saved/Builds");
        if (!savefolder.exists()) {
            JOmegga.log("save folder does not exist. Creating");
            savefolder.mkdirs();
        }
        File saveFile = new File(savefolder, "blockdrop_panel.brs");
        if (!saveFile.exists()) {
            JOmegga.log("Floor does not exist! Clear all bricks, build the floor at 0,0,0, and run !blockdrop createfloor");
            JOmegga.broadcast("Floor does not exist! Clear all bricks, build the floor at 0,0,0, and run !blockdrop createfloor");
        } else {
            response = JOmegga.readSaveData("blockdrop_panel");
        }
        JOmegga.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @EventHandler
    public void tick(TickEvent event) {
        if (tickCounter == 0) {
            if (safeTile == null) {
                ColorEnum random = null;
                boolean found = false;
                random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(ColorEnum.values().length)];
                if (dropPlates.size() > 1) {
                    while (!found) {
                        for (Plate plat : dropPlates) {
                            if (plat.getColor() == random) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(ColorEnum.values().length)];
                        }
                    }
                }

                safeTile = random;
                JOmegga.broadcast(">" + safeTile.name());
                tickCounter = 5 * 20;
            } else {
                if (clearing) {
                    clearing = false;
                    loadUnderPlate();
                    clearAllPlates(underplateUser);
                    loadPlates();
                    tickCounter = 5 * 20;
                    safeTile = null;
                } else {
                    clearing = true;
                    clearAllPlates(safeTile);
                    tickCounter = 5 * 20;
                }
            }
        }
        tickCounter--;
    }

    public void loadUnderPlate() {
        if (plate == null) {
            if (response == null || response.getReturnValue() == null) {
                return;
            }
            plate = (SaveData) response.getReturnValue();
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ColorEnum random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(ColorEnum.values().length)];
                for (Brick brick : plate.getBricks()) {
                    brick.setColor(new ColorMode(0));
                    plate.setAuthor(underplateUser);
                    plate.getBrickOwners().clear();
                    plate.getBrickOwners().add(underplateUser);
                    brick.setOwnerIndex(1);
                }
                Plate floor = new Plate(random, plate);
                dropPlates.add(floor);
                JOmegga.loadSaveData(plate.toRPCData(), x * 160, y * 160, 990, true);
            }
        }
    }
    public void loadPlates() {
        if (plate == null) {
            if (response == null || response.getReturnValue() == null) {
                return;
            }
            plate = (SaveData) response.getReturnValue();
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                ColorEnum random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(ColorEnum.values().length)];
                for (Brick brick : plate.getBricks()) {
                    brick.setColor(new ColorMode(random.getColorID()));
                    plate.setAuthor(random.getUser());
                    plate.getBrickOwners().clear();
                    plate.getBrickOwners().add(random.getUser());
                    brick.setOwnerIndex(1);
                }
                Plate floor = new Plate(random, plate);
                dropPlates.add(floor);
                JOmegga.loadSaveData(plate.toRPCData(), x * 160, y * 160, 1000, true);
            }
        }
    }

    public void clearAllPlates(User except) {
        dropPlates.clear();
        for (ColorEnum c : ColorEnum.values()) {
            JOmegga.clearBricks(c.getUser().getId().toString(), true);
        }
    }
    public void clearAllPlates(ColorEnum except) {
        dropPlates.clear();
        JOmegga.clearBricks(underplateUser.getId().toString(), true);
        for (ColorEnum c : ColorEnum.values()) {
            if (c == except) {
                continue;
            }
            JOmegga.clearBricks(c.getUser().getId().toString(), true);
        }
    }


    @EventHandler
    public void onChatCMD(ChatCommandEvent event) {
        if (event.getCommand().equalsIgnoreCase("blockdrop")) {
            if (event.getArgs().length > 0) {
                if (event.getArgs()[0].equalsIgnoreCase("createfloor")) {
                    JOmegga.save("blockdrop_panel");
                    response = JOmegga.readSaveData("blockdrop_panel");
                    JOmegga.broadcast("Starting game!");
                }
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        if (e.getMessage().equalsIgnoreCase("clear")) {
            JOmegga.clearBricks(e.getPlayername(), false);
            JOmegga.log("Clearing brivcks");
        }
    }
}
