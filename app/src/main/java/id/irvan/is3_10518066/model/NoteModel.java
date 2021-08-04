package id.irvan.is3_10518066.model;

import java.io.Serializable;

public class NoteModel implements Serializable {
    public int id;
    public String title;
    public String desc;
    public String dateTime;


    public NoteModel() {

    }

    public NoteModel(int id, String title, String desc, String dateTime) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.dateTime = dateTime;
    }


}
