package com.bdj.bot_discord.mascarade_bot.utils;

import org.jetbrains.annotations.NotNull;

public class LoopList<E extends Object> {
    private Node current;
    private int size = 0;

    public void push(@NotNull E element){
        if(current == null){
            current = new Node(element);
            current.next = current;
        }
        Node newOne = new Node(current.element,current.next);
        current.element=element;
        current.next = newOne;
        current = newOne;
        size++;
    }

    public boolean remove(E element){
        Node toRemove = find(element);
        if (toRemove == null) return false;
        size--;
        if (size == 1 && toRemove == current) {
            current = null;
            return true;
        }
        toRemove.element = toRemove.next.element;
        toRemove.next = toRemove.next.next;
        return true;
    }

    private Node find(E element){
        if (current == null) return null;
        if (current.element.equals(element)) return  current;
        Node next = current.next;
        while (next != current){
            if(next.element.equals(element)) return next;
            next = current.next;
        }
        return null;
    }

    public E next(){
        current = current.next;
        return current.element;
    }

    public E peekNext(){
        return current.next.element;
    }

    private class Node {
        E element;
        Node next;

        Node(){}
        Node(E element, Node next){
            this.element = element;
            this.next = next;
        }
        Node(Node next){
            this.next = next;
        }
        Node(E element){
            this.element = element;
        }
    }
}
