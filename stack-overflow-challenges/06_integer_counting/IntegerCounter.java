import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.LongStream;

public class IntegerCounter {

    public static Path downloadFile(String url, String filename) throws IOException, InterruptedException {
        Path outputPath = Paths.get(filename);
        if (Files.exists(outputPath)) {
            return outputPath;
        }
        System.out.println("Downloading: " + url);

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<Path> response = client.send(request,
                    HttpResponse.BodyHandlers.ofFile(outputPath));

            System.out.println("Downloaded to: " + response.body());
            return outputPath;
        }
    }

    public static int[] readIntegersFromFile(Path filePath) throws IOException {
        return Files.lines(filePath)
                .filter(s -> !s.isEmpty())
                .mapToInt((s) -> Integer.valueOf(s.trim()))
                .toArray();
    }

    public static int[] mostFrequentInteger(int[] integers) {
        int[] frequencies = new int[1000];
        int mostFrequent = -1;
        int maxFrequency = 0;

        for (int num : integers) {
            frequencies[num]++;
            if (frequencies[num] > maxFrequency) {
                maxFrequency = frequencies[num];
                mostFrequent = num;
            }
        }
        return new int[] { mostFrequent, maxFrequency };
    }

    public static <T> void benchmarkFunction(Supplier<T> function, int iterations) {
        long[] times = new long[iterations];

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            function.get();
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long totalTime = LongStream.of(times).sum();
        long maxTime = LongStream.of(times).max().getAsLong();
        long minTime = LongStream.of(times).min().getAsLong();

        double totalMilliseconds = totalTime / 1_000_000.0;
        double totalMicroseconds = totalTime / 1_000.0;
        double averageMicroseconds = totalMicroseconds / iterations;
        double averageMilliseconds = totalMilliseconds / iterations;
        double maxMicroseconds = maxTime / 1_000.0;
        double maxMilliseconds = maxTime / 1_000_000.0;
        double minMicroseconds = minTime / 1_000.0;
        double minMilliseconds = minTime / 1_000_000.0;

        String decimalFormat = "%-40s %15.2f\n";

        System.out.println("==== Benchmark Results ====");
        System.out.printf("\nTotal executions performed %d which took total of %.2f ms\n", iterations,
                totalMilliseconds);
        System.out.println("\n ==== Stats in microseconds ====");
        System.out.printf(decimalFormat, "Average per execution (microseconds):", averageMicroseconds);
        System.out.printf(decimalFormat, "Highest time (microseconds):", maxMicroseconds);
        System.out.printf(decimalFormat, "Lowest time (microseconds):", minMicroseconds);
        System.out.println("\n ==== Stats in milliseconds ====");
        System.out.printf(decimalFormat, "Average per execution (milliseconds):", averageMilliseconds);
        System.out.printf(decimalFormat, "Highest time (milliseconds):", maxMilliseconds);
        System.out.printf(decimalFormat, "Lowest time (milliseconds):", minMilliseconds);
        System.out.println("==== ====");
    }

    public static void main(String[] args) throws Exception {
        String filename = "1M-integers-list.txt";
        String fileUrl = "https://drive.usercontent.google.com/download?id=14kbAC0edO05Z1EIYbZMC6Gpzx1u2yecd&export=download";
        Path integerFilePath = downloadFile(fileUrl, filename);

        int[] integers = readIntegersFromFile(integerFilePath);
        System.out.println("Read " + integers.length + " integers from file.");

        int[] result = mostFrequentInteger(integers);
        System.out.printf("Most frequent number is '%d'. It occured %d times\n", result[0], result[1]);

        benchmarkFunction(() -> mostFrequentInteger(integers), 1000);
    }
}
