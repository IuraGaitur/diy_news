package video.paxra.com.videoconverter.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class Answer implements Serializable {

  public int id;

  public int to;
  public int from;

  public int fontSize;

  public int xPos;
  public int yPos;

  public int height;

  public List<Line> splittedText;

  public String type;
  public String answer;


  public Answer(int id, String answer, String type) {
    this.id = id;
    this.answer = answer.replace(" ", "^*");
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


  public int getFontSize() {
    return fontSize;
  }

  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }

  public int getxPos() {
    return xPos;
  }

  public void setxPos(int xPos) {
    this.xPos = xPos;
  }

  public int getyPos() {
    return yPos;
  }

  public void setyPos(int yPos) {
    this.yPos = yPos;
  }

  @Override public String toString() {
    return "Answer{"
        + "id="
        + id
        + ", to="
        + to
        + ", from="
        + from
        + ", fontSize="
        + fontSize
        + ", xPos="
        + xPos
        + ", yPos="
        + yPos
        + ", height="
        + height
        + ", splittedText="
        + splittedText
        + ", type='"
        + type
        + '\''
        + ", answer='"
        + answer
        + '\''
        + '}';
  }
}
