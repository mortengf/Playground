package mortengf.playground.utils;

import java.util.Calendar;
import java.util.Locale;

public class WeekDays {

   public static void main(String[] args) {
	   String year = args[0];
       Calendar c = Calendar.getInstance(Locale.getDefault());
	   c.set(Calendar.YEAR, Integer.parseInt(year));

       for (int month = 0; month < c.getMaximum(Calendar.MONTH) + 1; month++) {
           c.set(Calendar.MONTH, month);
           for (int day = 1; day <= c.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
               c.set(Calendar.DAY_OF_MONTH, day);

               if (isWeekDay(c)) {
                   System.out.println(day + "/" + (month + 1) + "/" + c.get(Calendar.YEAR));
               } 
			   
			   // Only print on week total per weekend
			   // TODO: comparing with SUNDAY causes week numbers to increase by 1: why?
			   if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) { 
				   System.out.println("Uge " + c.get(Calendar.WEEK_OF_YEAR) + "-total");
			   }
			   
			   if (c.get(Calendar.DAY_OF_MONTH) == c.getActualMaximum(Calendar.DAY_OF_MONTH)) {
				   System.out.println("I alt - " + new java.text.DateFormatSymbols().getMonths()[month]);
			   }
           }
       }
   }

   private static boolean isWeekDay(Calendar c) {
       return !(c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
   }

}