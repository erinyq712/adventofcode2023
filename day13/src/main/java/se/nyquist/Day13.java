package se.nyquist;

import javax.swing.text.html.Option;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day13 {

    public static void main(String[] args) {
        // var input = "input.txt";
        var input = "sample.txt";

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
                var results2 = new ArrayList<Integer>();
                while (line != null)
                {
                    horizontalLines.add(line);
                    line = reader.readLine();
                    if (line == null || line.isEmpty()) {
                        results.add(process(horizontalLines));
                        results2.add(process2(horizontalLines));
                        horizontalLines = new ArrayList<>();
                        line = reader.readLine();
                    }
                }
                var result = results.stream().reduce(Integer::sum).orElse(0);
                System.out.println("Exercise 1: " + result);
                var result2 = results2.stream().reduce(Integer::sum).orElse(0);
                System.out.println("Exercise 2: " + result2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int process(ArrayList<String> horizontalLines) {
        var yval = processHorizontal(horizontalLines);
        var xval = processVertical(transpose(horizontalLines));
        return xval + yval;
    }

    private static int processHorizontal(List<String> horizontalLines) {
        return Stream.of(findCenter(horizontalLines)).filter(i -> i > -1).map(i -> (i+1)*100).reduce(Integer::sum).orElse(0);
    }

    private static int processVertical(List<String> verticalLines) {
        return Stream.of(findCenter(verticalLines)).filter(i -> i > -1).map(i -> i+1).reduce(Integer::sum).orElse(0);
    }

    private static List<String> transpose(ArrayList<String> horizontalLines) {
        return IntStream.range(0, horizontalLines.getFirst().length()).boxed()
                .map(x -> {
                    var result = horizontalLines.stream().mapToInt(horizontalLine -> horizontalLine.charAt(x)).toArray();
                    return Arrays.stream(result).mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining());
                }).toList();
    }

    private static int findCenter(List<String> lines) {
        var arr = IntStream.range(0, lines.size() - 1)
                .filter(i -> lines.get(i).equals(lines.get(i + 1)))
                .filter(i -> checkCenter(i, lines))
                .toArray();
        return arr.length > 0 ? arr[0] : -1;
    }

    private static boolean checkCenter(int pos, List<String> lines) {
        var checkDistance = Math.min(pos,lines.size()-pos-2);
        return IntStream.range(1,checkDistance+1).allMatch(i -> lines.get(pos-i).equals(lines.get(pos+i+1)));
    }

    private static int process2(ArrayList<String> horizontalLines) {
        var ysmudge = findPossibleSmudge(horizontalLines).mapToInt(p -> processHorizontal(fix(p, horizontalLines).toList())).reduce(Integer::sum).orElse(0);
        var verticalLines = transpose(horizontalLines);
        var xsmudge = findPossibleSmudge(verticalLines).mapToInt(p -> processVertical(fix(p, verticalLines).toList())).reduce(Integer::sum).orElse(0);
        return ysmudge + xsmudge;
    }

    private static Stream<String> fix(Pair<Integer> p, List<String> horizontalLines) {
        return IntStream.range(0, horizontalLines.size()).mapToObj(i -> {
            if (i == p.right()) {
                return horizontalLines.get(p.left());
            } else {
                return  horizontalLines.get(i);
            }
        });
    }

    private static Stream<Pair<Integer>> findPossibleSmudge(List<String> lines) {
        return Stream.concat(IntStream.range(0, lines.size() - 1)
                .filter(i -> lines.get(i).equals(lines.get(i + 1)))
                .boxed().flatMap(i -> checkSmudge(i, lines)),
                IntStream.range(0, lines.size() - 1)
                        .filter(i -> isPossibleReflectionAfterSmudge(lines.get(i),lines.get(i + 1)))
                        .boxed()
                        .flatMap(i -> Stream.of(new Pair<Integer>(i,i+1))));
    }

    private static boolean isPossibleReflectionAfterSmudge(String line, String line2) {
        if (line.length() == line2.length()) {
            var result = IntStream.range(0,line.length()).filter(i -> line.charAt(i) != line2.charAt(i)).count() == 1;
            return result;
        }
        return false;
    }

    private static Stream<Pair<Integer>> checkSmudge(int pos, List<String> lines) {
        var checkDistance = Math.min(pos,lines.size()-pos-2);
        return IntStream.range(1,checkDistance+1).boxed().flatMap(i -> {
            var areEqual = lines.get(pos-i).equals(lines.get(pos+i+1));
            if (! areEqual && isPossibleReflectionAfterSmudge(lines.get(pos-i),lines.get(pos+i+1))) {
                return Stream.of(new Pair<Integer>(pos-i, pos+i+1));
            }
            return Stream.of();
        });
    }


}
