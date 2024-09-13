package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
//        Shape newShape = Parser.parseJSONToShape(
//                Path.of("./src/main/resources/example6.json").toAbsolutePath().toString()
//        );
//
//        System.out.println(newShape);
//        try {
//            newShape = Shape.parseJSONToShape(
//                    Path.of("./src/main/resources/example6.json").toAbsolutePath().toString()
//            );
//            System.out.println(newShape);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        catch (IOException e) {
//            throw new RuntimeException(e);
//        }


        Shape newShape = Parser.parseJSONToShape(
                Path.of("./src/main/resources/example3.json").toAbsolutePath().toString()
        );
        System.out.println(newShape);

    }
}