package com.tomokanji.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.Kanji;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class KanjiRepositoryImpl implements KanjiRepository{
    private List<Kanji> kanjis = new ArrayList<>();

    @Value("${kanjiPath}")
    private String kanjiPath;

    @PostConstruct
    public void init() {
        loadKanjis();
    }

    @Override
    public List<Kanji> getKanjis() {
        return kanjis;
    }

    @Override
    public List<Kanji> getKanjisByLevel(int level) {
        List<Kanji> filteredKanjis = kanjis.stream().filter(kanji -> kanji.getLevel() == level)
                .collect(Collectors.toList());
        return filteredKanjis;
    }

    @Override
    public List<Kanji> getKanjisByQuery(String query) {
        List<Kanji> filteredKanjis = kanjis.stream().filter(kanji ->
                        kanji.getCharacter().equals(query) ||
                        kanji.getMeanings().contains(query) ||
                        kanji.getOnyomi().contains(query) ||
                        kanji.getKunyomi().contains(query)
        ).collect(Collectors.toList());
        return filteredKanjis;
    }

    @Override
    public List<Kanji> findKanjisByIds(List<Integer> kanjiIds) {
        List<Kanji> filteredKanjis = kanjis.stream().filter(kanji -> kanjiIds.contains(kanji.getId()))
                .collect(Collectors.toList());
        return filteredKanjis;
    }

    private void loadKanjis() {
        System.out.println("Loading kanjis...");
        List<Kanji> kanjis = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(
                    new File(kanjiPath)
            );

            Iterator<String> fieldNames = rootNode.fieldNames();
            while (fieldNames.hasNext()) {
                String character = fieldNames.next();
                JsonNode kanjiNode = rootNode.get(character);

                int level = kanjiNode.get("jlpt_new").asInt();

                int strokes = kanjiNode.get("strokes").asInt();
                List<String> meanings = getCleanedList(kanjiNode.get("wk_meanings"));
                List<String> onyomi = getCleanedList(kanjiNode.get("wk_readings_on"));
                List<String> kunyomi = getCleanedList(kanjiNode.get("wk_readings_kun"));

                Kanji kanji = new Kanji();
                kanji.setId(kanjis.size() + 1);
                kanji.setCharacter(character);
                kanji.setLevel(level);
                kanji.setMeanings(meanings);
                kanji.setOnyomi(onyomi);
                kanji.setKunyomi(kunyomi);
                kanji.setStrokes(strokes);

                kanjis.add(kanji);
            }
            System.out.println("Kanjis loaded: " + kanjis.size());
        } catch (Exception ignored) {
            System.out.println("Error loading kanjis");
        }
        this.kanjis = kanjis;
    }

    private List<String> getCleanedList(JsonNode jsonNode) {
        List<String> list = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode node : jsonNode) {
                String value = node.asText();
                // Remove '!' or '^' from the beginning
                if (value.startsWith("!") || value.startsWith("^")) {
                    value = value.substring(1);
                }
                list.add(value);
            }
        }
        return list;
    }
}
