package com.company.raftest;

import java.io.*;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Person implements Serializable {
    private String name;
    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

public class Main {


    static {
        System.out.println("Welcome!");
    }

    public static void main(String[] args) {
        Person rishi = new Person("Rishi");
        Person bob = new Person("Bob");

        List<Person> people = new ArrayList<>();

        System.out.println(Integer.BYTES);
        Path path = FileSystems.getDefault().getPath("hello.dat");

        // java.io + java.nio
        // Write obj
        try(ObjectOutputStream locFile = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            locFile.writeObject(rishi);
            locFile.writeObject(bob);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read obj
        boolean eof = false;
        try (ObjectInputStream locFile = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            try {
                while (!eof) {
                    Person person = (Person) locFile.readObject();
                    System.out.println(person.getName());
                    people.add(person);
                }
            } catch (EOFException e) {
                eof = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // java.nio
        Path pathChar = FileSystems.getDefault().getPath("nio-char-write.txt");
        // write out to text
        try(BufferedWriter locFile = Files.newBufferedWriter(pathChar, Charset.forName("UTF-8"))) {

            for (Person person : people) {
                locFile.write(person.getName());
                locFile.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // read from text
        try (BufferedReader locFile = Files.newBufferedReader(pathChar, Charset.forName("UTF-8"))) {
            System.out.println("Reading from text file.");
            String input;
            try {
                while ((input = locFile.readLine()) != null) {
                    System.out.println(input);
                }
            } catch (EOFException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
