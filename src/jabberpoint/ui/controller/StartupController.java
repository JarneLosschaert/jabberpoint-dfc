package jabberpoint.ui.controller;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

import jabberpoint.application.port.in.BuildTocUseCase;
import jabberpoint.domain.toc.TocEntry;

public final class StartupController {
	private final BuildTocUseCase buildTocUseCase;

	public StartupController(BuildTocUseCase buildTocUseCase) {
		this.buildTocUseCase = Objects.requireNonNull(buildTocUseCase,
				"buildTocUseCase must not be null");
	}

	public String buildStatusText(String slideShowId) {
		List<TocEntry> entries = buildTocUseCase.buildFor(slideShowId);
		StringJoiner output = new StringJoiner(System.lineSeparator());
		output.add("JabberPoint startup is running.");
		output.add("TOC preview for slideshow '" + slideShowId + "':");
		for (TocEntry entry : entries) {
			output.add(entry.slideNumber() + ". " + entry.subject().value());
		}
		return output.toString();
	}
}
