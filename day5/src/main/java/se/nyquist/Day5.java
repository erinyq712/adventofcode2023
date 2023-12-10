package se.nyquist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

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
        //
        try (var stream = Day5.class.getClassLoader().getResourceAsStream(input)) {
            if (stream != null) {
                var lines = new BufferedReader(new InputStreamReader(stream)).lines().filter(l -> ! l.isEmpty()).toList();
                var sections = new EnumMap<Section, List<Mapping>>(Section.class);
                var seedLine = lines.get(0);
                if (seedLine.startsWith(Section.Seeds.label)) {
                    var numberInput = seedLine.substring(Section.Seeds.label.length() + 2).split("[ ]+");
                    List<Long> seeds = Stream.of(numberInput).mapToLong(Long::parseLong).boxed().toList();
                    processMapping(lines, sections);
                    var destinations = getNearestDestination(seeds, sections);
                    System.out.println("Day 5 Exercise 1: " + destinations.stream().min(Long::compare).orElse(0L));
                    List<Range> seedsRanges = IntStream.range(0, numberInput.length/2)
                            .mapToObj(i -> new Range(Long.parseLong(numberInput[2*i]), Long.parseLong(numberInput[2*i+1])))
                            .toList();
                    var destinations2 = getNearestDestination2(seedsRanges, sections);
                    System.out.println("Day 5 Exercise 2: " + destinations2);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void processMapping(List<String> lines, Map<Section, List<Mapping>> sections) {
        Section section = Section.Seeds;
        var current = new ArrayList<Mapping>();
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
    }

    private static List<Long> getNearestDestination(List<Long> seeds, Map<Section, List<Mapping>> sections) {
        return seeds.stream().mapToLong(s ->
                getNext(sections.get(Section.HumidityToLocation),
                        getNext(sections.get(Section.TemperatureToHumidity),
                                getNext(sections.get(Section.LightToTemperature),
                                        getNext(sections.get(Section.WaterToLight),
                                                getNext(sections.get(Section.FertilizerToWater),
                                                        getNext(sections.get(Section.SoilToFertilizer),
                                                                getNext(sections.get(Section.SeedToSoil), s)))))))).boxed().toList();
    }

    private static long getNext(List<Mapping> mappings, Long s) {
        for(var mapping : mappings) {
            if (s>=mapping.start() && s< mapping.start()+mapping.length()) {
                return mapping.destination() + (s-mapping.start());
            }
        }
        return s;
    }

    private static List<Range> processRanges(List<Range> seeds, List<Mapping> mappings) {
        var s1 = seeds.stream().flatMap(s -> getNext(mappings, s));
        var l1 = s1.toList();
        return l1;
    }

    private static long getNearestDestination2(List<Range> seeds, Map<Section, List<Mapping>> sections) {
        var l1 = processRanges(seeds, sections.get(Section.SeedToSoil));
        var l2 = processRanges(l1, sections.get(Section.SoilToFertilizer));
        var l3 = processRanges(l2, sections.get(Section.FertilizerToWater));
        var l4 = processRanges(l3, sections.get(Section.WaterToLight));
        var l5 = processRanges(l4, sections.get(Section.LightToTemperature));
        var l6 = processRanges(l5, sections.get(Section.TemperatureToHumidity));
        var l7 = processRanges(l6, sections.get(Section.HumidityToLocation));
        return l7.stream().mapToLong(Range::start).min().orElse(0L);
    }

    private static Stream<Range> getNext(List<Mapping> mappings, Range s) {
        List<Range> result = new ArrayList<>();
        List<Range> remainders = List.of(s);
        var next = new ArrayList<Range>();
        for(var mapping : mappings) {
            var mapped = new Range(mapping.start(), mapping.length());
            for(int i = 0; i < remainders.size(); i++) {
                var rem = remainders.get(i);
                var diff = rem.intersection(mapped);
                next.addAll(rem.subtract(diff));
                if (diff != Range.NULL) {
                    var mappedDiff = mapping.getDestinationRange(diff);
                    result.add(mappedDiff);
                }
            }
            remainders = next;

            next = new ArrayList<Range>();
        }
        var length = Stream.concat(remainders.stream(),result.stream()).map(Range::length).reduce(Long::sum).orElse(0L);
        // System.out.println("Invariant: " + length + " - " + s.length());
        return Stream.concat(remainders.stream(),result.stream());
    }

}

