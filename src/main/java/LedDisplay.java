import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class LedDisplay {
    private int width;
    private int height;
    private List<String> words;
    private int wordSize;

    //Metoda vrne najdaljso besedo iz seznama
    public String longestWord(){
        return Collections.max(getWords(), Comparator.comparing(String::length));
    }

    //Metoda vrne najvecjo velikost, ki jo zasede ena crka
    public int maxWidth(){
        final double v1 = getHeight() / Math.ceil((float)wordSize / getWidth());
        final int v2 = getWidth() / longestWord().length();
        return (int) Math.min(v1,v2);
    }

    @Override
    public String toString() {
        return width + " " + height + " " + listToString(words);
    }

    private String listToString(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : list) {
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }
}
