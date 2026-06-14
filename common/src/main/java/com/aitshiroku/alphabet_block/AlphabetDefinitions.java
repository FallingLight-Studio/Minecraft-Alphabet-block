package com.aitshiroku.alphabet_block;

import java.util.ArrayList;
import java.util.List;

public final class AlphabetDefinitions {

    public enum CharacterType {
        CONSONANT,
        VOWEL,
        TONE,
    }

    public record CharacterDef(
        String id,
        String thaiSymbol,
        String englishName,
        CharacterType type
    ) {}

    public static final List<CharacterDef> CONSONANTS = List.of(
        new CharacterDef("empty_block", "", "Empty Block", CharacterType.CONSONANT),
        new CharacterDef("letter_a", "A", "Letter A", CharacterType.CONSONANT),
        new CharacterDef("letter_b", "B", "Letter B", CharacterType.CONSONANT),
        new CharacterDef("letter_c", "C", "Letter C", CharacterType.CONSONANT),
        new CharacterDef("letter_d", "D", "Letter D", CharacterType.CONSONANT),
        new CharacterDef("letter_e", "E", "Letter E", CharacterType.CONSONANT),
        new CharacterDef("letter_f", "F", "Letter F", CharacterType.CONSONANT),
        new CharacterDef("letter_g", "G", "Letter G", CharacterType.CONSONANT),
        new CharacterDef("letter_h", "H", "Letter H", CharacterType.CONSONANT),
        new CharacterDef("letter_i", "I", "Letter I", CharacterType.CONSONANT),
        new CharacterDef("letter_j", "J", "Letter J", CharacterType.CONSONANT),
        new CharacterDef("letter_k", "K", "Letter K", CharacterType.CONSONANT),
        new CharacterDef("letter_l", "L", "Letter L", CharacterType.CONSONANT),
        new CharacterDef("letter_m", "M", "Letter M", CharacterType.CONSONANT),
        new CharacterDef("letter_n", "N", "Letter N", CharacterType.CONSONANT),
        new CharacterDef("letter_o", "O", "Letter O", CharacterType.CONSONANT),
        new CharacterDef("letter_p", "P", "Letter P", CharacterType.CONSONANT),
        new CharacterDef("letter_q", "Q", "Letter Q", CharacterType.CONSONANT),
        new CharacterDef("letter_r", "R", "Letter R", CharacterType.CONSONANT),
        new CharacterDef("letter_s", "S", "Letter S", CharacterType.CONSONANT),
        new CharacterDef("letter_t", "T", "Letter T", CharacterType.CONSONANT),
        new CharacterDef("letter_u", "U", "Letter U", CharacterType.CONSONANT),
        new CharacterDef("letter_v", "V", "Letter V", CharacterType.CONSONANT),
        new CharacterDef("letter_w", "W", "Letter W", CharacterType.CONSONANT),
        new CharacterDef("letter_x", "X", "Letter X", CharacterType.CONSONANT),
        new CharacterDef("letter_y", "Y", "Letter Y", CharacterType.CONSONANT),
        new CharacterDef("letter_z", "Z", "Letter Z", CharacterType.CONSONANT)
    );

    public static final List<CharacterDef> VOWELS = List.of();

    public static final List<CharacterDef> TONES = List.of();

    private AlphabetDefinitions() {}

    public static List<CharacterDef> all() {
        List<CharacterDef> allEntries = new ArrayList<>(
            CONSONANTS.size() + VOWELS.size() + TONES.size()
        );
        allEntries.addAll(CONSONANTS);
        allEntries.addAll(VOWELS);
        allEntries.addAll(TONES);
        return List.copyOf(allEntries);
    }
}
