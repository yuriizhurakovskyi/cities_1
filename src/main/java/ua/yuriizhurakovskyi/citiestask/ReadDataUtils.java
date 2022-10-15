package ua.yuriizhurakovskyi.citiestask;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadDataUtils {
    public static List<String> readFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            Scanner sc = new Scanner(fis);
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static List<String> receiveDataListFromLine(String fileLine) {
        return Arrays.stream(fileLine.split("\\|")).filter(str -> !str.contains("&#124;\n")).collect(Collectors.toList());
    }
}
