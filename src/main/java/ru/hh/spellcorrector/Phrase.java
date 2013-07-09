package ru.hh.spellcorrector;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import static java.util.Arrays.asList;

public class Phrase {

  private static final String EMPTY = "";
  private static final String SPACE = " ";

  private final List<String> words;
  private final List<String> space;

  private Phrase(List<String> words, List<String> space) {
    Preconditions.checkState(words.size() + 1 == space.size());
    this.words = words;
    this.space = space;
  }

  public static Phrase of(String word) {
    return new Phrase(asList(word), asList(EMPTY, EMPTY));
  }

  public static Phrase of(List<String> words, List<String> space) {
    return new Phrase(words, space);
  }

  public Phrase replace(String replace, int index) {
    if (singeWord()) {
      return Phrase.of(replace);
    }

    List<String> phrase = Lists.newArrayList(words);
    phrase.set(index, replace);
    return Phrase.of(phrase, space);
  }

  public Phrase replace(Partition partition, int index) {
    if (singeWord()) {
      return Phrase.of(asList(partition.left(), partition.right()), asList(space.get(0), SPACE, space.get(1)));
    }
    List<String> phrase = Lists.newArrayList(words);
    phrase.set(index, partition.left());
    phrase.add(index + 1, partition.right());
    List<String> newSpaces = Lists.newArrayList(space);
    newSpaces.add(index + 1, SPACE);
    return Phrase.of(phrase, newSpaces);
  }

  public boolean singeWord() {
    return words.size() == 1;
  }

  public List<String> getWords() {
    return words;
  }

  public String join() {
    StringBuilder builder = new StringBuilder();

    Iterator<String> spaceIt = space.iterator();
    Iterator<String> wordsIt = words.iterator();

    while (spaceIt.hasNext() || wordsIt.hasNext()) {
      if (spaceIt.hasNext()) {
        builder.append(spaceIt.next());
      }
      if (wordsIt.hasNext()) {
        builder.append(wordsIt.next());
      }
    }

    return builder.toString();
  }

  public int size() {
    return words.size();
  }

  public String getWord(int index) {
    return words.get(index);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    return words.equals(((Phrase) o).words);
  }

  @Override
  public int hashCode() {
    return words.hashCode();
  }

  private static final Joiner JOINER = Joiner.on(" ");

  @Override
  public String toString() {
    return words.size() > 0 ? "\"" + JOINER.join(words) + "\"" : words.iterator().next();
  }
}
