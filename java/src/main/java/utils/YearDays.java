import java.util.Calendar;
import java.util.Locale;

public class YearDays {

   public static void main(String[] args) {
	   String year = args[0];
       Calendar c = Calendar.getInstance(Locale.getDefault());
	   c.set(Calendar.YEAR, Integer.parseInt(year));

       for (int month = 0; month < c.getMaximum(Calendar.MONTH) + 1; month++) {
           c.set(Calendar.MONTH, month);
           for (int day = 1; day <= c.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
               c.set(Calendar.DAY_OF_MONTH, day);

               System.out.println(day + "/" + (month + 1) + "/" + c.get(Calendar.YEAR));
			   			   
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