package com.github.steevedroz.phonebookjava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A component that allows any object to notify or call any other object with an
 * event mechanism.
 * 
 * <h1>Notify</h1>
 * 
 * <p>
 * Two or more objects can communicate with each other using 4 simple steps:
 * <ol>
 * <li>The subscriber (i.e. the object that will be called) must implement the
 * {@link PhoneBookSubscriber} interface.</li>
 * <li>The subscriber must register with a keyword with
 * {@link PhoneBook#register(String, PhoneBookSubscriber)}</code>.</li>
 * <li>The caller must then call {@link PhoneBook#notice(String)} with the same
 * keyword.</li>
 * <li>The subscriber will have its {@link PhoneBookSubscriber#notify(String)}
 * method called, thus transmitting the message as a keyword.</li>
 * </ol>
 * </p>
 * 
 * <p>
 * It is possible to subscribe to the same event from two objects at once; in
 * that case, when the caller uses the keyword, all the subscribers will be
 * notified.
 * </p>
 * 
 * <p>
 * It is possible to unregister from a keyword using
 * {@link PhoneBook#unregister(String, PhoneBookSubscriber)}.
 * </p>
 * 
 * <h1>Call</h1>
 * 
 * <p>
 * An object can be called by any other object in the application using 2 simple
 * steps:
 * <ol>
 * <li>The object to call must be added to the entries list with a unique name
 * using {@link PhoneBook#addEntry(String, Object)}.</li>
 * <li>The caller calls it using {@link PhoneBook#call(String)}.</li>
 * </ol>
 * </p>
 * 
 * @author Steeve Droz
 *
 */
public enum PhoneBook {
    /**
     * PhoneBook implements a singleton design pattern using an enumeration.
     * That instance may be accessible, but it is useless as all the methods are
     * static.
     */
    INSTANCE;

    /**
     * The list of registered subscribers for each of the keywords.
     */
    private Map<String, List<PhoneBookSubscriber>> subscribers;
    /**
     * The list of registered objects that can be called anywhere in the
     * application
     */
    private Map<String, Object> entries;

    /**
     * Private constructor of the singleton. Only called once.
     */
    private PhoneBook() {
	subscribers = new HashMap<String, List<PhoneBookSubscriber>>();
	entries = new HashMap<String, Object>();
    }

    /**
     * In order to be notified by a keyword, a {@link PhoneBookSubscriber} must
     * first be registered using this method.
     * 
     * <p>
     * Example code:
     * </p>
     * 
     * <pre>
     * SomeClass receiver = new SomeClass(); // SomeClass implements PhoneBookSubscriber
     * PhoneBook.register("message", receiver);
     * 
     * // In another object:
     * 
     * PhoneBook.notify("message") // calls receiver.notify(message)
     * </pre>
     * 
     * @param keyword
     *            The keyword associated to the subscriber
     * @param subscriber
     *            The subscriber to register
     */
    public static void register(String keyword, PhoneBookSubscriber subscriber) {
	List<PhoneBookSubscriber> subscriberList = INSTANCE.subscribers.get(keyword);
	if (subscriberList == null) {
	    subscriberList = new ArrayList<PhoneBookSubscriber>();
	    INSTANCE.subscribers.put(keyword, subscriberList);
	}
	subscriberList.add(subscriber);
    }

    /**
     * Removes a {@link PhoneBookSubscriber} from the list of subscribers with a
     * given keyword.
     * 
     * @param keyword
     *            The keyword to unregister from
     * @param subscriber
     *            The subscriber that must not be notified by the keyword
     *            anymore
     */
    public static void unregister(String keyword, PhoneBookSubscriber subscriber) {
	List<PhoneBookSubscriber> subscriberList = INSTANCE.subscribers.get(keyword);
	if (subscriberList != null) {
	    subscriberList.remove(subscriber);
	}
    }

    /**
     * When one or more {@link PhoneBookSubscriber Subscribers} have been
     * registered to a given keyword, calling this method from anywhere, with
     * the matching keyword, will notify all those subscribers.
     * 
     * <p>
     * Example code:
     * </p>
     * 
     * <pre>
     * SomeClass receiver = new SomeClass(); // SomeClass implements PhoneBookSubscriber
     * PhoneBook.register("message", receiver);
     * 
     * // In another object:
     * 
     * PhoneBook.notify("message") // calls receiver.notify(message)
     * </pre>
     * 
     * 
     * @param keyword
     *            The keyword to use in order to notify the subscribers
     */
    public static void notice(String keyword) {
	List<PhoneBookSubscriber> subscriberList = INSTANCE.subscribers.get(keyword);
	if (subscriberList != null) {
	    for (PhoneBookSubscriber subscriber : subscriberList) {
		subscriber.notify(keyword);
	    }
	}
    }

    /**
     * In order to call an object, it must be added to the list of entries using
     * this method.
     * 
     * <p>
     * Example code:
     * </p>
     * 
     * <pre>
     * SomeClass someCallableObject = new SomeClass();
     * PhoneBook.addEntry("some-name", someCallableObject);
     * 
     * // In another object:
     * 
     * SomeClass someCallableObject = (SomeClass) PhoneBook.call("some-name");
     * </pre>
     * 
     * @param name
     *            The identifier of the callable object
     * @param callable
     *            The callable object itself
     */
    public static void addEntry(String name, Object callable) {
	if (INSTANCE.entries.get(name) != null) {
	    throw new DuplicateIndexException();
	}
	INSTANCE.entries.put(name, callable);
    }

    /**
     * If an object must not be callable anymore, this method will remove it
     * from the list of entries.
     * 
     * @param name
     *            The identifier of the object to remove from the list
     */
    public static void removeEntry(String name) {
	INSTANCE.entries.remove(name);
    }

    /**
     * Calls the object that matches the name given as parameter.
     * 
     * <p>
     * Note: this object will be returned as an Object and must be casted to the
     * correct class before being used normally.
     * </p>
     * 
     * <p>
     * Example code:
     * </p>
     * 
     * <pre>
     * SomeClass someCallableObject = new SomeClass();
     * PhoneBook.addEntry("some-name", someCallableObject);
     * 
     * // In another object:
     * 
     * // Note the casting
     * SomeClass someCallableObject = (SomeClass) PhoneBook.call("some-name");
     * </pre>
     * 
     * @param name
     *            The identifier of the callable object
     * @return The callable object
     */
    public static Object call(String name) {
	return INSTANCE.entries.get(name);
    }
}
