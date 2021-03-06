package us.skyywastaken.hypixelapi.arcade;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import us.skyywastaken.hypixelapi.HypixelAPI;
import us.skyywastaken.hypixelapi.arcade.game.ArcadeGame;
import us.skyywastaken.hypixelapi.arcade.game.blockingdead.BlockingDead;
import us.skyywastaken.hypixelapi.arcade.game.bountyhunters.BountyHunters;
import us.skyywastaken.hypixelapi.arcade.game.capturethewool.CaptureTheWool;
import us.skyywastaken.hypixelapi.arcade.game.creeperattack.CreeperAttack;
import us.skyywastaken.hypixelapi.arcade.game.enderspleef.EnderSpleef;
import us.skyywastaken.hypixelapi.arcade.game.farmhunt.FarmHunt;
import us.skyywastaken.hypixelapi.arcade.game.football.Football;
import us.skyywastaken.hypixelapi.arcade.game.galaxywars.GalaxyWars;
import us.skyywastaken.hypixelapi.arcade.game.hideandseek.HideAndSeek;
import us.skyywastaken.hypixelapi.arcade.game.holeinthewall.HoleInTheWall;
import us.skyywastaken.hypixelapi.arcade.game.hypixelsays.HypixelSays;
import us.skyywastaken.hypixelapi.arcade.game.miniwalls.MiniWalls;
import us.skyywastaken.hypixelapi.arcade.game.partygames.PartyGames;
import us.skyywastaken.hypixelapi.arcade.game.pixelpainters.PixelPainters;
import us.skyywastaken.hypixelapi.arcade.game.throwout.ThrowOut;
import us.skyywastaken.hypixelapi.arcade.game.zombies.Zombies;

public class Arcade {
    private final static Arcade ARCADE_INSTANCE = new Arcade();
    private final ArcadeHandler ARCADE_HANDLER = new ArcadeHandler(this);
    private final ArcadeScoreboardListener ARCADE_SCOREBOARD_LISTENER = new ArcadeScoreboardListener(this.ARCADE_HANDLER);
    private GamePhase currentGamePhase;
    private ArcadeGame currentGame = null;

    public Arcade() {
        registerGames();
        registerArcadeListeners();
    }

    private void registerArcadeListeners() {
        MinecraftForge.EVENT_BUS.register(this.ARCADE_SCOREBOARD_LISTENER);
        MinecraftForge.EVENT_BUS.register(new ArcadeChatHandler(this.ARCADE_HANDLER));
    }

    public static Arcade getArcade() {
        return ARCADE_INSTANCE;
    }

    public GamePhase getCurrentGamePhase() {
        return currentGamePhase;
    }

    void handleGameChange(ArcadeGame newGame) {
        unregisterCurrentListeners();
        if(newGame != null) {
            registerListeners(newGame);
        }
        this.currentGame = newGame;
    }

    void setGamePhase(GamePhase newGamePhase) {
        this.currentGamePhase = newGamePhase;
    }

    public ArcadeGame getCurrentGame() {
        return this.currentGame;
    }

    private void unregisterCurrentListeners() {
        if(this.currentGame == null || this.currentGame.getListeners() == null) {
            return;
        }
        for(Object currentListener : this.currentGame.getListeners()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Unregister attempt..."));
            MinecraftForge.EVENT_BUS.unregister(currentListener);
        }
    }

    private void registerListeners(ArcadeGame arcadeGame) {
        if(arcadeGame.getListeners() == null) {
            return;
        }
        for(Object currentObject : arcadeGame.getListeners()) {
            MinecraftForge.EVENT_BUS.register(currentObject);
        }
    }

    private void registerGames() {
        registerGame(new BlockingDead());
        registerGame(new BountyHunters());
        registerGame(new CaptureTheWool());
        registerGame(new CreeperAttack());
        registerGame(new EnderSpleef());
        registerGame(new GalaxyWars());
        registerGame(new FarmHunt());
        registerGame(new Football());
        registerGame(new HideAndSeek());
        registerGame(new HoleInTheWall());
        registerGame(new HypixelSays());
        registerGame(new MiniWalls());
        registerGame(new PartyGames());
        registerGame(new PixelPainters());
        registerGame(new ThrowOut());
        registerGame(new Zombies());
    }

    private void registerGame(ArcadeGame arcadeGame) {
        this.ARCADE_SCOREBOARD_LISTENER.registerGame(arcadeGame);
    }

    public void sendStartupMessage() {
        HypixelAPI.LOGGER.info("Getting ready to take over the arcade!");
    }

}
