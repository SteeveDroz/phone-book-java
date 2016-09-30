package com.github.steevedroz.phonebookjava;

/**
 * This interface must be implemented by a class that wants to be triggered by
 * events from {@link PhoneBook}.
 * 
 * @author Steeve Droz
 *
 */
public interface PhoneBookSubscriber {
    /**
     * This method is called when the given keyword has been sent by any object in
     * the same application.
     * 
     * @param keyword
     *            The keyword that had the object notified
     */
    public void notify(String keyword);
}
