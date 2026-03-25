package jabberpoint.domain.model;

import java.util.Objects;

public final class Subject {
	private static final Subject UNKNOWN = new Subject("Onbekend onderwerp");

	private final String value;

	private Subject(String value) {
		this.value = value;
	}

	// Strict factory: call sites that require a real subject should use this method.
	public static Subject of(String rawValue) {
		String normalized = normalize(rawValue);
		if (normalized.isEmpty()) {
			throw new IllegalArgumentException("Subject cannot be empty");
		}
		return new Subject(normalized);
	}

	// Tolerant factory: useful when reading external input where subject may be missing.
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
