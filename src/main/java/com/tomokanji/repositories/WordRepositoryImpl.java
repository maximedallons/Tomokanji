package com.tomokanji.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.*;
import org.springframework.beans.factory.annotation.Value;
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
        if(query.length() <= 3)
            return null;

        List<Word> filteredWords = words.stream().filter(word ->
                        word.getKanjis().stream().anyMatch(kanji -> kanji.getText().equals(query)) ||
                        word.getKanas().stream().anyMatch(kana -> kana.getText().equals(query)) ||
                        word.getTranslations().stream().anyMatch(translation -> translation.contains(query))
        ).collect(Collectors.toList());
        return filteredWords;
    }

    @Override
    public List<Word> findWordsByIds(List<Integer> wordIds) {
        return words.stream().filter(word -> wordIds.contains(word.getId())).collect(Collectors.toList());
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
                    boolean skipThisWord = false;
                    Word word = new Word();
                    word.setId(wordNode.get("id").asInt());

                    List<KanjiInfo> kanjis = new ArrayList<>();
                    for (JsonNode kanjiNode : wordNode.get("kanji")) {
                        if (kanjiNode.get("text").asText().length() == 1)
                            skipThisWord = true;

                        if (kanjiNode.get("common").asBoolean())
                            kanjis.add(new KanjiInfo(kanjiNode.get("text").asText(), true));
                    }

                    List<KanaInfo> kanas = new ArrayList<>();
                    JsonNode kanaArray = wordNode.get("kana");
                    if (kanaArray.size() == 1) {
                        // Add the single kana regardless of common status
                        JsonNode singleKanaNode = kanaArray.get(0);
                        kanas.add(new KanaInfo(singleKanaNode.get("text").asText(), singleKanaNode.get("common").asBoolean()));
                    } else {
                        // Otherwise, add only the common kanas
                        for (JsonNode kanaNode : kanaArray) {
                            if (kanaNode.get("common").asBoolean()) {
                                kanas.add(new KanaInfo(kanaNode.get("text").asText(), true));
                            }
                        }
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

                        // Populate examples from the "sentences" field
                        List<Example> examples = new ArrayList<>();
                        if(firstSenseNode.has("examples")) {
                            for(JsonNode exampleNode : firstSenseNode.get("examples")) {
                                Example example = new Example();
                                if (exampleNode.has("sentences")) {
                                    List<Sentence> sentences = new ArrayList<>();
                                    for (JsonNode sentenceNode : exampleNode.get("sentences")) {
                                        Sentence sentence = new Sentence();
                                        sentence.setLang(sentenceNode.get("land").asText());
                                        sentence.setText(sentenceNode.get("text").asText());
                                        sentences.add(sentence);
                                    }
                                    example.setText(exampleNode.get("text").asText());
                                    example.setSentences(sentences);
                                }
                                examples.add(example);
                            }
                        }
                        word.setExamples(examples);
                    }

                    word.setKanjis(kanjis);
                    word.setKanas(kanas);
                    word.setTranslations(translations);

                    Integer level = levelMap.get(word.getId());

                    if (level != null && !word.hasOnlyUncommons() && !skipThisWord) {
                        word.setLevel(level);
                        words.add(word);
                    }
                    skipThisWord = false;
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
