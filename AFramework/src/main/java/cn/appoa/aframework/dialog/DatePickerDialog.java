package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.appoa.aframework.R;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.widget.wheel.WheelView;
import cn.appoa.aframework.widget.wheel.adapter.ArrayWheelAdapter;
import cn.appoa.aframework.widget.wheel.listener.OnWheelChangedListener;

public class DatePickerDialog extends BaseDialog implements OnWheelChangedListener {

    /**
     * 类型，区分回调
     */
    private int type;
    /**
     * 开始时间，结束时间，选中时间
     */
    private Calendar mBeginTime, mEndTime, mSelectedTime;
    /**
     * 开始年月日时分，结束年月日时分
     */
    private int mBeginYear, mBeginMonth, mBeginDay, mBeginHour, mBeginMinute,
            mEndYear, mEndMonth, mEndDay, mEndHour, mEndMinute;
    /**
     * 年月日时分的数据集合
     */
    private List<String> mYearUnits = new ArrayList<>(),
            mMonthUnits = new ArrayList<>(), mDayUnits = new ArrayList<>(),
            mHourUnits = new ArrayList<>(), mMinuteUnits = new ArrayList<>();

    public DatePickerDialog(Context context, OnCallbackListener onCallbackListener, int type) {
        super(context, onCallbackListener);
        this.type = type;
    }

    /**
     * 是否是中文
     */
    private static boolean isZh;
    private TextView tv_dialog_title;
    private TextView tv_dialog_cancel;
    private TextView tv_dialog_confirm;
    private WheelView mDpvYear;
    private WheelView mDpvMonth;
    private WheelView mDpvDay;
    private WheelView mDpvHour;
    private WheelView mDpvMinute;

    @Override
    public Dialog initDialog(Context context) {
        isZh = Locale.getDefault().getLanguage().startsWith("zh");
        View view = View.inflate(context, R.layout.dialog_date_picker, null);
        tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        tv_dialog_confirm = (TextView) view.findViewById(R.id.tv_dialog_confirm);
        tv_dialog_cancel.setOnClickListener(this);
        tv_dialog_confirm.setOnClickListener(this);
        mDpvYear = (WheelView) view.findViewById(R.id.mWheelView1);
        mDpvMonth = (WheelView) view.findViewById(R.id.mWheelView2);
        mDpvDay = (WheelView) view.findViewById(R.id.mWheelView3);
        mDpvHour = (WheelView) view.findViewById(R.id.mWheelView4);
        mDpvMinute = (WheelView) view.findViewById(R.id.mWheelView5);
        //默认标签
        setLabel(isZh ? "年" : "Year", isZh ? "月" : "Month", isZh ? "日" : "Day",
                isZh ? "时" : "Hour", isZh ? "分" : "Minute");
        mDpvYear.addChangingListener(this);
        mDpvMonth.addChangingListener(this);
        mDpvDay.addChangingListener(this);
        mDpvHour.addChangingListener(this);
        mDpvMinute.addChangingListener(this);
        return initMatchWrapDialog(view, context, Gravity.BOTTOM, android.R.style.Animation_InputMethod);
    }

    /**
     * 是否可显示弹窗（设置数据后才可显示，即调用了initData）
     */
    private boolean mCanDialogShow = false;

    /**
     * 设置数据
     *
     * @param beginDateStr 日期字符串，格式为 yyyy-MM-dd HH:mm
     * @param endDateStr   日期字符串，格式为 yyyy-MM-dd HH:mm
     */
    public void initData(String beginDateStr, String endDateStr) {
        initData(str2Long(beginDateStr, mCanShowPreciseTime),
                str2Long(endDateStr, mCanShowPreciseTime));
    }

