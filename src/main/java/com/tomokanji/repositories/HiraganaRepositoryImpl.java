package com.tomokanji.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.Kana;
import com.tomokanji.model.Kanji;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HiraganaRepositoryImpl implements HiraganaRepository{
    private List<Kana> hiraganas = new ArrayList<>();

    @Value("${hiraganaPath}")
    private String hiraganaPath;

    @PostConstruct
    public void init() {
        loadHiraganas();
    }

    @Override
    public List<Kana> getHiraganas() {
        return hiraganas;
    }

    @Override
    public List<Kana> getHiraganasByQuery(String query) {
        List<Kana> filteredHiraganas = hiraganas.stream().filter(kanji ->
                        kanji.getKana().equals(query) ||
                        kanji.getRomaji().contains(query)
        ).collect(Collectors.toList());
        return filteredHiraganas;
    }

    @Override
    public List<Kana> findHiraganasByIds(List<Integer> hiraganaIds) {
        List<Kana> filteredHiraganas = hiraganas.stream().filter(hiragana -> hiraganaIds.contains(hiragana.getId()))
                .collect(Collectors.toList());
        return filteredHiraganas;
    }

    private void loadHiraganas() {
        System.out.println("Loading hiraganas...");
        List<Kana> hiraganas = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(
                    new File(hiraganaPath)
            );

            for (JsonNode node : rootNode) {
                int id = node.get("id").asInt();
                String kana = node.get("kana").asText();
                String romaji = node.get("romaji").asText();

                Kana hiragana = new Kana(id, kana, romaji);
                hiraganas.add(hiragana);
            }

            System.out.println("Hiraganas loaded: " + hiraganas.size());
        } catch (Exception ignored) {
            System.out.println("Error loading hiraganas");
        }
        this.hiraganas = hiraganas;
    }
}
