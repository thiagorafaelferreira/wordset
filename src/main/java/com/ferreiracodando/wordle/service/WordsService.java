package com.ferreiracodando.wordle.service;

import com.ferreiracodando.wordle.business.LoadWordsFromFileBR;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class WordsService {

    private final LoadWordsFromFileBR loadWordsFromFile;

    public WordsService(LoadWordsFromFileBR loadWordsFromFile) {
        this.loadWordsFromFile = loadWordsFromFile;
        System.out.println("WordsService#WordsService");
        System.out.println(loadWordsFromFile);
        this.loadWordsFromFile.process();
    }

    public String randomWord(String language) {
        System.out.println("WordsService#randomWord");
        int chosenNumber = gererateRandomWordChoice(this.loadWordsFromFile.numberWords(language));
        return this.loadWordsFromFile.getLanguageWords(language).get(chosenNumber);
    }

    public String randomWordBySize(Integer wordSize, String language) {
        System.out.println("WordsService#randomWordBySize");
        List<String> wordsWithDifinedSize = wordsBySize(wordSize, language);
        Integer positionChoose = gererateRandomWordChoice(wordsWithDifinedSize.size());
        String chooseWord = wordsWithDifinedSize.get(positionChoose);
        return chooseWord;
    }

    public Set<String> generateSetByNumberLetters(Integer numberLetters, String language) {
        System.out.println("WordsService#generateSetByNumberLetters");
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
        System.out.println("WordsService#generateSetByLetters");
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
        System.out.println("WordsService#genaratePermutationsAndMatchWithDictionary");
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
        System.out.println("WordsService#wordsBySize");
        return this.loadWordsFromFile.getLanguageWords(language)
                .stream()
                .filter(word -> word.length() == size)
                .collect(Collectors.toList());
    }

}
