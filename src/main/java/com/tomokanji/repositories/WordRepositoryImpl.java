package com.tomokanji.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.KanaInfo;
import com.tomokanji.model.KanjiInfo;
import com.tomokanji.model.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class WordRepositoryImpl implements WordRepository {
    private List<Word> words = new ArrayList<>();
    private Map<Integer, Integer> levelMap = new HashMap<>();

    @Value("${levelsPath}")
    private String levelsPath;

    @Value("${jmdictPath}")
    private String jmDictPath;

    @PostConstruct
    public void init() {
        loadLevels();
        loadWords();
    }

    @Override
    public List<Word> getWords() {
        return words;
    }

    @Override
    public List<Word> getWordsByLevel(int level) {
        List<Word> filteredWords = words.stream().filter(word -> word.getLevel() != null && word.getLevel() == level)
                .collect(Collectors.toList());
        return filteredWords;
    }

    @Override
    public List<Word> getWordsByQuery(String query) {
        // Minimum query length is 4 characters
        if(query.length() <= 3)
            return null;

        List<Word> filteredWords = words.stream().filter(word ->
                        word.getKanjis().stream().anyMatch(kanji -> kanji.getText().equals(query)) ||
                        word.getKanas().stream().anyMatch(kana -> kana.getText().equals(query)) ||
                        word.getTranslations().stream().anyMatch(translation -> translation.contains(query))
        ).collect(Collectors.toList());
        return filteredWords;
    }

    private void loadWords() {
        System.out.println("Loading words...");
        List<Word> words = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(
                    new File(this.jmDictPath)
            );
            JsonNode wordsNode = rootNode.get("words");

            if (wordsNode != null) {
                for (JsonNode wordNode : wordsNode) {
                    Word word = new Word();
                    word.setId(wordNode.get("id").asInt());

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

                    word.setKanjis(kanjis);
                    word.setKanas(kanas);
                    word.setTranslations(translations);

                    // Add examples handling if needed
                    word.setExamples(new ArrayList<>()); // Initialize examples if you need to

                    Integer level = levelMap.get(word.getId());
                    if (level != null) {
                        word.setLevel(level);
                    }

                    if(!word.hasOnlyUncommons())
                        words.add(word);
                }
                System.out.println("Words loaded: " + words.size());
            }
        } catch (IOException ignored) {
            System.out.println("Error loading words");
        }
        this.words = words;
    }

    private void loadLevels() {
        System.out.println("Loading word levels...");
        System.out.println("Levels path: " + this.levelsPath);
        Map<Integer, Integer> levelMap = new HashMap<>();
        File levelFile = new File(this.levelsPath);
        if (!levelFile.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(levelFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    levelMap.put(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                }
            }
            System.out.println("Levels loaded: " + levelMap.size());
        } catch (IOException ignored) {
            System.out.println("Error loading levels");
        }
        this.levelMap = levelMap;
    }
}
