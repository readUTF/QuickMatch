package com.readutf.proxy.utils;

import java.util.ArrayList;
import java.util.List;

public class ChatGraph {

    public static List<String> generateGraph(List<Double> values, int max) {
        int yIncrement = max / 8;

        List<String> lines = new ArrayList<>();

        for (int i = 8; i > 0; i--) {
            StringBuilder line = new StringBuilder();
            for (int size = values.size() - 1; size > Math.max(0, values.size() - 34) - 1; size--) {
                Double current = values.get(size);

                if (current >= (yIncrement * i)) {
                    line.append("⬛");
                } else {
                    line.append("⬜");
                }
            }
            lines.add(line.toString());
        }
        return lines;
    }
}