    /**
     * 设置数据
     *
     * @param beginTimestamp 开始时间（毫秒级时间戳）
     * @param endTimestamp   结束时间（毫秒级时间戳）
     */
    public void initData(long beginTimestamp, long endTimestamp) {
        mBeginTime = Calendar.getInstance();
        mBeginTime.setTimeInMillis(beginTimestamp);

        mEndTime = Calendar.getInstance();
        mEndTime.setTimeInMillis(endTimestamp);

        mSelectedTime = Calendar.getInstance();//默认选中开始时间
        mSelectedTime.setTimeInMillis(mBeginTime.getTimeInMillis());

        mBeginYear = mBeginTime.get(Calendar.YEAR);
        // Calendar.MONTH 值为 0-11
        mBeginMonth = mBeginTime.get(Calendar.MONTH) + 1;
        mBeginDay = mBeginTime.get(Calendar.DAY_OF_MONTH);
        mBeginHour = mBeginTime.get(Calendar.HOUR_OF_DAY);
        mBeginMinute = mBeginTime.get(Calendar.MINUTE);

        mEndYear = mEndTime.get(Calendar.YEAR);
        mEndMonth = mEndTime.get(Calendar.MONTH) + 1;
        mEndDay = mEndTime.get(Calendar.DAY_OF_MONTH);
        mEndHour = mEndTime.get(Calendar.HOUR_OF_DAY);
        mEndMinute = mEndTime.get(Calendar.MINUTE);

        boolean canSpanYear = mBeginYear != mEndYear;
        boolean canSpanMon = !canSpanYear && mBeginMonth != mEndMonth;
        boolean canSpanDay = !canSpanMon && mBeginDay != mEndDay;
        boolean canSpanHour = !canSpanDay && mBeginHour != mEndHour;
        boolean canSpanMinute = !canSpanHour && mBeginMinute != mEndMinute;
        if (canSpanYear) {
            initDateUnits(MAX_MONTH_UNIT, mBeginTime.getActualMaximum
                    (Calendar.DAY_OF_MONTH), MAX_HOUR_UNIT, MAX_MINUTE_UNIT);
        } else if (canSpanMon) {
            initDateUnits(mEndMonth, mBeginTime.getActualMaximum
                    (Calendar.DAY_OF_MONTH), MAX_HOUR_UNIT, MAX_MINUTE_UNIT);
        } else if (canSpanDay) {
            initDateUnits(mEndMonth, mEndDay, MAX_HOUR_UNIT, MAX_MINUTE_UNIT);
        } else if (canSpanHour) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, MAX_MINUTE_UNIT);
        } else if (canSpanMinute) {
            initDateUnits(mEndMonth, mEndDay, mEndHour, mEndMinute);
        }

        mCanDialogShow = true;
    }

    /**
     * 格式化两位数
     */
    private DecimalFormat mDecimalFormat = new DecimalFormat("00");

    /**
     * 初始化数据
     *
     * @param endMonth
     * @param endDay
     * @param endHour
     * @param endMinute
     */
    private void initDateUnits(int endMonth, int endDay, int endHour, int endMinute) {
        for (int i = mBeginYear; i <= mEndYear; i++) {
            mYearUnits.add(String.valueOf(i));
        }

        for (int i = mBeginMonth; i <= endMonth; i++) {
            mMonthUnits.add(mDecimalFormat.format(i));
        }

        for (int i = mBeginDay; i <= endDay; i++) {
            mDayUnits.add(mDecimalFormat.format(i));
        }

        if ((mScrollUnits & SCROLL_UNIT_HOUR) != SCROLL_UNIT_HOUR) {
            mHourUnits.add(mDecimalFormat.format(mBeginHour));
        } else {
            for (int i = mBeginHour; i <= endHour; i++) {
                mHourUnits.add(mDecimalFormat.format(i));
            }
        }

        if ((mScrollUnits & SCROLL_UNIT_MINUTE) != SCROLL_UNIT_MINUTE) {
            mMinuteUnits.add(mDecimalFormat.format(mBeginMinute));
        } else {
            for (int i = mBeginMinute; i <= endMinute; i++) {
                mMinuteUnits.add(mDecimalFormat.format(i));
            }
        }

        mDpvYear.setAdapter(new ArrayWheelAdapter<String>
                (context, mYearUnits.toArray(new String[mYearUnits.size()])));
        mDpvYear.setCurrentItem(0);
        mDpvMonth.setAdapter(new ArrayWheelAdapter<String>
                (context, mMonthUnits.toArray(new String[mMonthUnits.size()])));
        mDpvMonth.setCurrentItem(0);
        mDpvDay.setAdapter(new ArrayWheelAdapter<String>
                (context, mDayUnits.toArray(new String[mDayUnits.size()])));
        mDpvDay.setCurrentItem(0);
        mDpvHour.setAdapter(new ArrayWheelAdapter<String>
                (context, mHourUnits.toArray(new String[mHourUnits.size()])));
        mDpvHour.setCurrentItem(0);
        mDpvMinute.setAdapter(new ArrayWheelAdapter<String>
                (context, mMinuteUnits.toArray(new String[mMinuteUnits.size()])));
        mDpvMinute.setCurrentItem(0);

    }

    private int mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE;

    /**
     * 时间单位：时、分
     */
    private static final int SCROLL_UNIT_HOUR = 0b1;
    private static final int SCROLL_UNIT_MINUTE = 0b10;

    /**
     * 时间单位的最大显示值
     */
    private static final int MAX_MINUTE_UNIT = 59;
    private static final int MAX_HOUR_UNIT = 23;
    private static final int MAX_MONTH_UNIT = 12;

    /**
     * 级联滚动延迟时间
     */
    private static final long LINKAGE_DELAY_DEFAULT = 100L;

    /**
     * 是否显示时和分
     */
    private boolean mCanShowPreciseTime = false;

    /**
     * 设置日期控件是否显示时和分
     */
    public void setCanShowPreciseTime(boolean canShowPreciseTime) {
        if (canShowPreciseTime) {
            initScrollUnit();
            mDpvHour.setVisibility(View.VISIBLE);
            mDpvMinute.setVisibility(View.VISIBLE);
        } else {
            initScrollUnit(SCROLL_UNIT_HOUR, SCROLL_UNIT_MINUTE);
            mDpvHour.setVisibility(View.GONE);
            mDpvMinute.setVisibility(View.GONE);
        }
        mCanShowPreciseTime = canShowPreciseTime;
    }

    private void initScrollUnit(Integer... units) {
        if (units == null || units.length == 0) {
            mScrollUnits = SCROLL_UNIT_HOUR + SCROLL_UNIT_MINUTE;
        } else {
            for (int unit : units) {
                mScrollUnits ^= unit;
            }
        }
    }

    /**
     * 设置日期控件是否显示分（值得注意的是：即使隐藏分钟控件，传入格式也是yyyy-MM-dd HH:mm）
     *
     * @param canShowMinuteTime
     */
    public void setCanShowMinuteTime(boolean canShowMinuteTime) {
        if (canShowMinuteTime) {
            //显示分钟必然显示小时
            setCanShowPreciseTime(true);
        } else {
            mDpvMinute.setVisibility(View.GONE);
        }
    }

    /**
     * 显示弹窗
     *
     * @param title
     * @param dateStr 日期字符串，格式为 yyyy-MM-dd 或 yyyy-MM-dd HH:mm
     */
    public void showDatePickerDialog(String title, String dateStr) {
        showDatePickerDialog(title, str2Long(dateStr, mCanShowPreciseTime));
    }

    /**
     * 显示弹窗
     *
     * @param title
     * @param timestamp 时间戳，毫秒级别
     */
    public void showDatePickerDialog(String title, long timestamp) {
        tv_dialog_title.setText(title);
        if (mCanDialogShow && setSelectedTime(timestamp)) {
            showDialog();
        }
    }

    /**
     * 设置日期选择器的选中时间
     *
     * @param timestamp 毫秒级时间戳
     * @return 是否设置成功
     */
    public boolean setSelectedTime(long timestamp) {
        if (timestamp < mBeginTime.getTimeInMillis()) {
            timestamp = mBeginTime.getTimeInMillis();
        } else if (timestamp > mEndTime.getTimeInMillis()) {
            timestamp = mEndTime.getTimeInMillis();
        }
        mSelectedTime.setTimeInMillis(timestamp);

        mYearUnits.clear();
        for (int i = mBeginYear; i <= mEndYear; i++) {
            mYearUnits.add(String.valueOf(i));
        }
        mDpvYear.setAdapter(new ArrayWheelAdapter<String>
                (context, mYearUnits.toArray(new String[mYearUnits.size()])));
        mDpvYear.setCurrentItem(mSelectedTime.get(Calendar.YEAR) - mBeginYear);
        linkageMonthUnit();
        return true;
    }

    /**
     * 获得当前选中下标
     *
     * @param index
     * @return
     */
    public int getCurrentItem(int index) {
        int position = 0;
        if (index == 1) {
            position = mDpvYear.getCurrentItem();
        } else if (index == 2) {
            position = mDpvMonth.getCurrentItem();
        } else if (index == 3) {
            position = mDpvDay.getCurrentItem();
        } else if (index == 4) {
            position = mDpvHour.getCurrentItem();
        } else if (index == 5) {
            position = mDpvMinute.getCurrentItem();
        }
        return position;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_dialog_confirm) {
            // 确定
            if (onCallbackListener != null) {
                onCallbackListener.onCallback(type, long2Str(
                        mSelectedTime.getTimeInMillis(), mCanShowPreciseTime), mSelectedTime);
            }
        } else if (id == R.id.tv_dialog_cancel) {
            // 取消
        }
        dismissDialog();
    }

    /**
     * 设置标签
     *
     * @param Year
     * @param Month
     * @param Day
     * @param Hour
     * @param Minute
     */
    public void setLabel(String Year, String Month, String Day, String Hour, String Minute) {
        mDpvYear.setLabel(Year);
        mDpvMonth.setLabel(Month);
        mDpvDay.setLabel(Day);
        mDpvHour.setLabel(Hour);
        mDpvMinute.setLabel(Minute);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        int id = wheel.getId();
        if (id == R.id.mWheelView1) {
            mSelectedTime.set(Calendar.YEAR, Integer.parseInt(mYearUnits.get(newValue)));
            linkageMonthUnit();
        } else if (id == R.id.mWheelView2) {
            // 防止类似 2018/12/31 滚动到11月时因溢出变成 2018/12/01
            int lastSelectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
            mSelectedTime.add(Calendar.MONTH, Integer.parseInt(
                    mMonthUnits.get(newValue)) - lastSelectedMonth);
            linkageDayUnit();
        } else if (id == R.id.mWheelView3) {
            mSelectedTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mDayUnits.get(newValue)));
            linkageHourUnit();
        } else if (id == R.id.mWheelView4) {
            mSelectedTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(mHourUnits.get(newValue)));
            linkageMinuteUnit();
        } else if (id == R.id.mWheelView5) {
            mSelectedTime.set(Calendar.MINUTE, Integer.parseInt(mMinuteUnits.get(newValue)));
        }
    }

    /**
     * 联动“月”变化
     */
    private void linkageMonthUnit() {
        int minMonth;
        int maxMonth;
        int selectedYear = mSelectedTime.get(Calendar.YEAR);
        if (mBeginYear == mEndYear) {
            minMonth = mBeginMonth;
            maxMonth = mEndMonth;
        } else if (selectedYear == mBeginYear) {
            minMonth = mBeginMonth;
            maxMonth = MAX_MONTH_UNIT;
        } else if (selectedYear == mEndYear) {
            minMonth = 1;
            maxMonth = mEndMonth;
        } else {
            minMonth = 1;
            maxMonth = MAX_MONTH_UNIT;
        }

        // 重新初始化时间单元容器
        mMonthUnits.clear();
        for (int i = minMonth; i <= maxMonth; i++) {
            mMonthUnits.add(mDecimalFormat.format(i));
        }
        mDpvMonth.setAdapter(new ArrayWheelAdapter<String>
                (context, mMonthUnits.toArray(new String[mMonthUnits.size()])));

        // 确保联动时不会溢出或改变关联选中值
        int selectedMonth = getValueInRange(mSelectedTime.get(Calendar.MONTH) + 1, minMonth, maxMonth);
        mSelectedTime.set(Calendar.MONTH, selectedMonth - 1);
        mDpvMonth.setCurrentItem(selectedMonth - minMonth);

        // 联动“日”变化
        mDpvMonth.postDelayed(new Runnable() {
            @Override
            public void run() {
                linkageDayUnit();
            }
        }, LINKAGE_DELAY_DEFAULT);
    }

    /**
     * 联动“日”变化
     */
    private void linkageDayUnit() {
        int minDay;
        int maxDay;
        int selectedYear = mSelectedTime.get(Calendar.YEAR);
        int selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
        if (mBeginYear == mEndYear && mBeginMonth == mEndMonth) {
            minDay = mBeginDay;
            maxDay = mEndDay;
        } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth) {
            minDay = mBeginDay;
            maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
        } else if (selectedYear == mEndYear && selectedMonth == mEndMonth) {
            minDay = 1;
            maxDay = mEndDay;
        } else {
            minDay = 1;
            maxDay = mSelectedTime.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        mDayUnits.clear();
        for (int i = minDay; i <= maxDay; i++) {
            mDayUnits.add(mDecimalFormat.format(i));
        }
        mDpvDay.setAdapter(new ArrayWheelAdapter<String>
                (context, mDayUnits.toArray(new String[mDayUnits.size()])));

        int selectedDay = getValueInRange(mSelectedTime.get(Calendar.DAY_OF_MONTH), minDay, maxDay);
        mSelectedTime.set(Calendar.DAY_OF_MONTH, selectedDay);
        mDpvDay.setCurrentItem(selectedDay - minDay);

        mDpvDay.postDelayed(new Runnable() {
            @Override
            public void run() {
                linkageHourUnit();
            }
        }, LINKAGE_DELAY_DEFAULT);
    }

    /**
     * 联动“时”变化
     */
    private void linkageHourUnit() {
        if ((mScrollUnits & SCROLL_UNIT_HOUR) == SCROLL_UNIT_HOUR) {
            int minHour;
            int maxHour;
            int selectedYear = mSelectedTime.get(Calendar.YEAR);
            int selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
            int selectedDay = mSelectedTime.get(Calendar.DAY_OF_MONTH);
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth && mBeginDay == mEndDay) {
                minHour = mBeginHour;
                maxHour = mEndHour;
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth && selectedDay == mBeginDay) {
                minHour = mBeginHour;
                maxHour = MAX_HOUR_UNIT;
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth && selectedDay == mEndDay) {
                minHour = 0;
                maxHour = mEndHour;
            } else {
                minHour = 0;
                maxHour = MAX_HOUR_UNIT;
            }

            mHourUnits.clear();
            for (int i = minHour; i <= maxHour; i++) {
                mHourUnits.add(mDecimalFormat.format(i));
            }
            mDpvHour.setAdapter(new ArrayWheelAdapter<String>
                    (context, mHourUnits.toArray(new String[mHourUnits.size()])));

            int selectedHour = getValueInRange(mSelectedTime.get(Calendar.HOUR_OF_DAY), minHour, maxHour);
            mSelectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
            mDpvHour.setCurrentItem(selectedHour - minHour);
        }

        mDpvHour.postDelayed(new Runnable() {
            @Override
            public void run() {
                linkageMinuteUnit();
            }
        }, LINKAGE_DELAY_DEFAULT);
    }

    /**
     * 联动“分”变化
     */
    private void linkageMinuteUnit() {
        if ((mScrollUnits & SCROLL_UNIT_MINUTE) == SCROLL_UNIT_MINUTE) {
            int minMinute;
            int maxMinute;
            int selectedYear = mSelectedTime.get(Calendar.YEAR);
            int selectedMonth = mSelectedTime.get(Calendar.MONTH) + 1;
            int selectedDay = mSelectedTime.get(Calendar.DAY_OF_MONTH);
            int selectedHour = mSelectedTime.get(Calendar.HOUR_OF_DAY);
            if (mBeginYear == mEndYear && mBeginMonth == mEndMonth && mBeginDay == mEndDay && mBeginHour == mEndHour) {
                minMinute = mBeginMinute;
                maxMinute = mEndMinute;
            } else if (selectedYear == mBeginYear && selectedMonth == mBeginMonth && selectedDay == mBeginDay && selectedHour == mBeginHour) {
                minMinute = mBeginMinute;
                maxMinute = MAX_MINUTE_UNIT;
            } else if (selectedYear == mEndYear && selectedMonth == mEndMonth && selectedDay == mEndDay && selectedHour == mEndHour) {
                minMinute = 0;
                maxMinute = mEndMinute;
            } else {
                minMinute = 0;
                maxMinute = MAX_MINUTE_UNIT;
            }

            mMinuteUnits.clear();
            for (int i = minMinute; i <= maxMinute; i++) {
                mMinuteUnits.add(mDecimalFormat.format(i));
            }
            mDpvMinute.setAdapter(new ArrayWheelAdapter<String>
                    (context, mMinuteUnits.toArray(new String[mMinuteUnits.size()])));

            int selectedMinute = getValueInRange(mSelectedTime.get(Calendar.MINUTE), minMinute, maxMinute);
            mSelectedTime.set(Calendar.MINUTE, selectedMinute);
            mDpvMinute.setCurrentItem(selectedMinute - minMinute);
        }
    }

    private int getValueInRange(int value, int minValue, int maxValue) {
        if (value < minValue) {
            return minValue;
        } else if (value > maxValue) {
            return maxValue;
        } else {
            return value;
        }
    }

    /**
     * 获取年龄
     *
     * @param year
     * @return
     */
    public int getAge(int year) {
        int age = 0;
        int mYear = Calendar.getInstance().get(Calendar.YEAR);//今年
        if (mYear >= year) {
            age = mYear = year;
        }
        return age;
    }

    /**
     * 获取星期几
     *
     * @param calendar
     * @return
     */
    public static String getWeek(Calendar calendar) {
        String week = "";
        if (calendar != null) {
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
        }
        return week;
    }

    /**
     * 获取星座
     *
     * @param calendar
     * @return
     */
    public static String getConstellation(Calendar calendar) {
        String constellation = "";
        if (calendar != null) {
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座",
                    "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
            String[] astro2 = new String[]{"Capricorn", "Aquarius", "Pisces", "Aries", "Taurus", "Gemini",
                    "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"};
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

    //========================================时间工具类start========================================
    private static final String DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd";
    private static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */
    public static String long2Str(long timestamp, boolean isPreciseTime) {
        return long2Str(timestamp, getFormatPattern(isPreciseTime));
    }

    /**
     * 时间戳转字符串
     *
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String long2Str(long timestamp, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr       日期字符串
     * @param isPreciseTime 是否包含时分
     * @return 时间戳
     */
    public static long str2Long(String dateStr, boolean isPreciseTime) {
        return str2Long(dateStr, getFormatPattern(isPreciseTime));
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static long str2Long(String dateStr, String pattern) {
        try {
            return new SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).getTime();
        } catch (Throwable ignored) {
        }
        return 0;
    }

    /**
     * 格式化
     *
     * @param showSpecificTime
     * @return
     */
    private static String getFormatPattern(boolean showSpecificTime) {
        if (showSpecificTime) {
            return DATE_FORMAT_PATTERN_YMD_HM;
        } else {
            return DATE_FORMAT_PATTERN_YMD;
        }
    }
    //========================================时间工具类end========================================
}
