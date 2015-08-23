package com.grayraven.project1;

import java.util.Comparator;

public class RatingsComparator implements Comparator<ExtendedMovie> {
    @Override
    public int compare(ExtendedMovie lhs, ExtendedMovie rhs) {
        boolean result = lhs.getmDb().getVoteAverage() > rhs.getmDb().getVoteAverage();
        return result ? 1:0;
    }
}
