package tweet;

import java.util.Comparator;

public class TweetComparatorWithTime implements Comparator<Tweet> {

    @Override
    public int compare(Tweet o1, Tweet o2) {
        return o2.getTweetTime().compareTo(o1.getTweetTime());
    }
}
