package cn.appoa.aframework.widget.wheel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.appoa.aframework.widget.wheel.adapter.NumericWheelAdapter;
import cn.appoa.aframework.widget.wheel.listener.OnWheelChangedListener;

/**
 * 日期滚轮
 *
 * @deprecated Use {@link cn.appoa.aframework.dialog.DatePickerDialog} instead.
 *
 */
public class DateWheelView extends LinearLayout {

    public DateWheelView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public DateWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DateWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    private Context context;
    public WheelView yearWheelView;
    public WheelView monthWheelView;
    public WheelView dayWheelView;

    /**
     * 默认年
     */
    private int yearToday;

    /**
     * 默认月
     */
    private int monthToday;

    /**
     * 默认天
     */
    private int dayToday;

    /**
     * 初始年份，今年
     */
    private int year;

    /**
     * 初始年份，今年前25年
     */
    private int startYear;

    /**
     * 结束年份，今年后25年
     */
    private int endYear;

    /**
     * 是否是中文
     */
    private boolean isZh;

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        isZh = Locale.getDefault().getLanguage().startsWith("zh");
        this.context = context;
        yearWheelView = new WheelView(context, attrs, defStyleAttr);
        monthWheelView = new WheelView(context, attrs, defStyleAttr);
        dayWheelView = new WheelView(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        addView(yearWheelView, 0, params);
        addView(monthWheelView, 1, params);
        addView(dayWheelView, 2, params);
        yearToday = Calendar.getInstance().get(Calendar.YEAR);
        monthToday = Calendar.getInstance().get(Calendar.MONTH) + 1;
        dayToday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        setYear(yearToday, monthToday, dayToday);
    }

    /**
     * 31天的月份
     */
    private final String[] monthsBig = {"1", "3", "5", "7", "8", "10", "12"};

    /**
     * 30天的月份
     */
    private final String[] monthsLittle = {"4", "6", "9", "11"};

    /**
     * 31天的月份
     */
    private final List<String> listBig = Arrays.asList(monthsBig);

    /**
     * 30天的月份
     */
    private final List<String> listLittle = Arrays.asList(monthsLittle);

    /**
     * 是否截止到今天
     */
    private boolean isToday;

    /**
     * 设置今天是最后一天
     */
    public void setToday(boolean isToday, int startYear) {
        this.isToday = isToday;
        if (isToday) {
            this.yearToday = Calendar.getInstance().get(Calendar.YEAR);
            this.monthToday = Calendar.getInstance().get(Calendar.MONTH) + 1;
            this.dayToday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            setYear(yearToday, startYear, yearToday, monthToday, dayToday);
        } else {
            setYear(yearToday, startYear, yearToday + 25, monthToday, dayToday);
        }
    }

    /**
     * 是否今天是第一天
     */
    private boolean isAfterNow;

    public void setAfterNow(boolean isAfterNow) {
        this.isAfterNow = isAfterNow;
    }

    private int nowMonth;
    private int nowday;

    /**
     * 设置年份
     */
    public void setYear(int year, int month, int day) {
        this.year = year;
        this.startYear = yearToday - 25;
        this.endYear = yearToday + 25;
        this.nowMonth = month;
        this.nowday = day;
        setDate(year, month, day);
    }

    /**
     * 设置年份
     */
    public void setYear(int year, int startYear, int endYear, int month, int day) {
        this.year = year;
        this.startYear = startYear;
        this.endYear = endYear;
        this.nowMonth = month;
        this.nowday = day;
        setDate(year, month, day);
    }

