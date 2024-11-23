package com.tomokanji.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.KanjiInfo;
import com.tomokanji.model.Word;
import com.tomokanji.model.WordListWrapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WordService {
    private List<Word> words;

    public WordService() {
        loadWords();
    }

    private void loadWords() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WordListWrapper wrapper = objectMapper.readValue(
                    new ClassPathResource("static/dictionaries/jmdict.json").getFile(),
                    WordListWrapper.class
            );
            Map<String, Integer> levelMap = loadLevels(); // Load levels
            words = wrapper.getWords();
            // Assign levels to each word
            for (Word word : words) {
                Integer level = levelMap.get(word.getId());
                if (level != null) {
                    word.setLevel(level);
                }
            }
        } catch (IOException ignored) {
            words = new ArrayList<>();
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

    public List<Word> searchWords(String query, int level, boolean isCommon) {

        if((query == null || query.isEmpty()) && level == 0) {
            return new ArrayList<>();
        }

        String formattedQuery;
        if (!query.matches("^[\\u3040-\\u30FF\\u3400-\\u4DBF\\u4E00-\\u9FFF\\uF900-\\uFAFF]+$")) {
            formattedQuery = query.toLowerCase();
        } else {
            formattedQuery = query;
        }

        List<Word> wordsToFilter = words.stream().toList();

        if(isCommon) {
            System.out.println("Filtering common words");
            wordsToFilter = words.stream()
                    .filter(word -> word.getKanji().stream().anyMatch(KanjiInfo::isCommon))
                    .collect(Collectors.toList());
        }

        if(level > 0) {
            wordsToFilter = searchWordsByLevel(wordsToFilter, level);
        }

        if(query == null || query.isEmpty()) {
            return wordsToFilter;
        }

        List<Word> exactMatches = wordsToFilter.stream()
                .filter(word -> containsExactMatch(word, formattedQuery))
                .collect(Collectors.toList());

        List<Word> partialMatches = wordsToFilter.stream()
                .filter(word -> containsPartialMatch(word, formattedQuery))
                .collect(Collectors.toList());

        List<Word> filteredWords = Stream.concat(exactMatches.stream(), partialMatches.stream())
                .distinct() // remove duplicates
                .collect(Collectors.toList());

        List<Word> wordsWithLevel = filteredWords.stream()
                .filter(word -> word.getLevel() != null)
                .sorted((word1, word2) -> Integer.compare(word2.getLevel(), word1.getLevel())) // Sort in descending order
                .collect(Collectors.toList());
        List<Word> wordsWithoutLevel = filteredWords.stream()
                .filter(word -> word.getLevel() == null)
                .collect(Collectors.toList());

        return Stream.concat(wordsWithLevel.stream(), wordsWithoutLevel.stream())
                .collect(Collectors.toList());
    }

    private boolean containsExactMatch(Word word, String query) {
        return word.getKana().stream().anyMatch(kana -> kana.getText().equals(query)) ||
                word.getKanji().stream().anyMatch(kanji -> kanji.getText().equals(query) && kanji.isCommon()) ||
                (word.getSense() != null && !word.getSense().isEmpty() &&
                        word.getSense().get(0).getGloss().get(0).getText().equals(query)) ||
                word.getId().equals(query);
    }

    private boolean containsPartialMatch(Word word, String query) {
        return word.getKana().stream().anyMatch(kana -> kana.getText().contains(query)) ||
                word.getKanji().stream().anyMatch(kanji -> kanji.getText().contains(query) && kanji.isCommon()) ||
                (word.getSense() != null && !word.getSense().isEmpty() &&
                        word.getSense().get(0).getGloss().get(0).getText().contains(query)) ||
                word.getId().contains(query);
    }

    public List<Word> searchWordsByLevel(List<Word> wordsToFilter, int level) {
        return wordsToFilter.stream()
                .filter(word -> word.getLevel() != null && word.getLevel() == level)
                .collect(Collectors.toList());
    }
}