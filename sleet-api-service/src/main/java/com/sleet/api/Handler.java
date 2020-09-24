package com.sleet.api;

/**
 * A generic handler interface
 *
 * @author mautomic
 */
public interface Handler<E> {
    void handle(E item);
}
