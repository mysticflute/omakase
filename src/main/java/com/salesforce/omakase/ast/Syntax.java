﻿/**
 * ADD LICENSE
 */
package com.salesforce.omakase.ast;

import java.util.List;

import javax.annotation.concurrent.Immutable;

/**
 * TODO Description
 * 
 * @author nmcwilliams
 */
@Immutable
public interface Syntax {
    /**
     * TODO Description
     * 
     * @return TODO
     */
    int line();

    /**
     * TODO Description
     * 
     * @return TODO
     */
    int column();

    /**
     * TODO Description
     * 
     * @return TODO
     */
    List<String> comments();

    /**
     * TODO Description
     * 
     * @return TODO
     */
    List<String> ownComments();
}
