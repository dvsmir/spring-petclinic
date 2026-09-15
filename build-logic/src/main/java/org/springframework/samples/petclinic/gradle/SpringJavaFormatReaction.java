package org.springframework.samples.petclinic.gradle;

import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.xdcl.Reaction;
import org.gradle.api.xdcl.ReactionScope;

/**
 * Reacts to the {@code springJavaFormat} extension: applies Spring Java Format and, when Checkstyle
 * or nohttp are also active, runs their checks before formatting.
 */
public class SpringJavaFormatReaction implements Reaction<SpringJavaFormat, Project> {

	@Override
	public void on(SpringJavaFormat data, Project project, ReactionScope scope) {
		PluginManager plugins = project.getPluginManager();
		plugins.apply("io.spring.javaformat");

		if (!data.formatAot().get()) {
			// The Spring AOT source sets hold generated code. Disabling their tasks is not enough: the
			// check tasks would still pull in AOT processing to produce their inputs, so they are also
			// excluded from every invocation.
			ProjectSupport.disableTasks(project, "checkFormatAot", "checkFormatAotTest", "formatAot",
					"formatAotTest");
			ProjectSupport.excludeTasks(project, "checkFormatAot", "checkFormatAotTest");
		}

		plugins.withPlugin("checkstyle", plugin -> {
			ProjectSupport.configureTasks(project, Set.of("formatMain"), task -> task.dependsOn("checkstyleMain"));
			ProjectSupport.configureTasks(project, Set.of("formatTest"), task -> task.dependsOn("checkstyleTest"));
		});
		plugins.withPlugin("io.spring.nohttp", plugin -> ProjectSupport.configureTasks(project,
				Set.of("formatMain", "formatTest"), task -> task.dependsOn("checkstyleNohttp")));
	}

}
