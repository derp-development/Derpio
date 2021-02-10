package me.conclure.derpio.event;

import java.awt.Color;

public class CachedXPTable {

    public static final int[] XP_TABLE;
    public static final int[] XP_TIER_CUTLINE;
    public static final Color[] XP_TIER_COLORS;

    static {
        XP_TABLE = new int[120];
        XP_TIER_CUTLINE = new int[12];
        int prev = 100;
        int tier = 1;
        for (int i = 0; i < XP_TABLE.length; ++i) {
            prev = (int)Math.pow(prev, 1.015);
            XP_TABLE[i] = prev;
            if (i % 10 == 0) {
                XP_TIER_CUTLINE[tier - 1] = prev;
                tier++;
            }
        }
        XP_TIER_COLORS = new Color[]{
                new Color(255, 0, 0),
                new Color(255, 125, 0),
                new Color(255, 255, 0),
                new Color(125, 255, 0),
                new Color(0, 255, 0),
                new Color(0, 255, 125),
                new Color(0, 255, 255),
                new Color(0, 125, 255),
                new Color(0, 0, 255),
                new Color(0, 125, 255),
                new Color(0, 0, 255),
                new Color(125, 0, 255),
                new Color(255, 0, 255),
                new Color(255, 0, 125)
        };
    }

    public static int getTier(final int xp) {
        for (int i = 0; i < XP_TIER_CUTLINE.length; i++) {
            if (xp > XP_TIER_CUTLINE[i]) {
                return i - 1;
            }
        }
        return -1;
    }

    public static Color getColor(final int tier) {
        return XP_TIER_COLORS[tier];
    }

    public static int getTierXp(final int tier) {
        return XP_TIER_CUTLINE[tier];
    }

}
