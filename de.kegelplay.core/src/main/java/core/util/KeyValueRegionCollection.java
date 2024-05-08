package core.util;

import java.util.Collections;
import java.util.List;

public class KeyValueRegionCollection<K, V, R extends KeyValueRegion<K, V>> {

	private final List<R> regions;
	private       String  name;

	public KeyValueRegionCollection(String name, List<R> regions) {
		this.name = name;
		this.regions = regions;
	}

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
	public String getName() {
		return name;
	}

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
