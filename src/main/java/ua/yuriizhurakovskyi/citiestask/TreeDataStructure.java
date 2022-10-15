package ua.yuriizhurakovskyi.citiestask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ua.yuriizhurakovskyi.citiestask.ReadDataUtils.receiveDataListFromLine;

public class TreeDataStructure {
    private final String name;
    private final List<TreeDataStructure> children = new ArrayList<>();
    private TreeDataStructure father;
    private static final Logger LOGGER = LogManager.getLogger(TreeDataStructure.class);

    public TreeDataStructure(String name) {
        this.name = name;
    }

    public TreeDataStructure(String name, TreeDataStructure father) {
        this.name = name;
        this.father = father;
    }

    public boolean isRoot() {
        return father == null;
    }

    public TreeDataStructure addChild(String name, TreeDataStructure father) {
        TreeDataStructure child = new TreeDataStructure(name, father);
        father.children.add(child);
        return child;
    }

    public void print() {
        if (isRoot()) {
            System.out.println("ROOT: " + name);
            System.out.println(children.stream().map(continent -> "Continent: " + continent.name + ", " + getCountries(continent.children)).collect(Collectors.joining("; \n")));
        }
    }

    public void fillDataSourceStructure(TreeDataStructure fatherNode, List<String> fileLines) {
        for (String fileLine : fileLines) {
            List<String> dataFromLine = receiveDataListFromLine(fileLine);
            String continentStr = dataFromLine.get(2);
            TreeDataStructure continent = createNewContinentNode(continentStr, fatherNode);
            String countryStr = dataFromLine.get(1);
            TreeDataStructure country = createNewCountryNode(countryStr, continent);
            String cityStr = dataFromLine.get(0);
            TreeDataStructure city = createNewCityNode(cityStr, country);
        }
    }

    public boolean checkIfContinentExists(String continentStr, TreeDataStructure worldNode) {
        return worldNode.children.stream().anyMatch(continent -> continent.name.equals(continentStr));
    }

    public boolean checkIfCountryExists(String countryStr, TreeDataStructure continentNode) {
        return continentNode.father.children.stream().map(continent -> continent.children).anyMatch(countries -> countries.stream().anyMatch(countryNode -> countryNode.name.equals(countryStr)));
    }

    public boolean checkIfCityExists(String cityStr, TreeDataStructure countryNode) {
        return countryNode.father.children.stream().map(country -> country.children).anyMatch(cities -> cities.stream().anyMatch(cityNode -> cityNode.name.equals(cityStr)));
    }

    public TreeDataStructure createNewContinentNode(String continent, TreeDataStructure fatherNode) {
        if (!checkIfContinentExists(continent, fatherNode)) {
            LOGGER.info("Adding new continent: " + continent + " to the world");
            return this.addChild(continent, fatherNode);
        }
        LOGGER.info("Continent: " + continent + " already exists");
        return fatherNode.children.stream().filter(continentNode -> continentNode.name.equals(continent)).findFirst().get();
    }

    public TreeDataStructure createNewCountryNode(String country, TreeDataStructure continentNode) {
        if (!checkIfCountryExists(country, continentNode)) {
            LOGGER.info("Adding new country: " + country + " to the continent " + continentNode.name);
            return this.addChild(country, continentNode);
        }
        LOGGER.info("Country: " + country + " already exists");
        return continentNode.children.stream().filter(countryNode -> countryNode.name.equals(country)).findFirst().get();
    }

    public TreeDataStructure createNewCityNode(String city, TreeDataStructure countryNode) {
        if (!checkIfCityExists(city, countryNode)) {
            LOGGER.info("Adding new city: " + city + " to the country " + countryNode.name);
            return this.addChild(city, countryNode);
        }
        LOGGER.info("City: " + city + " already exists");
        return countryNode.children.stream().filter(cityNode -> cityNode.name.equals(city)).findFirst().get();
    }

    public String getCountries(List<TreeDataStructure> countries) {
        return countries.stream().map(country -> "Country: " + country.name + ", " + getCities(country.children)).collect(Collectors.joining("; \n"));
    }

    public String getCities(List<TreeDataStructure> cities) {
        return cities.stream().map(city -> "City: " + city.name).collect(Collectors.joining("; \n"));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeDataStructure that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(father, that.father);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, father);
    }

    @Override
    public String toString() {
        return " name: " + name + "\n";
    }
}
