package org.springframework.samples.petclinic.gradle;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.gradle.StartParameter;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

/**
 * Small helpers shared by the reactions. Reactions must be stateless, so everything here is static.
 */
final class ProjectSupport {

	private ProjectSupport() {
	}

	/** Adds every notation of an optional list property to the named configuration. */
	static void addDependencies(Project project, String configuration, Provider<List<String>> notations) {
		for (String notation : notations.getOrElse(List.of())) {
			project.getDependencies().add(configuration, notation);
		}
	}

	/** Configures the tasks with the given names, lazily and whether or not they exist yet. */
	static void configureTasks(Project project, Set<String> names, Action<? super Task> action) {
		project.getTasks().configureEach(task -> {
			if (names.contains(task.getName())) {
				action.execute(task);
			}
		});
	}

	/** Disables the tasks with the given names, if they are ever created. */
	static void disableTasks(Project project, String... names) {
		configureTasks(project, Set.of(names), task -> task.setEnabled(false));
	}

	/**
	 * Excludes the tasks with the given names from the invocation, as {@code -x} would. Unlike
	 * disabling, this also keeps the tasks they depend on from running.
	 */
	static void excludeTasks(Project project, String... names) {
		StartParameter startParameter = project.getGradle().getStartParameter();
		Set<String> excluded = new LinkedHashSet<>(startParameter.getExcludedTaskNames());
		excluded.addAll(Arrays.asList(names));
		startParameter.setExcludedTaskNames(excluded);
	}

}
