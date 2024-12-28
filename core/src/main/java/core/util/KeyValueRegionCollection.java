package core.util;

import java.util.Collections;
import java.util.List;

/**
 * A collection of {@link KeyValueRegion}s.
 *
 * @param <K> The type of the key.
 * @param <V> The type of the value.
 * @param <R> The type of the region.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see KeyValueRegion
 * @since 1.0.0
 */
public class KeyValueRegionCollection<K, V, R extends KeyValueRegion<K, V>> {

	protected final List<R> regions;
	private       String  name;

	/**
	 * Creates a new collection of {@link KeyValueRegion}s.
	 *
	 * @param name    The name of the collection.
	 * @param regions The regions of the collection.
	 */
	public KeyValueRegionCollection(String name, List<R> regions) {
		this.name = name;
		this.regions = regions;
	}

	/**
	 * Get a region by its name.
	 *
	 * @param name The name of the region.
	 * @return The region with the name.
	 */
	public R getRegion(String name) {
		return getRegions().stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
	}

	/**
	 * Get a region by its index.
	 *
	 * @param index The index of the region.
	 * @return The region at the index.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 */
	public R getRegion(int index) throws IndexOutOfBoundsException {
		return getRegions().get(index);
	}

	//region setter/getter
	/**
	 * Get the name of the collection.
	 *
	 * @return The name of the collection.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the collection.
	 *
	 * @param name The name of the collection.
	 */
	protected void setName(String name) {
		this.name = name;
	}

	/**
	 * Get all regions of the ini file.
	 *
	 * @return An unmodifiable list of all regions.
	 */
	public List<R> getRegions() {
		return Collections.unmodifiableList(regions);
	}

	/**
	 * Get the number of regions in the ini file.
	 *
	 * @return The number of regions.
	 */
	public int getRegionCount() {
		return regions.size();
	}
//endregion
}
