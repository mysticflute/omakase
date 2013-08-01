﻿/**
 * ADD LICENSE
 */
package com.salesforce.omakase;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * TODO Description
 * 
 * @author nmcwilliams
 */
public final class Util {
    private Util() {}

    /**
     * TODO Description
     * 
     * @param <T>
     *            TODO
     * @param original
     *            TODO
     * @return TODO
     */
    public static <T> ImmutableList<T> immutable(List<T> original) {
        return original == null ? ImmutableList.<T>of() : ImmutableList.copyOf(original);
    }
}
