package com.ferreiracodando.wordle.service;

import com.ferreiracodando.wordle.business.LoadWordsFromFileBR;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class WordsService {

    private final LoadWordsFromFileBR loadWordsFromFile;

    public WordsService(LoadWordsFromFileBR loadWordsFromFile) {
        this.loadWordsFromFile = loadWordsFromFile;
        this.loadWordsFromFile.process();
    }

    public String randomWord(String language) {
        int chosenNumber = gererateRandomWordChoice(this.loadWordsFromFile.numberWords(language));
        return this.loadWordsFromFile.getLanguageWords(language).get(chosenNumber);
    }

    public String randomWordBySize(Integer wordSize, String language) {
        List<String> wordsWithDifinedSize = wordsBySize(wordSize, language);
        Integer positionChoose = gererateRandomWordChoice(wordsWithDifinedSize.size());
        String chooseWord = wordsWithDifinedSize.get(positionChoose);
        return chooseWord;
    }

    public Set<String> generateSetByNumberLetters(Integer numberLetters, String language) {
        List<String> wordsWithDifinedSize = wordsBySize(numberLetters, language);
        Integer positionChoose = gererateRandomWordChoice(wordsWithDifinedSize.size());
        AtomicReference<String> chooseWord = new AtomicReference<>(wordsWithDifinedSize.get(positionChoose));

        List<String> matchedWords = new ArrayList<>();

        IntStream.range(0, numberLetters).forEach(i -> {
            List<String> letters = new ArrayList<>();
            for(char character: chooseWord.get().toCharArray()) {
                letters.add(String.valueOf(character));
                if(letters.size() >= 3) {
                    matchedWords.addAll(genaratePermutationsAndMatchWithDictionary(letters, language));
                }
            }
            chooseWord.set(StringUtils.rotate(chooseWord.get(), 1));
        });

        Set<String> distinctWords = matchedWords.stream().collect(Collectors.toSet());

        return distinctWords;
    }

    public Set<String> generateSetByLetters(String lettersRequest, String language) {
        AtomicReference<String> chooseWord = new AtomicReference<>(lettersRequest);

        List<String> matchedWords = new ArrayList<>();

        IntStream.range(0, lettersRequest.length()).forEach(i -> {
            List<String> letters = new ArrayList<>();
            for(char character: chooseWord.get().toCharArray()) {
                letters.add(String.valueOf(character));
                if(letters.size() >= 3) {
                    matchedWords.addAll(genaratePermutationsAndMatchWithDictionary(letters, language));
                }
            }
            chooseWord.set(StringUtils.rotate(chooseWord.get(), 1));
        });

        Set<String> distinctWords = matchedWords.stream().collect(Collectors.toSet());

        return distinctWords;
    }

    private List<String> genaratePermutationsAndMatchWithDictionary(List<String> letters, String language) {
        Set<String> listToMatch = this.loadWordsFromFile.getLanguageDistinctWords(language);

        Collection<List<String>> permutations = Collections2.permutations(letters);

        List<String> matchedWords = permutations.stream()
                .filter(permutation -> listToMatch.contains(Joiner.on("").join(permutation)))
                .map(permutation -> Joiner.on("").join(permutation))
                .collect(Collectors.toList());

        return matchedWords;
    }

    private Integer gererateRandomWordChoice(Integer listSize) {
        return new Random().nextInt(listSize);
    }

    private List<String> wordsBySize(Integer size, String language) {
        return this.loadWordsFromFile.getLanguageWords(language)
                .stream()
                .filter(word -> word.length() == size)
                .collect(Collectors.toList());
    }

}
