package com.Zephyr.Core.Features;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Collection;

//Checks where the player is currently at on Skyblock.
public class PlayerLocationChecker {
    private static String location;
    private int tickCooldown = 0;
    private static final int COOLDOWN_TIME = 20;

    //Remove non char letters (emojis and stuff). i hate minecraft
    public static String removeSurrogatePairs(String input) {
        if (input == null) return null;

        StringBuilder sb = new StringBuilder();
        int length = input.length();

        for (int offset = 0; offset < length; ) {
            int codePoint = input.codePointAt(offset);

            // Filter: allow letters, digits, space, basic punctuation; remove everything else including emojis/symbols
            if (
                    Character.isLetterOrDigit(codePoint) ||
                            Character.isSpaceChar(codePoint) ||
                            codePoint == '.' || codePoint == ',' || codePoint == '!' || codePoint == '?' || codePoint == '\'' || codePoint == '-'
            ) {
                sb.appendCodePoint(codePoint);
            }

            offset += Character.charCount(codePoint);
        }

        return sb.toString();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(Minecraft.getMinecraft().theWorld == null) return;
        //Tick Cooldown to help with performance
        if(tickCooldown > 0) {
            tickCooldown--;
            return;
        }

        //Reset cooldown
        tickCooldown = COOLDOWN_TIME;

        //Gets the scoreboard
        Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();
        //Gets the sidebar
        ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);
        //Stops if no sidebar is present.
        if(sidebar == null) return;
        //Gets each line on the sidebar
        Collection<Score> scores = scoreboard.getSortedScores(sidebar);
        for(Score score : scores) {
            String rawLine = score.getPlayerName();
            String formattedLine = rawLine;
            ScorePlayerTeam team = scoreboard.getPlayersTeam(rawLine);
            //Check for new line or blank lines in the sidebar.
            if(rawLine == null || rawLine.startsWith("#")) continue;
            //Get all sidebar text components and format the text together
            if(scoreboard.getPlayersTeam(rawLine) != null) {
                // Combine prefix + playerName + suffix to get the full line
                formattedLine = team.getColorPrefix() + rawLine + team.getColorSuffix();
            }
            //Check for Hypixel island on sidebar
            if(formattedLine.contains(Character.toString((char)0x23e3))) {
                //Get the island name and remove all unnecessary word parts.
                location = EnumChatFormatting.getTextWithoutFormattingCodes(formattedLine)
                        .replace(Character.toString((char)0x23e3), "")
                        .trim();
                location = removeSurrogatePairs(location).trim();
                break;
            }
        }
    }


    public static String getLocation() {
        if(location != null) {
            return location;
        }else {
            return "N/A";
        }
    }
}
