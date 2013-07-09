package ru.hh.spellcorrector.morpher;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.PeekingIterator;
import ru.hh.spellcorrector.Correction;
import ru.hh.spellcorrector.Phrase;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

class SplitDelimiter extends Morpher {

  private static final Set<Character> DELIMITERS = ImmutableSet.of(
      '[', ']', '{', '}', ';', '\'', ':', '"', ',', '.', '<', '>'
  );

  private static final SplitDelimiter INSTANCE = new SplitDelimiter();

  private SplitDelimiter() {}

  static SplitDelimiter instance() {
    return INSTANCE;
  }

  @Override
  public Iterable<Correction> corrections(Correction source) {
    Phrase phrase = source.getPhrase();
    if (!phrase.singeWord()) {
      return Collections.singleton(source);
    }
    Phrase parsed = parseString(phrase.getWord(0));
    if (parsed.singeWord()) {
      return Collections.singleton(source);
    }
    return Arrays.asList(source, Correction.of(parsed, source.getWeight()));
  }

  private Phrase parseString(String source) {
    List<String> spaces = Lists.newArrayList();
    List<String> words = Lists.newArrayList();

    PeekingIterator<Character> characterIt = Iterators.peekingIterator(Lists.charactersOf(source).iterator());
    while (characterIt.hasNext()) {
      StringBuilder space = new StringBuilder();
      while (characterIt.hasNext() && DELIMITERS.contains(characterIt.peek())) {
        space.append(characterIt.next());
      }
      spaces.add(space.toString());

      StringBuilder word = new StringBuilder();
      while (characterIt.hasNext() && !DELIMITERS.contains(characterIt.peek())) {
        word.append(characterIt.next());
      }
      words.add(word.toString());
    }

    StringBuilder space = new StringBuilder();
    while (characterIt.hasNext() && DELIMITERS.contains(characterIt.peek())) {
      space.append(characterIt.next());
    }
    spaces.add(space.toString());

    return Phrase.of(words, spaces);
  }
}