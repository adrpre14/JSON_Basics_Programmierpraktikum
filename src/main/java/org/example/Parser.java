package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Parser {

    public static Map<String, Object> getKeyValuePairsFromJSON(String path) {
        String fileContent = parseJsonFileToString(path);
        Map<String, String> keyValuePairs = new LinkedHashMap<>();

        // Only works for Arrays<String>, Strings, Numbers, Booleans, or null
        Stream.of(fileContent.split(":"))
                .forEach(str -> {
                    String[] separated = str.split(",");
                    separated = Arrays.stream(separated)
                            .map(String::trim)
                            .toArray(String[]::new);
                    if (separated[0].startsWith("[")) {
                        // join the elements until the closing bracket
                        String lastElement = separated[separated.length - 1];
                        if (lastElement.endsWith("]")) {
                            separated = new String[] {String.join(",", separated)};
                        }
                        else {
                            separated = new String[] {String.join(",", Arrays.copyOf(separated, separated.length - 1)), lastElement};
                        }
                    }
                    if (separated.length == 1) { // it's the first key or the last value
                        if (keyValuePairs.isEmpty()) {
                            keyValuePairs.put(separated[0], null);
                        }
                        else {
                            keyValuePairs.replace(
                                    keyValuePairs.keySet().toArray()[keyValuePairs.size() - 1].toString(),
                                    separated[0]
                            );
                        }
                    }
                    else {
                        keyValuePairs.replace(
                                keyValuePairs.keySet().toArray()[keyValuePairs.size() - 1].toString(),
                                separated[0]
                        );
                        keyValuePairs.put(separated[1], null);
                    }
                });

        Map<String, Object> returnMap = new LinkedHashMap<>();
        // parse values to their respective types
        keyValuePairs.forEach((key, value) -> {
            key = key.replace("\"", "").trim();
            if (value == null) return;
            if (value.startsWith("\"") && value.endsWith("\"")) {
                returnMap.put(key, value.substring(1, value.length() - 1));
            }
            else if (value.equals("true") || value.equals("false")) {
                returnMap.put(key, Boolean.parseBoolean(value));
            }
            else if (value.contains(".")) {
                returnMap.put(key, Double.parseDouble(value));
            }
            else if (value.startsWith("[") && value.endsWith("]")) {
                returnMap.put(key, Arrays.stream(value.substring(1, value.length() - 1).split(","))
                        .map(val -> val.trim().replace("\"", ""))
                        .toList()
                );
            }
            else {
                returnMap.put(key, Integer.parseInt(value));
            }
        });

        return returnMap;
    }

    private static String parseJsonFileToString(String path) {
        String fileContent = getFileToString(path);

        if (fileContent.startsWith("{") && fileContent.endsWith("}")) {
            return fileContent.substring(1, fileContent.length() - 1); // we get rid of the curly braces
        }
        else
            throw new RuntimeException("This has not yet been implemented");
    }

    private static String getFileToString(String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            return reader.lines().reduce("", (acc, line) -> acc + line.trim());
        }
        catch (Exception e) {
            throw new RuntimeException("Something went wrong", e);
        }
    }

    public static Shape parseJSONToShape(String path) {
        Map<String, Object> jsonMap = getKeyValuePairsFromJSON(path);
        Shape.ShapeType type = Shape.ShapeType.valueOf(
                jsonMap.getOrDefault("shapeType",
                        jsonMap.getOrDefault("type", "CIRCLE")).toString());
        int posX = (int) jsonMap.getOrDefault("posX",
                jsonMap.getOrDefault("x", 0));
        int posY = (int) jsonMap.getOrDefault("posY",
                jsonMap.getOrDefault("y", 0));
        String color = jsonMap.getOrDefault("color",
                jsonMap.getOrDefault("lineColor", "white")).toString();
        List<String> tags = (List<String>) jsonMap.getOrDefault("tags", List.of());
        String fillColor = jsonMap.getOrDefault("fillColor", "").toString();
        int lineWidth = (int) jsonMap.getOrDefault("lineWidth",
                jsonMap.getOrDefault("lw", 1));
        int rotation = (int) jsonMap.getOrDefault("rotation", 0);
        int scaleX = (int) jsonMap.getOrDefault("scaleX",
                jsonMap.getOrDefault("scaleHorizontal", 0));
        int scaleY = (int) jsonMap.getOrDefault("scaleY",
                jsonMap.getOrDefault("scaleVertical", 0));
        Boolean hidden = (Boolean) jsonMap.getOrDefault("hidden", false);

        try {
            return new Shape(type, posX, posY, tags, color, fillColor, lineWidth, scaleX, scaleY, rotation, hidden);
        }
        catch (IllegalArgumentException e) {
            return new Shape(type, posX, posY, tags, color);
        }
    }

}
