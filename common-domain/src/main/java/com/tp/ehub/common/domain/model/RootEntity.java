package com.tp.ehub.common.domain.model;

/**
 * The <code>RootEntity</code> represents the state of an <code>Aggregate</code>
 * as this is reduced from all affecting events
 * <p>
 * We consider as granted that the entity has a unique id that can be used to
 * save and retrieve it safely from an data store
 * </p>
 * 
 * @param <K>
 *            the type of this root entity's unique identifier
 */
public interface RootEntity<K> extends Identifiable<K> {

}