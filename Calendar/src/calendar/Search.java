package calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Processes user search input
 * Created by Will. Celestin on 7/14/2015.
 */

public class Search {

    /**
     * Returns a formatted version of user imputed date
     */
    public static String getCleanDate(String userDate, String currDate) {
        try {
            try { // catch faults in user input return to today if error is found
                String[] split = userDate.split("/");
                int gdate = Integer.parseInt(split[1]);
                int gmonth = Integer.parseInt(split[0]);
                long gyear = Long.parseLong(split[2]);

                if (gdate < 1 || gdate > 31 || gmonth < 1 || gmonth > 12 || gyear < 1 || gyear > 1000000) {
                    Meeti.dayShift = 0;
                    return null;
                }
            } catch (NumberFormatException e) {
                Meeti.dayShift = 0;
                return null;
            } catch (IndexOutOfBoundsException e) {
                Meeti.dayShift = 0;
                return null;
            }

            // get the date
            java.util.Calendar c = java.util.Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            Date d = format.parse(userDate);
            c.setTime(d);

            int day = c.get(java.util.Calendar.DAY_OF_WEEK);
            int month = c.get(java.util.Calendar.MONTH);
            int year = c.get(java.util.Calendar.YEAR);
            int date = c.get(java.util.Calendar.DATE);

            String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            String[] months = {"January", "February", "March", "April", "May", "June", "July",
                    "August", "September", "October", "November", "December"};

            // format the date
            String thedate = days[day - 1] + ", ";
            thedate += months[month] + " ";
            thedate += date + " ";
            thedate += year;

            Meeti.dayShift += getDif(currDate, thedate); // keep invariant

            // set the season
            if (month + 1 == 12 || (month + 1 > 0 && month + 1 < 3)) {
                Meeti.season = "winter";
            } else if (month + 1 > 2 && month + 1 < 6) {
                Meeti.season = "spring";
            } else if (month + 1 > 5 && month + 1 < 9) {
                Meeti.season = "summer";
            } else if (month + 1 > 8 && month + 1 < 12) {
                Meeti.season = "fall";
            }
            return thedate;

        } catch (java.text.ParseException e) {
        }
        return null;
    }

    /**
     * Returns the number of days between two dates
     */
    public static int getDif(String now, String other) {
        try {
            // split the dates to usable integers
            String[] nowSplit = now.split(" ");
            String[] otherSplit = other.split(" ");

            String[] months = {"January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"};
            ArrayList<String> MONTHS = new ArrayList<>(12);
            for (String m : months) {
                MONTHS.add(m);
            }

            int now_date = Integer.parseInt(nowSplit[2]);
            int now_month = MONTHS.indexOf(nowSplit[1]) + 1;
            int now_year = Integer.parseInt(nowSplit[3]);

            int other_date = Integer.parseInt(otherSplit[2]);
            int other_month = MONTHS.indexOf(otherSplit[1]) + 1;
            int other_year = Integer.parseInt(otherSplit[3]);

            // CREDITS TO http://en.wikipedia.org/wiki/Julian_day for the jdn mathematical formula used
            int a_now = (14 - now_month) / 12;
            int y_now = now_year + 4800 - a_now;
            int m_now = now_month + 12 * a_now - 3;
            int jdn_now =
                    now_date + (153 * m_now + 2) / 5 + 365 * y_now +
                            y_now / 4 - y_now / 100 + y_now / 400 - 32045;

            int a_other = (14 - other_month) / 12;
            int y_other = other_year + 4800 - a_other;
            int m_other = other_month + 12 * a_other - 3;
            int jad_other =
                    other_date + (153 * m_other + 2) / 5 + 365 * y_other +
                            y_other / 4 - y_other / 100 + y_other / 400 - 32045;

            return jad_other - jdn_now;

        } catch (NullPointerException e) {
            return 0;
        }
    }

    /**
     * Returns true if a user inquiry is a date
     */
    public static boolean isDate(String userinput) {
        if (userinput.split("/").length == 3) {
            return true;
        } else {
            return false;
        }
    }
}