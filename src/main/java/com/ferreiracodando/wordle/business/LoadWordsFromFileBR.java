package com.ferreiracodando.wordle.business;

import com.google.common.collect.Lists;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@Data
public class LoadWordsFromFileBR {

    @Value("classpath:br-words.txt")
    public Resource resourceFileBR;

    @Value("classpath:en-words.txt")
    public Resource resourceFileEN;

    private Map<String, List<String>> languageWords;

    private Map<String, Set<String>> languageDistinctWords;

    public LoadWordsFromFileBR process() {
        System.out.println("LoadWordsFromFileBR");
        languageWords = new HashMap<>();
        languageDistinctWords = new HashMap<>();
        List<Resource> resources = Lists.newArrayList(resourceFileBR, resourceFileEN);

        resources.stream().forEach(resource -> {
            List<String >words = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String lowercase = line.toLowerCase(Locale.ROOT);
                    String normalize = Normalizer.normalize(lowercase, Normalizer.Form.NFD);
                    String replace = normalize.replaceAll("[^\\p{ASCII}]", "");
                    words.add(replace);
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                log.error(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                log.error(e.getMessage());
            }

            try {
                String language = resource.getFile().getName().substring(0,2);
                languageWords.put(language, words);
                Set<String> distinctWords = words.stream().collect(Collectors.toSet());
                languageDistinctWords.put(language, distinctWords);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return this;
    }

    public Integer numberWords(String language) {
        if(languageWords == null) {
            languageWords = new HashMap<>();
        }

        return languageWords.get(language).size();
    }

    public List<String> getLanguageWords(String language) {
        return this.languageWords.get(language);
    }

    public Set<String> getLanguageDistinctWords(String language) {
        return this.languageDistinctWords.get(language);
    }
}
