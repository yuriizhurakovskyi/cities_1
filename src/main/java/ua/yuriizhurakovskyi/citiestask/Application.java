package ua.yuriizhurakovskyi.citiestask;

import java.util.List;

import static ua.yuriizhurakovskyi.citiestask.ReadDataUtils.readFile;

public class Application {
    public static void main(String[] args) {
        String file = "src/main/resources/file.txt";
        System.out.println(String.join("\n", readFile(file)));

        TreeDataStructure treeDataStructure = new TreeDataStructure("World");

        List<String> lines = readFile(file);
        treeDataStructure.fillDataSourceStructure(treeDataStructure, lines);

        treeDataStructure.print();
    }
}
