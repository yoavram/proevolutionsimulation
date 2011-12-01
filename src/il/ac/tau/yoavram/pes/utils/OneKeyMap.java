package il.ac.tau.yoavram.pes.utils;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

public class OneKeyMap<K, V> implements Map<K, V>, Serializable {
	private static final long serialVersionUID = 4544260190954733366L;

	private K k = null;
	private int hash = 0;
	private V v = null;

	@Override
	public int size() {
		if (k == null)
			return 0;
		else
			return 1;
	}

	@Override
	public boolean isEmpty() {
		return k == null;
	}

	@Override
	public boolean containsKey(Object key) {
		return k != null && key.hashCode() == hash;
	}

	@Override
	public boolean containsValue(Object value) {
		return v == value;
	}

	@Override
	public V get(Object key) {
		if (containsKey(key))
			return v;
		else
			return null;
	}

	@Override
	public V put(K key, V value) {
		hash = key.hashCode();
		k = key;
		V tmp = v;
		v = value;
		return tmp;
	}

	@Override
	public V remove(Object key) {
		if (containsKey(key)) {
			V tmp = v;
			clear();
			return tmp;
		} else {
			return null;
		}

	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("Can't put multiple values");

	}

	@Override
	public void clear() {
		hash = 0;
		v = null;
		k = null;
	}

	@Override
	public Set<K> keySet() {
		Set<K> s = Sets.newHashSet();
		s.add(k);
		return s;
	}

	@Override
	public Collection<V> values() {
		Collection<V> c = Sets.newHashSet();
		c.add(v);
		return c;
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		Map.Entry<K, V> e = new AbstractMap.SimpleEntry<K, V>(k, v);
		Set<Map.Entry<K, V>> set = Sets.newHashSet();
		set.add(e);
		return set;
	}

}
