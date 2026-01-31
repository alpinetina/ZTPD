package app.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
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
import org.apache.lucene.search.highlight.*;
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
        System.out.println("Dokumentów w indeksie: " + reader.numDocs());
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

                long startTime = System.currentTimeMillis();
                TopDocs results = searcher.search(query, MAX_RESULTS);
                long searchTime = System.currentTimeMillis() - startTime;

                ScoreDoc[] hits = results.scoreDocs;

                System.out.println("\n--- Statystyki ---");
                System.out.println("Znaleziono: " + results.totalHits.value + " dokumentów");
                System.out.println("Czas wyszukiwania: " + searchTime + " ms");
                if (hits.length > 0) {
                    System.out.println("Najlepszy wynik (score): " + String.format("%.4f", hits[0].score));
                    System.out.println("Najsłabszy wynik (score): " + String.format("%.4f", hits[hits.length-1].score));
                }
                System.out.println("Wyświetlam: " + hits.length + " dokumentów");
                System.out.println();

                if (hits.length == 0) {
                    System.out.println("Brak wyników dla zapytania: " + queryString);
                    continue;
                }

                Formatter formatter = new SimpleHTMLFormatter("[", "]");
                QueryScorer scorer = new QueryScorer(query);
                Highlighter highlighter = new Highlighter(formatter, scorer);
                Fragmenter fragmenter = new SimpleFragmenter(100);
                highlighter.setTextFragmenter(fragmenter);

                for (int i = 0; i < hits.length; i++) {
                    Document doc = searcher.storedFields().document(hits[i].doc);
                    float score = hits[i].score;

                    System.out.println((i + 1) + ". " + doc.get("title") +
                            " (score: " + String.format("%.4f", score) + ")");
                    System.out.println("   ID: " + doc.get("id"));

                    String content = doc.get("content");
                    try {
                        TokenStream tokenStream = analyzer.tokenStream("content", content);
                        String[] fragments = highlighter.getBestFragments(tokenStream, content, 2);

                        if (fragments.length > 0) {
                            System.out.println("   Znalezione frazy:");
                            for (String fragment : fragments) {
                                System.out.println("   ... " + fragment + " ...");
                            }
                        } else {
                            System.out.println("   " + content);
                        }
                    } catch (InvalidTokenOffsetsException e) {
                        System.out.println("   " + content);
                    }

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