package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Day2 {

    public static void main(String[] args) {
        Configuration configuration = new Configuration(14, 13, 12);
        var input = "input.txt";
        // var input = "sample.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day2.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                exercise1(configuration, lines);
                exercise2(lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise1(Configuration configuration, List<String> lines) {
        List<Integer> valid = lines.stream().map(l -> parse(l, configuration)).filter(Objects::nonNull).toList();
        System.out.println("Exercise 1 Sum: " + valid.stream().reduce(Integer::sum).orElse(0));
    }

    private static void exercise2(List<String> lines) {
        List<Configuration> valid = lines.stream().map(l -> parseLine(l)).filter(Objects::nonNull).toList();
        System.out.println("Exercise 2 Sum: " + valid.stream().map(Configuration::power).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
    }

    private static Integer parse(String l, Configuration configuration) {
        var gameValues = l.split(":");
        var roundValues = gameValues[1].split(";");
        for (int i = 0; i < roundValues.length; i++) {
            var round = roundValues[i].trim();
            var colorValues = round.split(",");
            var map = new HashMap<String, Integer>();
            for (int j = 0; j < colorValues.length; j++) {
                var colorValue = colorValues[j].trim();
                var number = Integer.parseInt(colorValue.substring(0, colorValue.indexOf(' ')));
                var color = colorValue.substring(colorValue.indexOf(' ') + 1);
                map.put(color, number);
            }
            var cfg = new Configuration(getColor(map, "blue"), getColor(map, "green"), getColor(map, "red"));
            if (!configuration.isSubset(cfg)) {
                return null;
            }
        }
        var gameValue = gameValues[0];
        return Integer.parseInt(gameValue.substring(gameValue.indexOf(' ') + 1).trim());
    }

    private static Configuration parseLine(String l) {
        var cfgs = new ArrayList<Configuration>();
        var gameValues = l.split(":");
        var roundValues = gameValues[1].split(";");
        for (int i = 0; i < roundValues.length; i++) {
            var round = roundValues[i].trim();
            var colorValues = round.split(",");
            var map = new HashMap<String, Integer>();
            for (int j = 0; j < colorValues.length; j++) {
                var colorValue = colorValues[j].trim();
                var number = Integer.parseInt(colorValue.substring(0, colorValue.indexOf(' ')));
                var color = colorValue.substring(colorValue.indexOf(' ') + 1);
                map.put(color, number);
            }
            cfgs.add(new Configuration(getColor(map, "blue"), getColor(map, "green"), getColor(map, "red")));
        }
        return cfgs.stream().reduce(new Configuration(), Day2::maximum);
    }

    private static Configuration maximum(Configuration a, Configuration b) {
        return new Configuration(max(a.blue(),b.blue()), max(a.green(), b.green()), max(a.red(), b.red()));
    }

    private static int getColor(HashMap<String, Integer> map, String color) {
        return map.getOrDefault(color, 0);
    }
}
