package net.sodiumzh.nff.girls.util;

import net.minecraft.network.chat.Component;
import net.sodiumzh.nfu.function.NFUPredicate;
import net.sodiumzh.nfu.util.NFUInfoStatics;

import java.util.function.Predicate;

public class NFFGirlsPredicate<T> extends NFUPredicate<T> {

    public NFFGirlsPredicate(Class<T> subjectClass, Predicate<T> predicate) {
        super(subjectClass, predicate);
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
