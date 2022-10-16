package ua.yuriizhurakovskyi.citiestask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static ua.yuriizhurakovskyi.citiestask.ReadDataUtils.readFile;

public class Application {

    private static final Logger LOGGER = LogManager.getLogger(Application.class);
    public static void main(String[] args) {
        String file = "src/main/resources/file.txt";
        LOGGER.info(String.join("\n", readFile(file)));

        CityTreeDataStructure cityTreeDataStructure = new CityTreeDataStructure("World");

        List<String> lines = readFile(file);
        cityTreeDataStructure.fillDataSourceStructure(cityTreeDataStructure, lines);

        cityTreeDataStructure.print();
    }
}
