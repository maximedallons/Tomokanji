package com.tomokanji.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.Entry;
import com.tomokanji.model.KanaInfo;
import com.tomokanji.model.KanjiInfo;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WordService {
    private List<Entry> entries;

    public WordService() {
        loadWords();
    }

    private void loadWords() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(
                    new ClassPathResource("static/dictionaries/jmdict.json").getFile()
            );
            JsonNode wordsNode = rootNode.get("words");

            if (wordsNode != null) {
                entries = new ArrayList<>();
                for (JsonNode wordNode : wordsNode) {
                    Entry entry = new Entry();
                    entry.setId(wordNode.get("id").asText());

                    List<KanjiInfo> kanjis = new ArrayList<>();
                    for (JsonNode kanjiNode : wordNode.get("kanji")) {
                        kanjis.add(new KanjiInfo(kanjiNode.get("text").asText(), kanjiNode.get("common").asBoolean()));
                    }

                    List<KanaInfo> kanas = new ArrayList<>();
                    for (JsonNode kanaNode : wordNode.get("kana")) {
                        kanas.add(new KanaInfo(kanaNode.get("text").asText(), kanaNode.get("common").asBoolean()));
                    }

                    List<String> translations = new ArrayList<>();
                    // Check if wordNode has the "sense" field and it is not empty
                    if (wordNode.has("sense") && wordNode.get("sense").size() > 0) {
                        // Get the first sense object
                        JsonNode firstSenseNode = wordNode.get("sense").get(0);

                        // Check if the first sense node has the "gloss" field
                        if (firstSenseNode.has("gloss")) {
                            // Loop through each gloss entry in the first sense object
                            for (JsonNode glossNode : firstSenseNode.get("gloss")) {
                                // Add the text of the gloss to the translations list
                                translations.add(glossNode.get("text").asText());
                            }
                        }
                    }

                    entry.setKanjis(kanjis);
                    entry.setKanas(kanas);
                    entry.setTranslations(translations);

                    // Add examples handling if needed
                    entry.setExamples(new ArrayList<>()); // Initialize examples if you need to

                    entries.add(entry);
                }

                Map<String, Integer> levelMap = loadLevels(); // Load levels
                for (Entry entry : entries) {
                    Integer level = levelMap.get(entry.getId());
                    if (level != null) {
                        entry.setLevel(level);
                    }
                }
            } else {
                entries = new ArrayList<>();
            }
        } catch (IOException ignored) {
            entries = new ArrayList<>();
        }
    }

    private Map<String, Integer> loadLevels() {
        Map<String, Integer> levelMap = new HashMap<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("static/dictionaries/levels.csv");
        if (inputStream == null) {
            return levelMap;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    levelMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException ignored) {}
        return levelMap;
    }

    public List<Entry> searchWords(String query, int level, boolean isCommon) {

        if((query == null || query.isEmpty()) && level == 0) {
            return new ArrayList<>();
        }

        String formattedQuery;
        if (!query.matches("^[\\u3040-\\u30FF\\u3400-\\u4DBF\\u4E00-\\u9FFF\\uF900-\\uFAFF]+$")) {
            formattedQuery = query.toLowerCase();
        } else {
            formattedQuery = query;
        }

        List<Entry> wordsToFilter = new ArrayList<>(entries);

        if(isCommon) {
            List<Entry> commonEntries = new ArrayList<>();
            for(Entry entry : wordsToFilter) {
                if(!entry.hasOnlyUncommons()) {
                    entry.removeUncommons();
                    commonEntries.add(entry);
                }
            }
            wordsToFilter = commonEntries;
        }

        if(level > 0) {
            wordsToFilter = searchWordsByLevel(wordsToFilter, level);
        }

        if(query == null || query.isEmpty()) {
            return wordsToFilter;
        }

        List<Entry> exactMatches = wordsToFilter.stream()
                .filter(entry -> containsExactMatch(entry, formattedQuery))
                .toList();

        List<Entry> partialMatches = wordsToFilter.stream()
                .filter(entry -> containsPartialMatch(entry, formattedQuery))
                .toList();

        List<Entry> filteredWords = Stream.concat(exactMatches.stream(), partialMatches.stream())
                .distinct() // remove duplicates
                .toList();

        List<Entry> wordsWithLevel = filteredWords.stream()
                .filter(entry -> entry.getLevel() != null)
                .sorted((entry1, entry2) -> Integer.compare(entry2.getLevel(), entry1.getLevel())) // Sort in descending order
                .toList();
        List<Entry> wordsWithoutLevel = filteredWords.stream()
                .filter(entry -> entry.getLevel() == null)
                .toList();

        return Stream.concat(wordsWithLevel.stream(), wordsWithoutLevel.stream())
                .collect(Collectors.toList());
    }

    private boolean containsExactMatch(Entry entry, String query) {
        return entry.getKanas().stream().anyMatch(kana -> kana.getText().equals(query)) ||
                entry.getKanjis().stream().anyMatch(kanji -> kanji.getText().equals(query)) ||
                entry.getTranslations().stream().anyMatch(translation -> translation.equals(query)) ||
                entry.getId().equals(query);
    }

    private boolean containsPartialMatch(Entry entry, String query) {
        return entry.getKanas().stream().anyMatch(kana -> kana.getText().contains(query)) ||
                entry.getKanjis().stream().anyMatch(kanji -> kanji.getText().contains(query)) ||
                entry.getTranslations().stream().anyMatch(translation -> translation.contains(query)) ||
                entry.getId().contains(query);
    }

    public List<Entry> searchWordsByLevel(List<Entry> wordsToFilter, int level) {
        return wordsToFilter.stream()
                .filter(entry -> entry.getLevel() != null && entry.getLevel() == level)
                .collect(Collectors.toList());
    }
}