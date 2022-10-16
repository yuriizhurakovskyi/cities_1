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
            LOGGER.info("\nROOT: {}\n{}", name, children.stream()
                    .map(continent -> "Continent: " + continent.name + ", " + getCountries(continent.children))
                    .collect(Collectors.joining("; \n")));
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
            createNewCityNode(cityStr, country);
        }
    }

    public boolean checkIfContinentExists(String continentStr, TreeDataStructure worldNode) {
        return worldNode.children.stream().anyMatch(continent -> continent.name.equals(continentStr));
    }

    public boolean checkIfCountryExists(String countryStr, TreeDataStructure continentNode) {
        return continentNode.father.children.stream()
                .map(continent -> continent.children)
                .anyMatch(countries -> countries.stream()
                        .anyMatch(countryNode -> countryNode.name.equals(countryStr)));
    }

    public boolean checkIfCityExists(String cityStr, TreeDataStructure countryNode) {
        return countryNode.father.children.stream()
                .map(country -> country.children)
                .anyMatch(cities -> cities.stream()
                        .anyMatch(cityNode -> cityNode.name.equals(cityStr)));
    }

    public TreeDataStructure createNewContinentNode(String continent, TreeDataStructure fatherNode) {
        if (!checkIfContinentExists(continent, fatherNode)) {
            LOGGER.info("Adding new continent: {} to the world", continent);
            return this.addChild(continent, fatherNode);
        }
        LOGGER.info("Continent: " + continent + " already exists");
        return fatherNode.children.stream()
                .filter(continentNode -> continentNode.name.equals(continent))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public TreeDataStructure createNewCountryNode(String country, TreeDataStructure continentNode) {
        if (!checkIfCountryExists(country, continentNode)) {
            LOGGER.info("Adding new country: {} to the continent {}", country, continentNode.name);
            return this.addChild(country, continentNode);
        }
        LOGGER.info("Country: " + country + " already exists");
        return continentNode.children.stream()
                .filter(countryNode -> countryNode.name.equals(country))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public void createNewCityNode(String city, TreeDataStructure countryNode) {
        if (!checkIfCityExists(city, countryNode)) {
            LOGGER.info("Adding new city: {} to the country {}", city, countryNode.name);
            TreeDataStructure city_ = this.addChild(city, countryNode);
            LOGGER.info("City {} created", city_.name);
        }
        LOGGER.info("City: {} already exists", city);
    }

    public String getCountries(List<TreeDataStructure> countries) {
        return countries.stream()
                .map(country -> "Country: " + country.name + ", " + getCities(country.children))
                .collect(Collectors.joining("; \n"));
    }

    public String getCities(List<TreeDataStructure> cities) {
        return cities.stream()
                .map(city -> "City: " + city.name)
                .collect(Collectors.joining("; \n"));
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
