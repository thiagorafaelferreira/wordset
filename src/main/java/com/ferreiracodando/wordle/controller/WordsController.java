package com.ferreiracodando.wordle.controller;

import com.ferreiracodando.wordle.service.WordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.io.IOException;
import java.util.Set;

@RestController
@Validated
public class WordsController {

    @Autowired
    private WordsService wordsService;

    @GetMapping("/words/length/{language}")
    public ResponseEntity<String> word(@PathVariable("language") @Valid @Max(3) Integer language) throws IOException {
        return ResponseEntity
                .ok()
                .body(wordsService.randomWord(language.toString()));
    }

    @GetMapping("/words/random/{language}")
    public ResponseEntity<String> random(@PathVariable("language") String language) throws IOException {
        return ResponseEntity
                .ok()
                .body(wordsService.randomWord(language));
    }

    @GetMapping("/words/random/{word_size}/{language}")
    public ResponseEntity<String> randomBySize(
            @PathVariable("word_size")  Integer size,
            @PathVariable("language") String language) throws IOException {
        return ResponseEntity
                .ok()
                .body(wordsService.randomWordBySize(size, language));
    }

    @GetMapping("/words/set/size/{number_letters}/{language}")
    public ResponseEntity<Set<String>> setByNumberLetters(@PathVariable("number_letters") Integer numberLetters,
                                                          @PathVariable("language") String language) throws IOException {
        return ResponseEntity
                .ok()
                .body(wordsService.generateSetByNumberLetters(numberLetters, language));
    }

    @GetMapping("/words/set/letters/{letters}/{language}")
    public ResponseEntity<Set<String>> setByLetters(@PathVariable("letters") String lettersRequest,
                                                    @PathVariable("language") String language) throws IOException {
        return ResponseEntity
                .ok()
                .body(wordsService.generateSetByLetters(lettersRequest, language));
    }
}
