package com.tomokanji.repositories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomokanji.model.Kana;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class KatakanaRepositoryImpl implements KatakanaRepository{
    private List<Kana> katakanas = new ArrayList<>();

    @Value("${katakanaPath}")
    private String katakanaPath;

    @PostConstruct
    public void init() {
        loadKatakanas();
    }

    @Override
    public List<Kana> getKatakanas() {
        return katakanas;
    }

    @Override
    public List<Kana> getKatakanasByQuery(String query) {
        List<Kana> filteredKatakanas = katakanas.stream().filter(kanji ->
                        kanji.getKana().equals(query) ||
                        kanji.getRomaji().contains(query)
        ).collect(Collectors.toList());
        return filteredKatakanas;
    }

    @Override
    public List<Kana> findKatakanasByIds(List<Integer> katakanaIds) {
        List<Kana> filteredKatakanas = katakanas.stream().filter(katakana -> katakanaIds.contains(katakana.getId()))
                .collect(Collectors.toList());
        return filteredKatakanas;
    }

    private void loadKatakanas() {
        System.out.println("Loading katakanas...");
        List<Kana> katakanas = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(
                    new File(katakanaPath)
            );

            for (JsonNode node : rootNode) {
                int id = node.get("id").asInt();
                String kana = node.get("kana").asText();
                String romaji = node.get("romaji").asText();

                Kana katakana = new Kana(id, kana, romaji);
                katakanas.add(katakana);
            }
            
            System.out.println("Katakanas loaded: " + katakanas.size());
        } catch (Exception ignored) {
            System.out.println("Error loading katakanas");
        }
        this.katakanas = katakanas;
    }
}
