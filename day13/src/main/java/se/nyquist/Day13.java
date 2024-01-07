package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 {

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";

        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day13.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var reader = new BufferedReader(new InputStreamReader(stream));
                var line = reader.readLine();
                int y = 0;
                var horizontalLines = new ArrayList<String>();
                var results = new ArrayList<Integer>();
                while (line != null)
                {
                    final var currentLine = line;
                    horizontalLines.add(line);
                    line = reader.readLine();
                    if (line == null || line.isEmpty()) {
                        results.add(process(horizontalLines));
                        horizontalLines =new ArrayList<String>();
                        line = reader.readLine();
                    }
                }
                var result = results.stream().reduce(Integer::sum).orElse(0);
                System.out.println("Exercise 1: " + result);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int process(ArrayList<String> horizontalLines) {
        var yval = Stream.of(findCenter(horizontalLines)).filter(i -> i > -1).map(i -> (i+1)*100).reduce(Integer::sum).orElse(0);
        var verticalLines = transpose(horizontalLines);
        var xval = Stream.of(findCenter(verticalLines)).filter(i -> i > -1).map(i -> i+1).reduce(Integer::sum).orElse(0);
        return xval + yval;
    }

    private static List<String> transpose(ArrayList<String> horizontalLines) {
        return IntStream.range(0, horizontalLines.getFirst().length()).boxed()
                .map(x -> {
                    var result = horizontalLines.stream().mapToInt(horizontalLine -> horizontalLine.charAt(x)).toArray();
                    return Arrays.stream(result).mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
                }).toList();
    }

    private static <T> int findCenter(List<T> lines) {
        var arr = IntStream.range(0, lines.size() - 1)
                .filter(i -> lines.get(i).equals(lines.get(i + 1)))
                .filter(i -> checkCenter(i, lines))
                .toArray();
        return arr.length > 0 ? arr[0] : -1;
    }

    private static <T> boolean checkCenter(int pos, List<T> lines) {
        var checkDistance = Math.min(pos,lines.size()-pos-2);
        return IntStream.range(1,checkDistance+1).allMatch(i -> lines.get(pos-i).equals(lines.get(pos+i+1)));
    }


}
