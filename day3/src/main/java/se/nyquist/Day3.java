package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Hello world!
 *
 */
public class Day3
{

    public static final Pattern SYMBOL_PATTERN = Pattern.compile("[^.\\d]");
    public static final Pattern PART_PATTERN = Pattern.compile("\\d+");

    public static void main(String[] args )
    {
        var input = "input.txt";
        // var input = "sample.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day3.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().toList();
                var parts = new ArrayList<PartNumber>();
                var symbols = new ArrayList<Symbol>();
                IntStream.range(0,lines.size()).forEach(l -> parseLine(l, lines.get(l), parts, symbols));
                exercise1(parts, symbols);
                exercise2(parts, symbols);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exercise1(List<PartNumber> parts, List<Symbol> symbols) {
        var validParts = parts.stream().filter(p -> p.isAdjacent(symbols)).toList();
        System.out.println("Day 3 Exercise 1: " + validParts.stream().map(PartNumber::value).reduce(Integer::sum).orElse(0));
    }

    private static void exercise2(List<PartNumber> parts, List<Symbol> symbols) {
        var gearSum = symbols.stream().mapToLong(s -> s.gearNumber(parts)).reduce(Long::sum).orElse(0);
        System.out.println("Day 3 Exercise 2: " + gearSum);
    }

    private static void parseLine(int row, String l, ArrayList<PartNumber> parts, ArrayList<Symbol> symbols) {
        var pattern = PART_PATTERN;
        var matcher = pattern.matcher(l);
        int start = 0;
        while (matcher.find(start)) {
            var number = Integer.parseInt(matcher.group());
            var pos = matcher.start();
            var last = matcher.end()-1;
            start = matcher.end();
            parts.add(new PartNumber(number, row, pos, last));
        }
        var symbolMatcher = SYMBOL_PATTERN.matcher(l);
        start = 0;
        while (symbolMatcher.find(start)) {
            var symbol = symbolMatcher.group();
            var pos = symbolMatcher.start();
            start = symbolMatcher.end();
            symbols.add(new Symbol(symbol, row, pos));
        }
    }
}
