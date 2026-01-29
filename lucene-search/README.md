# Projekt: Wyszukiwanie pełnotekstowe z Apache Lucene

**Przedmiot:** Zaawansowane Technologie Przetwarzania Danych  
**Temat:** Wyszukiwanie pełnotekstowe przez integrację biblioteki Lucene w aplikacji

## Opis

Projekt składa się z dwóch prostych aplikacji Java wykorzystujących Apache Lucene:
- `Index.java` - tworzy indeks i dodaje przykładowe dokumenty
- `Search.java` - wyszukuje dokumenty w indeksie

## Technologie

- Apache Lucene 9.9.1
- PolishAnalyzer ze stemmerem Stempel
- Java 17
- Maven

## Struktura projektu

```
lucene-search/
├── pom.xml
├── src/main/java/app/lucene/
│   ├── Index.java
│   └── Search.java
└── index/          (tworzony automatycznie)
```

## Kompilacja

```bash
mvn clean compile
```

## Uruchomienie

### 1. Utworzenie indeksu

```bash
mvn exec:java -Dexec.mainClass="app.lucene.Index"
```

### 2. Wyszukiwanie

```bash
mvn exec:java -Dexec.mainClass="app.lucene.Search"
```

## Przykłady zapytań

```
> lucene
> wyszukiwanie
> lucene AND wyszukiwanie
> indeksowanie OR analiza
> przetwarzanie NOT bazy
> wyszuki*
> lucene~
```

## Przykładowe dokumenty w indeksie

1. Wprowadzenie do Apache Lucene
2. Analiza tekstu w Lucene
3. Wyszukiwanie w indeksie
4. Bazy danych NoSQL
5. Przetwarzanie danych

## Wykorzystane elementy z laboratoriów

- PolishAnalyzer dla języka polskiego
- FSDirectory - indeks na dysku
- QueryParser do parsowania zapytań
- Struktura dokumentu z polami TextField i StringField
- Podstawowe zapytania (proste, operatory logiczne, wildcards, fuzzy)

## Rozszerzenia względem laboratoriów

- Trwały indeks na dysku (zamiast pamięci)
- Interaktywny interfejs wyszukiwania
- Więcej przykładowych dokumentów
- Pełna treść dokumentów (pole content)

## Testy wyszukiwania

Przetestuj następujące zapytania:

1. **Proste wyszukiwanie:** `lucene` - znajdzie dokumenty z tym słowem
2. **Operatory:** `lucene AND wyszukiwanie` - oba słowa muszą występować
3. **Stemming:** `wyszukiwanie` - znajdzie też "wyszukiwać", "wyszukiwania"
4. **Wildcards:** `indeks*` - znajdzie "indeks", "indeksowanie", "indeksować"
5. **Fuzzy:** `lucene~` - tolerancja na literówki

## Wnioski

Projekt pokazuje praktyczne zastosowanie Apache Lucene do wyszukiwania pełnotekstowego.
PolishAnalyzer dobrze radzi sobie z polską odmianą wyrazów dzięki stemmerowi Stempel.
System może być rozbudowany o więcej dokumentów, dodatkowe pola, czy interfejs webowy.
