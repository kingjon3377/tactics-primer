package controller;

import java.util.BitSet;

/**
 * A class to register IDs with and produce not-yet-used IDs.
 *
 * @author Jonathan Lovelace
 *
 */
public final class IDFactory {
	/**
	 * An instance of this class.
	 */
	public static final IDFactory FACTORY = new IDFactory();
	/**
	 * The set of IDs used already.
	 */
	private final BitSet usedIDs = new BitSet();

	/**
	 * An exception to warn about duplicate IDs.
	 */
	public static class DuplicateIDException extends Exception {
		/**
		 * @param idNum the duplicate ID.
		 */
		public DuplicateIDException(final int idNum) {
			super("Duplicate ID #" + idNum);
		}
	}
	/**
	 * This should probably only be called from the IDFactoryFiller.
	 * @param idNum an ID number.
	 * @return whether it's used.
	 */
	public boolean used(final int idNum) {
		return idNum < 0 || usedIDs.get(idNum);
	}
	/**
	 * Register an ID.
	 *
	 * @param idNum the ID to register.
	 * @return the id, so this can be used functionally.
	 * @throws DuplicateIDException if ID is already in use
	 */
	public synchronized int register(final int idNum) throws DuplicateIDException {
		if (idNum >= 0) {
			if (usedIDs.get(idNum)) {
				throw new DuplicateIDException(idNum);
			}
			usedIDs.set(idNum);
		}
		return idNum;
	}

	/**
	 * Generate and register an id that hasn't been previously registered.
	 *
	 * @return the generated id
	 */
	public synchronized int createID() {
		if (usedIDs.cardinality() < Integer.MAX_VALUE) {
			final int idNum = usedIDs.nextClearBit(0);
			usedIDs.set(idNum);
			return idNum;
		} else {
			throw new IllegalStateException("Exhausted all ints ...");
		}
	}

	/**
	 * Create a copy of this factory for testing purposes. (So that we don't
	 * "register" IDs that don't end up getting used.)
	 *
	 * @return a copy of this factory
	 */
	public IDFactory copy() {
		final IDFactory retval = new IDFactory();
		retval.usedIDs.or(usedIDs);
		return retval;
	}

	/**
	 * @return a String representation of the object
	 */
	@Override
	public String toString() {
		return "IDFactory";
	}
}
