﻿/**
 * ADD LICENSE
 */
package com.salesforce.omakase.ast;

import java.util.List;

/**
 * TODO Description
 * 
 * @author nmcwilliams
 */
public interface Stylesheet {
    /**
     * TODO Description
     * 
     * @param rule
     *            TODO
     * @return TODO
     */
    Stylesheet rule(Rule rule);

    /**
     * TODO Description
     * 
     * @return TODO
     */
    List<? extends Rule> rules();
}