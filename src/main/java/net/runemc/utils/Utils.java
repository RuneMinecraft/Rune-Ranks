package net.runemc.utils;

public class Utils {
    public static String Colour(String s) {
        char altColorChar = '&';
        StringBuilder b = new StringBuilder();
        int i = 0;
        boolean color = false;
        boolean hashtag = false;
        boolean doubleTag = false;

        while (i < s.length()) {
            char c = s.charAt(i);
            if (doubleTag) {
                doubleTag = false;
                int max = i + 3;
                if (max <= s.length() && s.substring(i, max).matches("[0-9a-fA-F]{3}")) {
                    b.append('§').append('x');
                    for (int j = 0; j < 3; j++) {
                        b.append('§').append(s.charAt(i++)).append('§').append(s.charAt(i));
                    }
                    continue;
                }
                b.append(altColorChar).append("##");
            } else if (hashtag) {
                hashtag = false;
                if (c == '#') {
                    doubleTag = true;
                    i++;
                    continue;
                }
                int max = i + 6;
                if (max <= s.length() && s.substring(i, max).matches("[0-9a-fA-F]{6}")) {
                    b.append('§').append('x');
                    for (int j = 0; j < 6; j++) {
                        b.append('§').append(s.charAt(i++));
                    }
                    continue;
                }
                b.append(altColorChar).append('#');
            } else if (color) {
                color = false;
                if (c == '#') {
                    hashtag = true;
                    i++;
                } else if ("0123456789abcdefABCDEFrRkKoO".indexOf(c) >= 0) {
                    b.append('§').append(c);
                    i++;
                } else {
                    b.append(altColorChar);
                }
            } else if (c == altColorChar) {
                color = true;
                i++;
            } else {
                b.append(c);
            }
            i++;
        }
        if (color) b.append(altColorChar);
        else if (hashtag) b.append(altColorChar).append('#');
        else if (doubleTag) b.append(altColorChar).append("##");

        return b.toString();
    }
}