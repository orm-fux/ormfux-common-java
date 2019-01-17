package org.blue.bunny.common.datatype;

import java.util.Calendar;
import java.util.Date;

/**
 * A Unit of time. The values provide the option to move a date to the first or last day of the 
 * interval represented by the unit.
 */
public enum TimeUnit {
    /**
     * A day.
     */
    DAY(Calendar.DAY_OF_YEAR) {
        /** {@inheritDoc} */
        @Override
        public Date moveDateToFirstDayOfInterval(final Date date) {
            return date;
        }
        
        /** {@inheritDoc} */
        @Override
        public Date moveDateToLastDayOfInterval(final Date date) {
            return date;
        }
    },
    
    /**
     * A week.
     */
    WEEK(Calendar.WEEK_OF_YEAR) {
        /** {@inheritDoc} */
        @Override
        public Date moveDateToFirstDayOfInterval(final Date date) {
            if (date == null) {
                return null;
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(date.getTime());
                
                while (calendar.get(Calendar.DAY_OF_WEEK) != calendar.getFirstDayOfWeek()) {
                    calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of week.
                }
                
                return calendar.getTime();
            }
            
        }
        
        /** {@inheritDoc} */
        @Override
        public Date moveDateToLastDayOfInterval(final Date date) {
            if (date == null) {
                return null;
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(date.getTime());
                
                final int week = calendar.get(getCalendarCode());
                
                while (calendar.get(getCalendarCode()) == week) {
                    calendar.add(Calendar.DATE, +1); // Add 1 day until first day of the next week.
                }
                
                calendar.add(Calendar.DATE, -1); // subtract 1 day to get to the last day of the previous week.
                
                return calendar.getTime();
                
            }
        }
        
    },
    
    /**
     * A month.
     */
    MONTH(Calendar.MONTH) {
        /** {@inheritDoc} */
        @Override
        public Date moveDateToFirstDayOfInterval(final Date date) {
            if (date == null) {
                return null;
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(date.getTime());
                
                while (calendar.get(Calendar.DATE) > 1) {
                    calendar.add(Calendar.DATE, -1); // Substract 1 day until first day of month.
                }
                
                return calendar.getTime();
            }
        }
        
        /** {@inheritDoc} */
        @Override
        public Date moveDateToLastDayOfInterval(final Date date) {
            if (date == null) {
                return null;
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(date.getTime());
                
                final int month = calendar.get(getCalendarCode());
                
                while (calendar.get(getCalendarCode()) == month) {
                    calendar.add(Calendar.DATE, +1); // Add 1 day until reaching first day of next month.
                }
                
                calendar.add(Calendar.DATE, -1); //Subtract 1 day to go to the last day of the previous month
                
                return calendar.getTime();
            }
        }
    },
    
    /**
     * A year.
     */
    YEAR(Calendar.YEAR) {
        /** {@inheritDoc} */
        @Override
        public Date moveDateToFirstDayOfInterval(final Date date) {
            if (date == null) {
                return null;
                
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(date.getTime());
                
                while (calendar.get(Calendar.DAY_OF_YEAR) > 1) {
                    calendar.add(Calendar.DATE, -1); // Subtract 1 day until first day of year.
                }
                
                return calendar.getTime();
            }
        }
        
        /** {@inheritDoc} */
        @Override
        public Date moveDateToLastDayOfInterval(final Date date) {
            if (date == null) {
                return null;
                
            } else {
                final Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.setTimeInMillis(date.getTime());
                
                final int year = calendar.get(getCalendarCode());
                
                while (calendar.get(getCalendarCode()) == year) {
                    calendar.add(Calendar.DATE, +1); //add 1 day until first day of the next year.
                }
                
                calendar.add(Calendar.DATE, +1); //subtract 1 day to get to last day of previous year.
                
                return calendar.getTime();
            }
        }
        
    };
    
    /**
     * The code of the standard {@link Calendar} code repesenting this time unit.
     */
    private final int calendarCode;
    
    /**
     * @param calendarCode The code of the standard {@link Calendar} code repesenting this time unit.
     */
    private TimeUnit(final int calendarCode) {
        this.calendarCode = calendarCode;
    }
    
    /**
     * The code of the standard {@link Calendar} code repesenting this time unit.
     */
    public int getCalendarCode() {
        return calendarCode;
    }
    
    /**
     * Calculates the day that is the first day of the interval represented by the time unit
     * (e.g. the date representing the first day of a week)
     */
    public abstract Date moveDateToFirstDayOfInterval(final Date date);
    
    /**
     * Calculates the day that is the last day of the interval represented by the time unit
     * (e.g. the date representing the last day of a week)
     */
    public abstract Date moveDateToLastDayOfInterval(final Date date);
}