    /**
     * 设置当前日期
     */
    private void setDate(int year, int month, int day) {
        // 日期
        if (day > 0 && day < 32) {
            dayWheelView.setVisibility(View.VISIBLE);
            dayWheelView.setLabel(isZh ? "日" : "Day");
            // dayWheelView.setCyclic(true);
            dayWheelView.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {

                }
            });
            setDayAdapter(year, month);
            if (isAfterNow) {
                dayWheelView.setCurrentItem(0);
            } else {
                dayWheelView.setCurrentItem(day - 1);
            }
        } else {
            dayWheelView.setVisibility(View.GONE);
        }
        // 月份
        if (month > 0 && month < 13) {
            monthWheelView.setVisibility(View.VISIBLE);
            monthWheelView.setLabel(isZh ? "月" : "Month");
            // monthWheelView.setCyclic(true);
            monthWheelView.addChangingListener(new OnWheelChangedListener() {

                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    setDayAdapter(getYear(), getMonth());
                    if (isAfterNow && getYear() == DateWheelView.this.year
                            && getMonth() == DateWheelView.this.nowMonth) {
                        dayWheelView.setCurrentItem(0);
                    }
                }
            });
            setMonthAdapter(year);
            if (isAfterNow) {
                monthWheelView.setCurrentItem(0);
            } else {
                monthWheelView.setCurrentItem(month - 1);
            }
        } else {
            monthWheelView.setVisibility(View.GONE);
        }
        // 年份
        if (year > 0 && year >= startYear && year <= endYear) {
            yearWheelView.setVisibility(View.VISIBLE);
            yearWheelView.setLabel(isZh ? "年" : "Year");
            // yearWheelView.setCyclic(true);
            yearWheelView.addChangingListener(new OnWheelChangedListener() {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue) {
                    setMonthAdapter(getYear());
                    if (isAfterNow && getYear() == DateWheelView.this.year) {
                        monthWheelView.setCurrentItem(0);
                    }
                    setDayAdapter(getYear(), getMonth());
                    if (isAfterNow && getYear() == DateWheelView.this.year
                            && getMonth() == DateWheelView.this.nowMonth) {
                        dayWheelView.setCurrentItem(0);
                    }
                }
            });
            yearWheelView.setAdapter(new NumericWheelAdapter(startYear, endYear));
            if (isAfterNow) {
                yearWheelView.setCurrentItem(0);
            } else {
                yearWheelView.setCurrentItem(year - startYear);
            }
        } else if (year == 0) {
            yearWheelView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置月份
     */
    private void setMonthAdapter(int year) {
        if (isToday && year == Calendar.getInstance().get(Calendar.YEAR)) {
            if (getMonth() > Calendar.getInstance().get(Calendar.MONTH) + 1) {
                monthWheelView.setCurrentItem(Calendar.getInstance().get(Calendar.MONTH));
            }
            monthWheelView.setAdapter(new NumericWheelAdapter(1, Calendar.getInstance().get(Calendar.MONTH) + 1));
        } else {
            if (isAfterNow && year == DateWheelView.this.year) {
                monthWheelView.setAdapter(new NumericWheelAdapter(nowMonth, 12));
            } else {
                monthWheelView.setAdapter(new NumericWheelAdapter(1, 12));
            }
        }
    }

    /**
     * 设置日期
     */
    private void setDayAdapter(int year, int month) {
        if (isToday && year == Calendar.getInstance().get(Calendar.YEAR)
                && month == Calendar.getInstance().get(Calendar.MONTH) + 1) {
            if (getDay() > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                dayWheelView.setCurrentItem(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1);
            }
            dayWheelView.setAdapter(new NumericWheelAdapter(1, Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        } else {
            if (isAfterNow && year == DateWheelView.this.year && month == DateWheelView.this.nowMonth) {
                if (listBig.contains(String.valueOf(month))) {
                    dayWheelView.setAdapter(new NumericWheelAdapter(nowday, 31));
                } else if (listLittle.contains(String.valueOf(month))) {
                    if (getDay() > 30) {
                        dayWheelView.setCurrentItem(29);
                    }
                    dayWheelView.setAdapter(new NumericWheelAdapter(nowday, 30));
                } else {
                    if (((year) % 4 == 0 && (year) % 100 != 0) || (year) % 400 == 0) {
                        if (getDay() > 29) {
                            dayWheelView.setCurrentItem(28);
                        }
                        dayWheelView.setAdapter(new NumericWheelAdapter(nowday, 29));
                    } else {
                        if (getDay() > 28) {
                            dayWheelView.setCurrentItem(27);
                        }
                        dayWheelView.setAdapter(new NumericWheelAdapter(nowday, 28));
                    }
                }
            } else {
                if (listBig.contains(String.valueOf(month))) {
                    dayWheelView.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (listLittle.contains(String.valueOf(month))) {
                    if (getDay() > 30) {
                        dayWheelView.setCurrentItem(29);
                    }
                    dayWheelView.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((year) % 4 == 0 && (year) % 100 != 0) || (year) % 400 == 0) {
                        if (getDay() > 29) {
                            dayWheelView.setCurrentItem(28);
                        }
                        dayWheelView.setAdapter(new NumericWheelAdapter(1, 29));
                    } else {
                        if (getDay() > 28) {
                            dayWheelView.setCurrentItem(27);
                        }
                        dayWheelView.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                }
            }
        }
    }

    /**
     * 获取年份
     *
     * @return
     */
    public int getYear() {
        int year = 0;
        if (yearWheelView.getVisibility() != View.GONE) {
            year = yearWheelView.getCurrentItem() + startYear;
        }
        return year;
    }

    /**
     * 获取月份
     *
     * @return
     */
    public int getMonth() {
        int month = 0;
        if (monthWheelView.getVisibility() != View.GONE) {
            if (isAfterNow && getYear() == DateWheelView.this.year) {
                month = monthWheelView.getCurrentItem() + nowMonth;
            } else {
                month = monthWheelView.getCurrentItem() + 1;
            }
        }
        return month;
    }

    /**
     * 获取日期
     *
     * @return
     */
    public int getDay() {
        int day = 0;
        if (dayWheelView.getVisibility() != View.GONE) {
            if (isAfterNow && getYear() == DateWheelView.this.year && getMonth() == nowMonth) {
                day = dayWheelView.getCurrentItem() + nowday;
            } else {
                day = dayWheelView.getCurrentItem() + 1;
            }
        }
        return day;
    }

    /**
     * 获取年龄
     *
     * @return
     */
    public int getAge() {
        int age = 0;
        if (yearWheelView.getVisibility() != View.GONE) {
            age = Calendar.getInstance().get(Calendar.YEAR) - getYear();
        }
        return age;
    }

    /**
     * 获取星期几
     *
     * @return
     */
    public String getWeek() {
        String week = "";
        Calendar calendar = Calendar.getInstance();
        calendar.set(getYear(), getMonth() - 1, getDay());
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                week = isZh ? "星期日" : "Sunday";
                break;
            case 2:
                week = isZh ? "星期一" : "Monday";
                break;
            case 3:
                week = isZh ? "星期二" : "Tuesday";
                break;
            case 4:
                week = isZh ? "星期三" : "Wednesday";
                break;
            case 5:
                week = isZh ? "星期四" : "Thursday";
                break;
            case 6:
                week = isZh ? "星期五" : "Friday";
                break;
            case 7:
                week = isZh ? "星期六" : "Saturday";
                break;
            default:
                week = "";
                break;
        }
        return week;
    }

    /**
     * 获取星座
     *
     * @return
     */
    public String getConstellation() {
        String constellation = "";
        if (monthWheelView.getVisibility() != View.GONE && dayWheelView.getVisibility() != View.GONE) {
            int month = getMonth();
            int day = getDay();
            String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座",
                    "射手座", "摩羯座"};
            String[] astro2 = new String[]{"Capricorn", "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra",
                    "Scorpio", "Sagittarius", "Capricorn"};
            int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
            int index = month;
            // 所查询日期在分割日之前，索引-1，否则不变
            if (day < arr[month - 1]) {
                index = index - 1;
            }
            constellation = isZh ? astro[index] : astro2[index];
        }
        return constellation;
    }

}
