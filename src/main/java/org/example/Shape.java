package org.example;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Vorteile/Nachteile:
// - Jackson ist mehr lesbar und einfacher zu verstehen
// - unsere Implementierung kann nicht verschachtelte JSON-Objekte/JSON-Arrays verarbeiten
// - bei neue Eigenschaften in der Shape-Klasse müssen wir die Methoden in der Parser-Klasse anpassen
// - Fehlerbehandlung ist in Jackson besser implementiert

public class Shape {
    enum ShapeType {CIRCLE, TRIANGLE, QUAD}

    private ShapeType type;
    private int posX;
    private int posY;
    List<String> tags;
    private String color;
    private String fillColor;
    private int lineWidth;
    private int scaleX;
    private int scaleY;
    private int rotation;
    private Boolean hidden;


    //Constructor
    public Shape(ShapeType type, int x, int y, List<String> tags, String color, String fillColor, int lineWidth, int scaleX, int scaleY, int rotation, Boolean hidden) {
        if (rotation < 0 || rotation > 365) {
            throw new IllegalArgumentException("Rotation must be between 0 and 365 degrees.");
        }
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.tags = new ArrayList<>(tags);
        this.color = color;
        this.fillColor = fillColor;
        this.lineWidth = lineWidth;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.rotation = rotation;
        this.hidden = hidden;
    }

    public Shape(ShapeType type, int x, int y, List<String> tags, String color) {
        this.type = type;
        this.posX = x;
        this.posY = y;
        this.tags = new ArrayList<>(tags);
        this.color = color;
    }

    //Copy Constructor
    public Shape(Shape original) {
        this.type = original.type;
        this.posX = original.posX;
        this.posY = original.posY;

        // Deep copy of the tags list
        this.tags = new ArrayList<>();
        for (String tag : original.tags) {
            this.tags.add(new String(tag));  // Deep Copy
        }

        this.color = original.color;
        this.fillColor = original.fillColor;
        this.lineWidth = original.lineWidth;
        this.scaleX = original.scaleX;
        this.scaleY = original.scaleY;
        this.rotation = original.rotation;
        this.hidden = original.hidden;
    }

    // empty constructor
    public Shape() {

    }

    //Getters
    public ShapeType getType() {
        return type;
    }

    @JsonGetter("posX")
    public int getPosx() {
        return posX;
    }

    @JsonGetter("posY")
    public int getPosy() {
        return posY;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getColor() {
        return color;
    }

    public String getFillColor() {
        return fillColor;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public int getScaleX() {
        return scaleX;
    }

    public int getScaleY() {
        return scaleY;
    }

    public int getRotation() {
        return rotation;
    }

    public Boolean getHidden() {
        return hidden;
    }

    //Setters
    @JsonSetter("shapeType")
    @JsonAlias({"shapeType_one_of_three"})
    public void setType(ShapeType type) {
        this.type = type;
    }

    public void setPosx(int posx) {
        this.posX = posx;
    }

    public void setPosy(int posy) {
        this.posY = posy;
    }

    @JsonAlias({"tagSet"})
    public void setTags(List<String> tags) {
        this.tags = new ArrayList<>(tags);
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    public void setRotation(int rotation) {
        if (rotation < 0 || rotation > 365) {
            throw new IllegalArgumentException("Rotation must be between 0 and 365 degrees.");
        }
        this.rotation = rotation;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String toJSONString() throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

    public static Shape parseJSONToShape(String path) throws IOException {
        return new ObjectMapper().readValue(new File(path), Shape.class);
    }

    public static void writeToFile(String content, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Override toString
    @Override
    public String toString() {
        return "ShapeType: " + type +
                ", Position: (" + posX + ", " + posY + ")" +
                ", Color: " + color +
                ", FillColor: " + fillColor +
                ", LineWidth: " + lineWidth +
                ", Scale: (" + scaleX + ", " + scaleY + ")" +
                ", Rotation: " + rotation + "°" +
                ", Hidden: " + hidden +
                ", Tags: " + tags;
    }
}