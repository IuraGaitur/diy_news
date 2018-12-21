package video.paxra.com.videoconverter.models;



public class Line {
  public int position;
  public int height;
  public String text;

  public Line(int position, int height, String text) {
    this.position = position;
    this.height = height;
    this.text = text;
  }

  @Override public String toString() {
    return "Line{" + "position=" + position + ", height=" + height + ", text='" + text + '\'' + '}';
  }
}
