package ru.hh.spellcorrector.morpher;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Map;
import java.util.Set;

class Keyboard extends StringTransform {

  public Keyboard(double multiplier) {
    super(multiplier);
  }

  private static final ImmutableMap<Character, Character> TO_RUS;
  private static final ImmutableMap<Character, Character> TO_ENG;

  static {
    String rus = "йцукенгшщзхъфывапролджэячсмитьбю.ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ.";
    String eng = "qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?";
    Map<Character, Character> toEng = Maps.newHashMap();
    Map<Character, Character> toRus = Maps.newHashMap();
    for (int i = 0; i < rus.length(); i++) {
      toEng.put(rus.charAt(i), eng.charAt(i));
      toRus.put(eng.charAt(i), rus.charAt(i));
    }

    TO_RUS = ImmutableMap.copyOf(toRus);
    TO_ENG = ImmutableMap.copyOf(toEng);
  }

  public String toAnotherCharset(String from, Map<Character, Character> transform) {
    char[] result = new char[from.length()];
    for (int i = 0; i < from.length(); i++) {
      Character c = transform.get(from.charAt(i));
      result[i] = c != null ? c : from.charAt(i);
    }
    return new String(result);
  }

  @Override
  public Iterable<String> variants(final String source) {
    Set<String> result = Sets.newHashSetWithExpectedSize(3);
    result.add(source);
    result.add(toAnotherCharset(source, TO_RUS));
    result.add(toAnotherCharset(source, TO_ENG));
    return result;
  }
}
