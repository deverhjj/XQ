package com.binfenjiari.widget.calendar;


public interface DayViewDecorator {
    boolean shouldDecorate(CalendarDay day);

    void decorate(DayView view);
}
