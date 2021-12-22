package org.auioc.mods.ahutils.api.function;

@FunctionalInterface
public interface FunctionEx<T, R> {

    R apply(T t) throws Exception;

}
