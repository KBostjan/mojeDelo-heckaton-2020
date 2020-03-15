import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Heckaton {

    public static final String SPACE = " ";

    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("Ni vhodne datoteke!!");
            return;
        }
        String fileName = args[0];
        List<String> list = new ArrayList<>();
        List<LedDisplay> ledDisplays = readFile(fileName, list);

        System.out.println("Vhod:");
        write(ledDisplays, false);

        System.out.println();
        System.out.println("Izhod:");
        write(ledDisplays, true);
    }

    private static List<LedDisplay> readFile(String fileName, List<String> list) {
        List<LedDisplay> ledDisplays = new ArrayList<>();

        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            list = stream.collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s1 : list) {
            String[] line = s1.split(SPACE);
            LedDisplay ledDisplay = new LedDisplay();
            ledDisplay.setWidth(Integer.parseInt(line[0]));
            ledDisplay.setHeight(Integer.parseInt(line[1]));

            List<String> besede = new LinkedList<>();
            for(int i = 2; i < line.length; i++){
                besede.add(line[i]);
                besede.add(SPACE);
            }
            besede.remove(besede.size()-1);

            ledDisplay.setWords(besede);
            ledDisplay.setWordSize(getTextLength(ledDisplay));
            ledDisplays.add(ledDisplay);
        }
        return ledDisplays;
    }

    private static int getTextLength(LedDisplay ledDisplay) {
        return ledDisplay.getWords().stream().map(String::length).reduce(0, Integer::sum);
    }

    private static boolean isValid(LedDisplay ledDisplay, int pixels) {
        if (Math.sqrt(pixels) * ledDisplay.getWordSize() > ledDisplay.getWidth() * ledDisplay.getHeight()) {
            return false;
        }

        int line_length = 0;
        int row = 0;
        for (String word : ledDisplay.getWords()) {
            int len = word.length() + 1;

            if (line_length + word.length() * pixels <= ledDisplay.getWidth()) {
                line_length += word.length() * pixels;
            } else {
                line_length = 0;
                if(!word.equals(SPACE)){
                    line_length = word.length() * pixels;
                }
                row++;
            }
        }

        return (row + 1) * pixels <= ledDisplay.getHeight();
    }

    private static int solution(LedDisplay ledDisplay) {
        int pixels = ledDisplay.maxWidth();
        int high = pixels;
        int low = 0;
        int max = 0;

        if (isValid(ledDisplay, pixels)) {
            return pixels;
        }

        while (pixels > 1) {
            int temp;
            if (isValid(ledDisplay, pixels)) {
                if (pixels == max) {
                    return max;
                } else if (pixels > max) {
                    max = pixels;
                }
                temp = pixels;
                pixels = (high + pixels) / 2;
                low = temp;
            } else {
                temp = pixels;
                pixels = (low + pixels) / 2;
                high = temp;
            }
        }
        return 0;
    }

    private static void write(List<LedDisplay> ledDisplays, boolean isSolution) {
        for (LedDisplay ledDisplay : ledDisplays) {
            if(isSolution){
                System.out.println(solution(ledDisplay));
            }else{
                System.out.println(ledDisplay);
            }
        }
    }

}
