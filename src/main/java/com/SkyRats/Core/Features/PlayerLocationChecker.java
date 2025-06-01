package com.SkyRats.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;

//Checks where the player is currently at on Skyblock.
public class PlayerLocationChecker {
    //Gets the scoreboard
    private Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
    //Gets the sidebar
    private ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
    private String location;

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if(event.entity == Minecraft.getMinecraft().thePlayer) {
            if(sidebar != null) {
                //Gets each line on the sidebar
                Collection<Score> scores = scoreboard.getSortedScores(sidebar);
                for(Score score : scores) {
                    //Check for new line or blank lines in the sidebar.
                    if(score.getPlayerName() == null || score.getPlayerName().startsWith("#")) continue;
                    //Get all sidebar text components and format the text together
                    String line = scoreboard.getPlayersTeam(score.getPlayerName()).formatString(score.getPlayerName());
                    //Check for Hypixel island on sidebar
                    if(line.contains("⏣")) {
                        //Get the island name and remove all unnecessary word parts.
                        location = line.replace("⏣", "").replaceAll("§.", "").trim();
                    }
                }
            }
        }
    }

    public String getLocation() {
        return location;
    }
}
