package Parsers;

import java.text.ParseException;

public interface Parser<T> {
    T fromString(String value) throws ParseException;
}
