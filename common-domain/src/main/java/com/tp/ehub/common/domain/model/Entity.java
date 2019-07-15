package com.tp.ehub.common.domain.model;

/**
 * The <code>Entity</code> represents an object that is not fundamentally
 * defined by its attributes, but rather by a thread of continuity and identity.
 * <p>
 * We consider as granted that the entity has a unique id that can be used to
 * save and retrieve it safely from an data store
 * </p>
 * 
 * @param <K>
 *            the type of this root entity's unique identifier
 */
public interface Entity<K> extends Identifiable<K> {

}