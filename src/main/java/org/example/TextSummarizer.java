import java.util.*;

public class TextSummarizer {

    public static String summarize(String text, int numSentences) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        // 1. Dividir em frases (abordagem simples, para um resultado melhor usar OpenNLP)
        String[] sentences = text.split("[.!?]");

        // 2. Calcular a frequência de cada palavra
        Map<String, Integer> wordFrequencies = getWordFrequencies(text);

        // 3. Pontuar as frases
        Map<String, Integer> sentenceScores = new HashMap<>();
        for (String sentence : sentences) {
            int score = 0;
            String[] words = sentence.toLowerCase().split("\\s+");
            for (String word : words) {
                if (wordFrequencies.containsKey(word)) {
                    score += wordFrequencies.get(word);
                }
            }
            // Armazena a frase original com sua pontuação
            sentenceScores.put(sentence.trim(), score);
        }

        // 4. Ordenar as frases pela pontuação
        List<Map.Entry<String, Integer>> sortedSentences = new ArrayList<>(sentenceScores.entrySet());
        sortedSentences.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // 5. Construir o resumo com as melhores frases
        StringBuilder summary = new StringBuilder();
        int sentencesAdded = 0;
        for (Map.Entry<String, Integer> entry : sortedSentences) {
            if (sentencesAdded < numSentences && !entry.getKey().isEmpty()) {
                summary.append(entry.getKey()).append(". ");
                sentencesAdded++;
            }
        }

        return summary.toString();
    }

    private static Map<String, Integer> getWordFrequencies(String text) {
        Map<String, Integer> frequencies = new HashMap<>();
        // Remove pontuação, converte para minúsculas e divide em palavras
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Záàâãéèêíïóôõöúçñ\\s]", "").split("\\s+");

        // Lista de stop words (palavras comuns a serem ignoradas)
        Set<String> stopWords = new HashSet<>(Arrays.asList(
                "a", "o", "e", "de", "do", "da", "em", "um", "uma", "para", "com", "não", "os", "as", "que"
        ));

        for (String word : words) {
            if (!stopWords.contains(word) && word.length() > 2) {
                frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        }
        return frequencies;
    }
}