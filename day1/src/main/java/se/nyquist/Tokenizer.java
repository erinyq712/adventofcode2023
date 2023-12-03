package se.nyquist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenizer {
    private String v;

    public Tokenizer(String value) {
        this.v = value;
    }

    public String getValue() {
        List<Character> charList = new ArrayList<>(v.length());
        var next = tokenize(v, charList);
        while (! next.isEmpty()) {
            next = tokenize(next, charList);
        }
        return charList.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static String tokenize(String s, List<Character> chars) {
        var c = s.charAt(0);
        if (s.startsWith("one")) {
            chars.add('1');
            return s.substring(3);
        } else if (s.startsWith("two")) {
            chars.add('2');
            return s.substring(3);
        } else if (s.startsWith("three")) {
            chars.add('3');
            return s.substring(5);
        } else if (s.startsWith("four")) {
            chars.add('4');
            return s.substring(4);
        } else if (s.startsWith("five")) {
            chars.add('5');
            return s.substring(4);
        } else if (s.startsWith("six")) {
            chars.add('6');
            return s.substring(3);
        } else if (s.startsWith("seven")) {
            chars.add('7');
            return s.substring(5);
        } else if (s.startsWith("eight")) {
            chars.add('8');
            return s.substring(5);
        } else if (s.startsWith("nine")) {
            chars.add('9');
            return s.substring(4);
        }
        chars.add(c);
        return s.substring(1);
    }
}
