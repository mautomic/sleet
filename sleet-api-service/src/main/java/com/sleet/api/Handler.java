package com.sleet.api;

/**
 * A generic handler interface
 */
public interface Handler<E> {
    void handle(E item);
}
