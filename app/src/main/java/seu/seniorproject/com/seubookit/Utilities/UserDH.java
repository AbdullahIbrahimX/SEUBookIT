package seu.seniorproject.com.seubookit.Utilities;

import android.content.Context;
import android.graphics.Color;

public class UserDH{
    private String  Name, Date, Purpose,filename,Reqormet, word,parentfile,kind,Time;
    private int bgColor;
    public UserDH() {
    }

    public UserDH(String Date, int bgColor,String word,String patrentfile,String Time) {
        this.Date = Date;
        this.bgColor = bgColor;
        this.word = word;
        this.parentfile = patrentfile;
        this.Time = Time;
    }

    public UserDH(String name, String date, String purpose, String reqormet, int bgcolor,String kind) {
        Name = name;
        Date = date;
        Purpose = purpose;
        Reqormet =reqormet;
        bgColor = bgcolor;
        this.kind = kind;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getReqormet() {
        return Reqormet;
    }

    public void setReqormet(String reqormet) {
        Reqormet = reqormet;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getParentfile() {
        return parentfile;
    }

    public void setParentfile(String parentfile) {
        this.parentfile = parentfile;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
