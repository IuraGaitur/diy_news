package video.paxra.com.videoconverter.models;

import java.io.Serializable;

/**
 * Created by crove on 09.10.2016.
 */

public class Answer implements Serializable{
    public int id;
    public String answer;
    public int from;
    public int to;
    public String type;

    public Answer(int id, String answer, String type) {
        this.id = id;
        this.answer = answer;
        this.type = type;
    }

    public Answer(int id, String answer, int from, int to, String type) {
        this.id = id;
        this.answer = answer;
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", type='" + type + '\'' +
                '}';
    }
}
