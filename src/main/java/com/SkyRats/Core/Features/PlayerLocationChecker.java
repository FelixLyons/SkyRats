package com.SkyRats.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collection;

//Checks where the player is currently at on Skyblock.
public class PlayerLocationChecker {
    private String location;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().theWorld == null) return;
        //Gets the scoreboard
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        //Gets the sidebar
        ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
        //Stops if no sidebar is present.
        if(sidebar == null) return;
        //Gets each line on the sidebar
        Collection<Score> scores = scoreboard.getSortedScores(sidebar);
        for(Score score : scores) {
            //Check for new line or blank lines in the sidebar.
            if(score.getPlayerName() == null || score.getPlayerName().startsWith("#")) continue;
            if(scoreboard.getPlayersTeam(score.getPlayerName()) == null) continue;
            //Get all sidebar text components and format the text together
            String line = scoreboard.getPlayersTeam(score.getPlayerName()).formatString(score.getPlayerName());
            //Check for Hypixel island on sidebar
            if(line.contains("⏣")) {
                //Get the island name and remove all unnecessary word parts.
                location = line.replace("⏣", "").replaceAll("§.", "").trim();
            }
        }
    }


    public static String getLocation() {
        return location;
    }
}
