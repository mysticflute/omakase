﻿/**
 * ADD LICENSE
 */
package com.salesforce.omakase.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.salesforce.omakase.ast.Declaration;
import com.salesforce.omakase.ast.selector.SelectorGroup;

/**
 * TODO Description
 * 
 * @author nmcwilliams
 */
public class EchoLogger implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(EchoLogger.class);

    @Override
    public void comment(String comment) {
        logger.info("comment: {}", comment);
    }

    @Override
    public void selectorGroup(SelectorGroup selectors) {
        logger.info("selectors: {}", selectors);
    }

    @Override
    public void declaration(Declaration declaration) {
        logger.info("declaration: {}", declaration);
    }

}