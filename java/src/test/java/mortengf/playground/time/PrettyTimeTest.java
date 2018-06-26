package mortengf.playground.time;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Calendar;
import java.util.Date;

public class PrettyTimeTest {
    public static void main(String[] args) {
        Calendar now = Calendar.getInstance();
        long nowInSecs = now.getTimeInMillis() / 1000;

        PrettyTime prettyTime = new PrettyTime();
        String time = prettyTime.format(new Date(nowInSecs));
        System.out.println(time);
    }
}
