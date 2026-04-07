package br.com.iniflex;

import br.com.iniflex.model.Funcionario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {

        // 3.1 - Inserção
        List<Funcionario> funcionarios = new ArrayList<>(List.of(
                new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
                new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
                new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
                new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
                new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.58"), "Recepcionista"),
                new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
                new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
                new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
                new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
                new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        ));

        // 3.2 - Remoção
        funcionarios.removeIf(f -> f.getNome().equals("João"));

        // 3.3 - Impressão Formatada
        var df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

        System.out.println("--- 3.3 ---");
        funcionarios.forEach(f -> System.out.printf("%s | %s | %s | %s%n",
                f.getNome(), f.getDataNascimento().format(df), nf.format(f.getSalario()), f.getFuncao()));
        System.out.println();

        // 3.4 - Aumento
        funcionarios.forEach(f -> f.setSalario(f.getSalario().multiply(new BigDecimal("1.10"))));

        // 3.5 e 3.6 - Agrupamento
        var agrupados = funcionarios.stream().collect(Collectors.groupingBy(Funcionario::getFuncao));
        System.out.println("--- 3.6 ---");
        agrupados.forEach((funcao, lista) -> System.out.println(funcao + ": " + lista.stream().map(Funcionario::getNome).toList()));
        System.out.println();

        // 3.8 - Aniversariantes
        var mesesAlvo = List.of(Month.OCTOBER, Month.DECEMBER);
        System.out.println("--- 3.8 ---");
        funcionarios.stream()
                .filter(f -> mesesAlvo.contains(f.getDataNascimento().getMonth()))
                .forEach(f -> System.out.println("Aniversariante: " + f.getNome()));
        System.out.println();

        // 3.9 - Maior Idade
        System.out.println("--- 3.9 ---");
        funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresent(maisVelho -> {
                    long idadeReal = ChronoUnit.YEARS.between(maisVelho.getDataNascimento(), LocalDate.now());
                    System.out.println("Mais velho: " + maisVelho.getNome() + " | Idade: " + idadeReal);
                });
        System.out.println();

        // 3.10 - Ordem Alfabética
        var ordenados = funcionarios.stream().sorted(Comparator.comparing(Funcionario::getNome)).toList();
        System.out.println("--- 3.10 ---");
        System.out.println("Ordem alfabética: " + ordenados.stream().map(Funcionario::getNome).toList());
        System.out.println();

        // 3.11 - Total Salários
        var total = funcionarios.stream().map(Funcionario::getSalario).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("--- 3.11 ---");
        System.out.println("Total: " + nf.format(total));
        System.out.println();

        // 3.12 - Salários Mínimos
        BigDecimal min = new BigDecimal("1212.00");
        System.out.println("--- 3.12 ---");
        funcionarios.forEach(f -> System.out.println(f.getNome() + " ganha " + f.getSalario().divide(min, 1, RoundingMode.DOWN) + " salários mínimos."));
        System.out.println();
    }

}