package cn.appoa.aframework.widget.side;

import android.text.TextUtils;

import java.util.Comparator;

/**
 * 排序
 */
public class PinyinComparator implements Comparator<Sort> {

    public int compare(Sort s1, Sort s2) {

        if (TextUtils.equals(s1.getInitialLetter(), "#")) {
            return 1;
        } else if (TextUtils.equals(s2.getInitialLetter(), "#")) {
            return -1;
        } else {
            return s1.getInitialLetter().compareTo(s2.getInitialLetter());
        }
    }

}