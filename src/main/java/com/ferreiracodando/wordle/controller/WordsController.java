package com.ferreiracodando.wordle.controller;

import com.ferreiracodando.wordle.service.WordsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/v1/words")
public class WordsController {

    private final WordsService wordsService;

    @GetMapping("/random/{language}")
    public ResponseEntity<String> random(@PathVariable("language") String language) throws IOException {
        System.out.println("WordsController#random");
        return ResponseEntity
                .ok()
                .body(wordsService.randomWord(language));
    }

    @GetMapping("/random/{word_size}/{language}")
    public ResponseEntity<String> randomBySize(
            @PathVariable("word_size")  Integer size,
            @PathVariable("language") String language) throws IOException {
        System.out.println("WordsController#randomBySize");
        return ResponseEntity
                .ok()
                .body(wordsService.randomWordBySize(size, language));
    }

    @GetMapping("/set/size/{number_letters}/{language}")
    public ResponseEntity<Set<String>> setByNumberLetters(@PathVariable("number_letters") Integer numberLetters,
                                                          @PathVariable("language") String language) throws IOException {
        System.out.println("WordsController#setByNumberLetters");
        return ResponseEntity
                .ok()
                .body(wordsService.generateSetByNumberLetters(numberLetters, language));
    }

    @GetMapping("/set/letters/{letters}/{language}")
    public ResponseEntity<Set<String>> setByLetters(@PathVariable("letters") String lettersRequest,
                                                    @PathVariable("language") String language) throws IOException {
        System.out.println("WordsController#setByLetters");
        return ResponseEntity
                .ok()
                .body(wordsService.generateSetByLetters(lettersRequest, language));
    }
}

