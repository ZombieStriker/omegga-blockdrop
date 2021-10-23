package me.zombie_striker.blockdrop;

import com.kmschr.brs.*;
import me.zombie_striker.omeggajava.JOmegga;
import me.zombie_striker.omeggajava.RPCResponse;
import me.zombie_striker.omeggajava.events.*;
import me.zombie_striker.omeggajava.objects.PromisedObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDrop implements Listener {

    private SaveData plate;
    private RPCResponse response;

    private List<Plate> dropPlates = new LinkedList<>();

    private User underplateUser = new User(UUID.fromString("aaaaaaaa-aaab-aaaa-aaaa-aaaaaaaaaaba"), "BP_UnderPlate");

    private int speed = 20 * 4;
    private int tickCounter = speed;
    private ColorEnum safeTile = null;
    private boolean clearing = false;

    private int ticks = 0;
    private int difficulty = 1;

    public static void main(String... args) {
        JOmegga.init();
        new BlockDrop().init();
        while(JOmegga.isRunning()){}
    }

    public void init() {
        JOmegga.registerListener(this);
    }
    @EventHandler
    public void onBootstrap(BootstrapEvent event){
        File savefolder = new File(JOmegga.getOmeggaDir(), "data/Saved/Builds");
        File saveFile = new File(savefolder, "blockdrop_panel.brs");
        File saveSpawnFile = new File(savefolder, "invisiblespawn.brs");
        if (!saveFile.exists()) {
            File originalfile = new File(JOmegga.getJOmeggaJar().getParentFile(), "blockdrop_panel.brs");
            if (!originalfile.exists()) {
                JOmegga.logAndBroadcast("Failed to find block panel brs file");
            }
            try {
                Files.copy(originalfile.toPath(), saveFile.toPath());
                Files.copy(new File(JOmegga.getJOmeggaJar().getParentFile(), "invisiblespawn.brs").toPath(), saveSpawnFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
        }
    }

    @EventHandler
    public void onStop(StopEvent event) {
        JOmegga.broadcast("[!]Attempting to stop blockdrop. Might be a glitched round");
    }

    private boolean loadComplete = false;

    @EventHandler
    public void tick(TickEvent event) {
        if (tickCounter == 0) {
            if (!loadComplete) {
                if (plate == null) {
                    File savefolder = new File(JOmegga.getOmeggaDir(), "data/Saved/Builds");
                    File saveFile = new File(savefolder, "blockdrop_panel.brs");
                    if(!saveFile.exists()){
                        JOmegga.log("Save file does not exist");
                        return;
                    }
                    if (response == null) {
                        response = JOmegga.readSaveData("blockdrop_panel");
                        JOmegga.log("Creating response");
                        return;
                    } else if (response.getReturnValue() == null) {
                        JOmegga.log("Loading the data is still null");
                        return;
                    }
                    plate = (SaveData) response.getReturnValue();
                }
                JOmegga.log("Clearing bricks, as this may be initializing");
                JOmegga.clearallbricks(true);
                File savefolder = new File(JOmegga.getOmeggaDir(), "/data/Saved/Builds");
                for (ColorEnum ce : ColorEnum.values()) {
                    if (!new File(savefolder, ce.getSavename() + ".brs").exists()) {
                        for (Brick brick : plate.getBricks()) {
                            brick.setColor(new ColorMode(ce.getColorID()));
                            plate.setAuthor(ce.getUser());
                            plate.getBrickOwners().clear();
                            plate.getBrickOwners().add(ce.getUser());
                            brick.setOwnerIndex(1);
                        }
                        JOmegga.loadSaveData(plate.toRPCData(), 0, 0, 0, true);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        JOmegga.save(ce.getSavename());
                        JOmegga.logAndBroadcast("Saving bricks for " + ce.getName());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        JOmegga.clearallbricks(true);
                        return;
                    }
                }
                loadComplete = true;
                JOmegga.loadBricks("invisiblespawn", 80 * 8, 80 * 8, 1000 + 12);
            }
            if (safeTile == null) {
                ColorEnum random = null;
                boolean found = false;
                random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(difficulty)];
                if (dropPlates.size() > 1) {
                    while (!found) {
                        for (Plate plat : dropPlates) {
                            if (plat.getColor() == random) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(difficulty)];
                        }
                    }
                }

                safeTile = random;
                JOmegga.broadcast("Tiles that are not <color=\"" + safeTile.getHex() + "\"><b>" + safeTile.getName() + "</></b> will disappear.");
                tickCounter = speed;
                speed = (int) (((Math.sin(((double) ticks) / 100) + 1) * 25) + 50);
            } else {
                if (clearing) {
                    clearing = false;
                    loadUnderPlate();
                    clearAllPlates(underplateUser);
                    loadPlates();
                    tickCounter = speed;
                    safeTile = null;
                    difficulty = (int) Math.max(2, (((Math.sin(((double) ticks) / 10) + 1.0) * ColorEnum.values().length / 2.0) + 1));
                    JOmegga.log(difficulty + " current Difficulty");
                    ticks++;
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
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (Brick brick : plate.getBricks()) {
                    brick.setColor(new ColorMode(0));
                    plate.setAuthor(underplateUser);
                    plate.getBrickOwners().clear();
                    plate.getBrickOwners().add(underplateUser);
                    brick.setOwnerIndex(1);
                }
                Plate floor = new Plate(null, plate);
                dropPlates.add(floor);
                JOmegga.loadSaveData(plate.toRPCData(), x * 80, y * 80, 990, true);
                //JOmegga.loadBricks("blockdrop_panel", x * 80, y * 80, 990, true);
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
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                ColorEnum random = ColorEnum.values()[ThreadLocalRandom.current().nextInt(difficulty)];
                for (Brick brick : plate.getBricks()) {
                    brick.setColor(new ColorMode(random.getColorID()));
                    plate.setAuthor(random.getUser());
                    plate.getBrickOwners().clear();
                    plate.getBrickOwners().add(random.getUser());
                    brick.setOwnerIndex(1);
                }
                Plate floor = new Plate(random, plate);
                dropPlates.add(floor);
                JOmegga.loadBricks(random.getSavename(), x * 80, y * 80, 1000, true);
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
        JOmegga.log("CHAT EVENT " + event.getCommand() + " " + event.getPlayername());
        if (event.getCommand().equalsIgnoreCase("blockdrop")) {
            if (event.getArgs().length > 0) {
                if (event.getArgs()[0].equalsIgnoreCase("createfloor")) {
                    JOmegga.save("blockdrop_panel");
                    response = JOmegga.readSaveData("blockdrop_panel");
                    JOmegga.broadcast("Starting game!");
                }
            }
        } else if (event.getCommand().equalsIgnoreCase("setspeed")) {
            if (event.getPlayername().equalsIgnoreCase("Zombie_Striker")) {
                if (event.getArgs().length > 0) {
                    int speed = Integer.parseInt(event.getArgs()[0]);
                    JOmegga.broadcast("Setting speed to " + (((double) speed) / 20) + ".");
                    this.speed = speed;
                } else {
                    this.speed = 20 * 5;
                }
            } else {
                JOmegga.broadcast("You're not Zombie_Striker, " + event.getPlayername());
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
    }
}
