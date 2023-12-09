package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Day5 {

    enum Section {
        Seeds(0, "seeds"),
        SeedToSoil(1, "seed-to-soil map"),
        SoilToFertilizer(2, "soil-to-fertilizer map"),
        FertilizerToWater(3, "fertilizer-to-water map"),
        WaterToLight(4, "water-to-light map"),
        LightToTemperature(5, "light-to-temperature map"),
        TemperatureToHumidity(6, "temperature-to-humidity map"),
        HumidityToLocation(7, "humidity-to-location map");

        private final int val;
        private final String label;

        private static Map<String, Section> sections = new HashMap<>();

        static {
            for(var s : Section.values()) {
                sections.put(s.label, s);
            }
        }

        Section(int val, String label) {
            this.val = val;
            this.label = label;
        }

        static Section fromLabel(String label) {
            return sections.get(label);
        }
    }

    public static void main(String[] args) {
        var input = "input.txt";
        // var input = "sample.txt";
        if (args.length > 0) {
            input = args[0];
        }
        try (var stream = Day5.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(l -> ! l.isEmpty()).toList();
                var sections = new HashMap<Section, List<Mapping>>();
                Section section = Section.Seeds;
                var current = new ArrayList<Mapping>();
                var seedLine = lines.get(0);
                if (seedLine.startsWith(Section.Seeds.label)) {
                    var numberInput = seedLine.substring(section.label.length() + 2).split("[ ]+");
                    List<Long> seeds = Stream.of(numberInput).mapToLong(Long::parseLong).boxed().toList();
                    for (int i = 1; i < lines.size(); i++) {
                        var l = lines.get(i);
                        if (l.contains(":")) {
                            if (!current.isEmpty()) {
                                sections.put(section, current);
                            }
                            current = new ArrayList<Mapping>();
                            section = Section.fromLabel(l.substring(0, l.indexOf(':')));
                        } else if (!l.isEmpty()) {
                            if (Character.isDigit(l.charAt(0))) {
                                var numbers = Stream.of(l.split("[ ]+")).mapToLong(Long::parseLong).boxed().toList();
                                current.add(new Mapping(numbers.get(0), numbers.get(1), numbers.get(2)));
                            }
                        }
                    }
                    if (!current.isEmpty()) {
                        sections.put(section, current);
                    }
                    var destinations = seeds.stream().mapToLong(s ->
                            getNext(sections.get(Section.HumidityToLocation),
                                    getNext(sections.get(Section.TemperatureToHumidity),
                                            getNext(sections.get(Section.LightToTemperature),
                                                    getNext(sections.get(Section.WaterToLight),
                                                            getNext(sections.get(Section.FertilizerToWater),
                                                                    getNext(sections.get(Section.SoilToFertilizer),
                                                                            getNext(sections.get(Section.SeedToSoil), s)))))))).boxed().toList();
                    System.out.println("Day 5 Exercise 1: " + destinations.stream().min(Long::compare).orElse(0L));
                }
                // exercise1(sections);
                // exercise2(sections);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static long getNext(List<Mapping> mappings, Long s) {
        for(var mapping : mappings) {
            if (s>=mapping.start() && s< mapping.start()+mapping.length()) {
                return mapping.destination() + (s-mapping.start());
            }
        }
        return s;
    }

    private static void saveMapping(List<Mapping> seedToSoil, HashMap<Long, Long> seedMapping) {
        for(var mapping : seedToSoil) {
            LongStream.range(0,mapping.length()).forEach(i -> seedMapping.put(mapping.start()+i, mapping.destination()+i));
        }
    }

    private static void exercise2(HashMap<Section, List<List<Long>>> sections) {
       // System.out.println("Day 4 Exercise 2: " + result.keySet().stream().map(key -> result.get(key)).reduce(Integer::sum).orElse(0));
    }

    private static void exercise1(HashMap<Section, List<Mapping>> sections) {

        // System.out.println("Day 4 Exercise 1: " + result);
    }

    private static class InvalidInput extends RuntimeException {
        public InvalidInput() {
            super("Invalid input");
        }
    }

}

