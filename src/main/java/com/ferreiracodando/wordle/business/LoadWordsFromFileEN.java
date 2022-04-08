package com.ferreiracodando.wordle.business;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@Data
public class LoadWordsFromFileEN {

    @Value("classpath:en-words.txt")
    public Resource resourceFile;

    private List<String> words;

    private Set<String> distinctWords;

    public LoadWordsFromFileEN process() {
        words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(resourceFile.getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String lowercased = line.toLowerCase(Locale.ROOT);
                String normalized = Normalizer.normalize(lowercased, Normalizer.Form.NFD);
                String normalizedReplaced = normalized.replaceAll("[^\\p{ASCII}]", "");
                words.add(normalizedReplaced);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            log.error(e.getMessage());
        }

        distinctWords = words.stream().collect(Collectors.toSet());

        return this;
    }

    public Integer numberWords() {
        if(words == null) {
            words = new ArrayList<>();
        }

        return words.size();
    }
}
