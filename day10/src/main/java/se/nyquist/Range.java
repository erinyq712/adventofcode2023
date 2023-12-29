package se.nyquist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Range(long start, long length) {
    public static final Range NULL = new Range(0,0);

    public boolean isOverlapping(Range mapped) {
        return start <= mapped.start && start + length >= mapped.start ||
                mapped.start <= start && mapped.start + mapped.length >= start;
    }

    public boolean isExtension(Range previous) {
        return previous.start + previous.length == this.start;
    }

    public Range extend(long delta) {
        return new Range(start,length+delta);
    }

    public Range intersection(Range mapped) {
        if (isOverlapping(mapped)) {
            if (start <= mapped.start) {
                return getRange(mapped);
            } else {
                return mapped.getRange(this);
            }
        }
        return NULL;
    }

    private Range getRange(Range mapped) {
        return start + length < mapped.start + mapped.length ?
                new Range(mapped.start, start + length - mapped.start) :
                new Range(mapped.start, mapped.length);
    }

    public List<Range> subtract(Range diff) {
        if (! isOverlapping(diff)) {
            return List.of(this);
        }
        var result = new ArrayList<Range>();
        Range beginRange = new Range(start, diff.start - start);
        Range endRange = new Range(diff.start+diff.length,start + length-diff.start - diff.length);
        if (beginRange.isValid()) {
            result.add(beginRange);
        }
        if (endRange.isValid()) {
            result.add(endRange);
        }
        return result;
    }

    private boolean isValid() {
        return start >= 0 && length > 0;
    }

    public List<Range> subtractAll(List<Range> ranges) {
        List<Range> current = new ArrayList<>();
        current.add(this);
        for(var r : ranges) {
            var next = current.stream().flatMap(s -> s.subtract(r).stream()).toList();
            current = new ArrayList<>(next);
        }
        return current;
    }

    public long next() {
        return start + length;
    }
}
