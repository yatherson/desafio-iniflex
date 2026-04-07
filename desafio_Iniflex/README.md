#  Gestão de Funcionários — Iniflex

> Desafio de programação Java para gerenciamento de funcionários com regras de negócio : cálculos salariais, agrupamentos por função e formatação monetária/temporal no padrão brasileiro.

---

##  Sumário

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Arquitetura e Decisões Técnicas](#arquitetura-e-decisões-técnicas)
- [Requisitos Implementados](#requisitos-implementados)
- [Como Executar](#como-executar)

---

## Sobre o Projeto

Este projeto é uma aplicação Java focada no gerenciamento de uma lista de funcionários. Ele aplica conceitos fundamentais de orientação a objetos, como **herança** e **encapsulamento**, além de práticas modernas da linguagem para garantir precisão financeira, manipulação correta de datas e processamento eficiente de coleções.

---

##  Tecnologias Utilizadas

| Tecnologia | Finalidade |
|---|---|
| **Java 21** | Recursos modernos: `var` (Type Inference), Sequenced Collections, melhorias na Stream API |
| **Maven** | Gestão de dependências e estrutura de diretórios padronizada |
| **BigDecimal** | Precisão absoluta em cálculos financeiros, eliminando erros de `float`/`double` |
| **Java Time API (`LocalDate`)** | Manipulação moderna e segura de datas |

---

## Arquitetura e Decisões Técnicas

### 1. Organização do Projeto

O projeto é dividido em pacotes para separar responsabilidades:

```
src/
└── main/
    └── java/
        ├── model/
        │   ├── Pessoa.java        # Entidade base com nome e dataNascimento
        │   └── Funcionario.java   # Estende Pessoa; adiciona salario e funcao
        └── main/
            └── Main.java          # Lógica de execução e regras de negócio
```

`Funcionario` **estende** `Pessoa` via **herança**, evitando duplicação de código e facilitando manutenção.

---

### 2. Gestão de Coleções — `ArrayList` vs `List.of`

```java
// Correto: lista mutável para suportar remoções
List<Funcionario> funcionarios = new ArrayList<>(List.of(...));
```

`List.of()` cria listas **imutáveis**. Como o requisito 3.2 exige a remoção do funcionário *"João"*, envolvi a lista em um `ArrayList` para habilitar operações de modificação.

---

### 3. Remoção Segura com `removeIf`

```java
// Atômico e seguro — sem risco de ConcurrentModificationException
funcionarios.removeIf(f -> f.getNome().equals("João"));
```

Loops tradicionais sobre a mesma coleção que está sendo modificada causam `ConcurrentModificationException`. O `removeIf` com predicado resolve isso de forma segura e declarativa.

---

### 4. Precisão Financeira e Formatação

- **Aumento salarial:** aplicado diretamente nos objetos da lista antes de qualquer cálculo de totais.
- **Internacionalização:** `NumberFormat` com `Locale("pt", "BR")` garante a saída no padrão brasileiro:

```
R$ 3.500,00   →  ponto para milhar, vírgula para decimal
```

---

### 5. Cálculo de Idade com `ChronoUnit`

```java
// Considera dia e mês, não apenas o ano
long idade = ChronoUnit.YEARS.between(funcionario.getDataNascimento(), LocalDate.now());
```

A subtração simples de anos ignora se o aniversário já ocorreu no ano atual. `ChronoUnit.YEARS.between` faz a comparação completa (dia/mês/ano), garantindo precisão.

---

### 6. Processamento com Streams

Agrupamentos, ordenações e somatórios foram implementados com a **Stream API**, tornando o código **declarativo** (focado no *o quê* fazer) em vez de imperativo (focado no *como* fazer):

```java
// Agrupamento por função
Map<String, List<Funcionario>> porFuncao = funcionarios.stream()
    .collect(Collectors.groupingBy(Funcionario::getFuncao));

// Ordenação alfabética
funcionarios.stream()
    .sorted(Comparator.comparing(Funcionario::getNome))
    .forEach(...);
```

---

## Requisitos Implementados

- [x] Classe `Pessoa` com `nome` e `dataNascimento`
- [x] Classe `Funcionario` com `salario` e `funcao` (estende `Pessoa`)
- [x] Inserção de dados conforme tabela fornecida
- [x] Remoção de funcionário pelo nome (`"João"`)
- [x] Formatação de datas (`dd/MM/yyyy`) e valores (`R$ 0.000,00`)
- [x] Aumento salarial de 10% para todos os funcionários
- [x] Agrupamento por função usando `Map<String, List<Funcionario>>`
- [x] Filtro de aniversariantes nos meses 10 e 12
- [x] Identificação do funcionário mais velho (com idade exata via `ChronoUnit`)
- [x] Ordenação alfabética por nome
- [x] Somatório total dos salários
- [x] Cálculo de quantos salários mínimos cada funcionário recebe (base: R$ 1.212,00)

---

##  Como Executar

**Pré-requisitos:** JDK 21+ instalado e Maven configurado.

```bash
# 1. Clone o repositório
git clone https://github.com/yatherson/iniflex.git

# 2. Acesse o diretório
cd iniflex

# 3. Compile e execute via Maven
mvn compile exec:java -Dexec.mainClass="main.Main"
```

> **IntelliJ IDEA:** Basta importar o projeto — o Maven será reconhecido automaticamente. Execute a classe `Main` diretamente pela IDE.

---

