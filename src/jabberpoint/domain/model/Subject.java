package jabberpoint.domain.model;

import java.util.Objects;

/**
 * Value Object pattern: represents the topic of a slide; equality is defined by value.
 * Creation goes through named factory methods (Factory Method pattern) to enforce
 * validation and normalisation in one place.
 */
public final class Subject {
	private static final Subject UNKNOWN = new Subject("Onbekend onderwerp");

	private final String value;

	private Subject(String value) {
		this.value = value;
	}

	/** Strict factory: throws {@link IllegalArgumentException} if {@code rawValue} is null or blank. */
	public static Subject of(String rawValue) {
		String normalized = normalize(rawValue);
		if (normalized.isEmpty()) {
			throw new IllegalArgumentException("Subject cannot be empty");
		}
		return new Subject(normalized);
	}

	/** Tolerant factory: returns {@link #unknown()} if {@code rawValue} is null or blank. Use when reading external data. */
	public static Subject fromNullable(String rawValue) {
		String normalized = normalize(rawValue);
		if (normalized.isEmpty()) {
			return unknown();
		}
		return new Subject(normalized);
	}

	public static Subject unknown() {
		return UNKNOWN;
	}

	public String value() {
		return value;
	}

	private static String normalize(String value) {
		if (value == null) {
			return "";
		}
		return value.trim();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof Subject)) {
			return false;
		}
		Subject that = (Subject) other;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		// Must stay aligned with equals for use in hash-based collections.
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return value;
	}
}
