package br.com.letscode.java;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aplicacao {
    /*
    Quando informado o nome de um ator ou atriz, dê um resumo de quantos prêmios ele/ela recebeu e liste ano, idade e nome de cada filme pelo qual foi premiado(a).
    */
    private List<Oscar> atores;

    public static void main(String[] args) {
        Aplicacao app = new Aplicacao();
        Aplicacao appAtriz = new Aplicacao();
        Aplicacao appAtor = new Aplicacao();
        appAtor.testeDeLeituraArquivoCsv("male.csv");
        appAtriz.testeDeLeituraArquivoCsv("female.csv");

        System.out.println("O ator mais jovem a ganhar um Oscar: ");
        appAtor.findYoungestActor();
        System.out.println("\nA atriz mais vezes premiada: ");
        appAtriz.findMostAwardedActress();
        System.out.println("\nA atriz mais vezes premiada entre 20 e 30 anos: ");
        appAtriz.findMostAwardedActressBtwAges();

        List<Oscar> listaAtores = new ArrayList<>(appAtor.atores);
        List<Oscar> listaAtrizes = new ArrayList<>(appAtriz.atores);
        List<List<Oscar>> list = List.of(listaAtores, listaAtrizes);
        System.out.println("\nAtores ou atrizes que receberam mais oscars: ");
        app.findActWhoReceiveMoreThanOneOscar(list);

        String pessoaInformada = "Katharine Hepburn";
        System.out.println("\nPessoa informada : " + pessoaInformada);
        app.findThisPerson(pessoaInformada, list);

    }

    private void findYoungestActor() {
        this.atores
                .stream()
                .min(Comparator.comparingInt(Oscar::getAge))
                .ifPresent(System.out::println);
    }

    private void findMostAwardedActress() {
        this.atores
                .stream()
                .collect(Collectors.groupingBy(Oscar::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .ifPresent(System.out::println);
    }

    private void findMostAwardedActressBtwAges() {
        this.atores
                .stream()
                .filter(p -> p.getAge() >= 20 && p.getAge() <= 30)
                .collect(Collectors.groupingBy(Oscar::getName, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .ifPresent(System.out::println);
    }

    private void findActWhoReceiveMoreThanOneOscar(List<List<Oscar>> list) {
        Stream.of(list)
                .flatMap(List::stream)
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(Oscar::getName, Collectors.counting()))
                .entrySet().stream().filter(e -> e.getValue() > 1)
                .collect(Collectors.toList())
                .stream()
                .map(Map.Entry::getKey)
                .forEach(System.out::println);
    }

    private void findThisPerson(String nome, List<List<Oscar>> list){
        Stream.of(list)
                .flatMap(List::stream)
                .flatMap(List::stream)
                .filter(p -> p.getName().equals(nome))
                .forEach(System.out::println);

    }

    private void testeDeLeituraArquivoCsv(String fileName) {
        String filePath = getFilePathFromResourceAsStream(fileName);
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            this.atores = lines
                    .skip(1)
                    .map(Oscar::listaSplit)
                    .toList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilePathFromResourceAsStream(String fileName) {
        URL url = getClass().getClassLoader().getResource(fileName);
        File file = new File(url.getFile());
        return file.getPath();
    }

}
