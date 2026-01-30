package app.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.pl.PolishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Search {

    private static final String INDEX_DIRECTORY = "index";
    private static final int MAX_RESULTS = 10;

    public static void main(String[] args) {
        try {
            performSearch();
        } catch (IOException e) {
            System.err.println("Błąd: Nie można otworzyć indeksu.");
            System.err.println("Upewnij się, że uruchomiłeś najpierw Index.java");
        }
    }

    private static void performSearch() throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);

        Analyzer analyzer = new PolishAnalyzer();
        QueryParser parser = new QueryParser("content", analyzer);

        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Wyszukiwarka Lucene ===");
        System.out.println("Wpisz zapytanie (lub 'exit' aby wyjść):");
        System.out.println();

        while (true) {
            System.out.print("> ");
            String queryString = scanner.nextLine().trim();

            if (queryString.equalsIgnoreCase("exit")) {
                break;
            }

            if (queryString.isEmpty()) {
                continue;
            }

            try {
                Query query = parser.parse(queryString);
                TopDocs results = searcher.search(query, MAX_RESULTS);
                ScoreDoc[] hits = results.scoreDocs;

                System.out.println("\nZnaleziono: " + results.totalHits.value + " dokumentów");
                System.out.println("Wyświetlam: " + hits.length);
                System.out.println();

                for (int i = 0; i < hits.length; i++) {
                    Document doc = searcher.storedFields().document(hits[i].doc);
                    System.out.println((i + 1) + ". " + doc.get("title"));
                    System.out.println("   ID: " + doc.get("id"));
                    System.out.println("   " + doc.get("content"));
                    System.out.println();
                }

            } catch (ParseException e) {
                System.err.println("Błędne zapytanie: " + e.getMessage());
            }
        }

        reader.close();
        directory.close();
        System.out.println("Do widzenia!");
    }
}