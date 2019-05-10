package cn.appoa.aframework.widget.side;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.appoa.aframework.widget.side.HanziToPinyin.Token;

public class Sort implements Serializable {

    /**
     * 文件头
     */
    private static final long serialVersionUID = 1L;
    public String name;
    private String initialLetter;// 首字母

    public Sort() {
        super();
    }

    public Sort(String name) {
        super();
        this.name = name;
    }

    public String getInitialLetter() {
        if (initialLetter == null) {
            setInitialLetter(this);
        }
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    private void setInitialLetter(Sort s) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;
        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0) {
                    Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }
        if (s != null) {
            letter = new GetInitialLetter().getLetter(s.name);
        }
        setInitialLetter(letter);
    }

    private Map<String, Object> customInfo = new HashMap<>();

    public Object getCustomInfo(String key) {
        return customInfo.get(key);
    }

    public void setCustomInfo(String key, Object value) {
        customInfo.put(key, value);
    }

}
