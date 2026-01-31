package app.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class Index {

    private static final String INDEX_DIRECTORY = "index";

    public static void main(String[] args) {
        try {
            createIndex();
            System.out.println("Indeks został utworzony pomyślnie!");
        } catch (IOException e) {
            System.err.println("Błąd: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createIndex() throws IOException {
        Analyzer analyzer = new PolishAnalyzer();

        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(directory, config);

        writer.addDocument(buildDoc("Wprowadzenie do Apache Lucene", "001",
                "Apache Lucene to biblioteka do wyszukiwania pełnotekstowego. " +
                        "Umożliwia indeksowanie dokumentów i wykonywanie zaawansowanych zapytań."));

        writer.addDocument(buildDoc("Analiza tekstu w Lucene", "002",
                "Lucene wykorzystuje analizatory do przetwarzania tekstu. " +
                        "PolishAnalyzer obsługuje język polski ze stemmerem Stempel."));

        writer.addDocument(buildDoc("Wyszukiwanie w indeksie", "003",
                "Wyszukiwanie odbywa się przez QueryParser. " +
                        "Można używać operatorów logicznych AND, OR, NOT."));

        writer.addDocument(buildDoc("Bazy danych NoSQL", "004",
                "MongoDB to popularna baza dokumentowa. " +
                        "Elasticsearch wykorzystuje Lucene do wyszukiwania."));

        writer.addDocument(buildDoc("Przetwarzanie danych", "005",
                "Zaawansowane technologie przetwarzania danych obejmują " +
                        "indeksowanie, wyszukiwanie i analizę tekstów."));

        writer.addDocument(buildDoc("Programowanie w języku Java", "006",
                "Java to obiektowy język programowania używany do tworzenia " +
                        "aplikacji desktopowych, webowych i mobilnych."));

        writer.addDocument(buildDoc("Algorytmy sortowania", "007",
                "Podstawowe algorytmy sortowania to Quick Sort, Merge Sort " +
                        "i Bubble Sort. Różnią się złożonością czasową."));

        writer.addDocument(buildDoc("Uczenie maszynowe", "008",
                "Machine learning wykorzystuje algorytmy do analizy danych " +
                        "i wykrywania wzorców bez jawnego programowania."));

        writer.addDocument(buildDoc("Sieci neuronowe", "009",
                "Sztuczne sieci neuronowe są inspirowane biologicznymi " +
                        "neuronami i służą do rozpoznawania obrazów i tekstu."));

        writer.addDocument(buildDoc("Bazy danych relacyjne", "010",
                "SQL to język zapytań dla relacyjnych baz danych. " +
                        "PostgreSQL i MySQL to popularne systemy bazodanowe."));

        writer.addDocument(buildDoc("Struktury danych", "011",
                "Podstawowe struktury to tablice, listy, stosy, kolejki " +
                        "i drzewa binarne. Każda ma inne zastosowania."));

        writer.addDocument(buildDoc("Systemy rozproszone", "012",
                "Architektura rozproszona pozwala na skalowanie aplikacji. " +
                        "Kafka i RabbitMQ to popularne systemy kolejkowania."));

        writer.addDocument(buildDoc("REST API", "013",
                "RESTful API to architektura do tworzenia usług webowych. " +
                        "Wykorzystuje metody HTTP GET, POST, PUT, DELETE."));

        writer.addDocument(buildDoc("Bezpieczeństwo aplikacji", "014",
                "Zabezpieczanie aplikacji obejmuje szyfrowanie danych, " +
                        "uwierzytelnianie użytkowników i ochronę przed atakami."));

        writer.addDocument(buildDoc("Docker i konteneryzacja", "015",
                "Docker pozwala pakować aplikacje w kontenery. " +
                        "Kubernetes służy do orkiestracji wielu kontenerów."));

        writer.close();
        directory.close();
    }

    private static Document buildDoc(String title, String id, String content) {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("id", id, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));
        return doc;
    }
}