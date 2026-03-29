package jabberpoint.ui.controller;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import jabberpoint.application.port.in.BuildTableOfContentsUseCase;
import jabberpoint.domain.toc.TableOfContentsEntry;

public final class StartupController {
	private final BuildTableOfContentsUseCase buildTableOfContentsUseCase;

	public StartupController(BuildTableOfContentsUseCase buildTableOfContentsUseCase) {
		this.buildTableOfContentsUseCase = Objects.requireNonNull(buildTableOfContentsUseCase,
				"buildTableOfContentsUseCase must not be null");
	}

	public String buildStatusText(String slideShowId) {
		List<TableOfContentsEntry> entries = buildTableOfContentsUseCase.buildFor(slideShowId);
		StringJoiner output = new StringJoiner(System.lineSeparator());
		output.add("JabberPoint startup is running.");
		output.add("TOC preview for slideshow '" + slideShowId + "':");
		for (TableOfContentsEntry entry : entries) {
			output.add(entry.slideNumber() + ". " + entry.subject().value());
		}
		return output.toString();
	}
}
