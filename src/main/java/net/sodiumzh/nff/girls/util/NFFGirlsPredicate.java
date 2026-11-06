package net.sodiumzh.nff.girls.util;

import net.minecraft.network.chat.Component;
import net.sodiumzh.nfu.function.NFUOperator;
import net.sodiumzh.nfu.object.NFUJsonParser;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class NFFGirlsPredicate<T> extends NFUOperator<T, Boolean> {

    public NFFGirlsPredicate(Class<T> subjectClass, Map<String, NFUJsonParser<?>> argParserMap, BiPredicate<T, Map<String, Object>> predicate) {
        super(subjectClass, Boolean.class, argParserMap, predicate::test);
    }

    public static <T> NFFGirlsPredicate<T> simple(Class<T> subjectClass, Predicate<T> predicate) {
        return new NFFGirlsPredicate<>(subjectClass, Map.of(), (t, map) -> predicate.test(t));
    }

    private String translation = "";
    private String name = "";

    public String getName() {
        return name;
    }

    public NFFGirlsPredicate<T> setName(String name) {
        this.name = name;
        return this;
    }

    public Component getTranslation() {
        return NFUInfoStatics.createTranslatable(translation);
    }

    public NFFGirlsPredicate<T> setTranslation(String translation) {
        this.translation = translation;
        return this;
    }
}
