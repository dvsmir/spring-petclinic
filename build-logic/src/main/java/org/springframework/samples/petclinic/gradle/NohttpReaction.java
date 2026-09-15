package org.springframework.samples.petclinic.gradle;

import io.spring.nohttp.gradle.NoHttpExtension;
import org.gradle.api.Project;
import org.gradle.api.xdcl.Reaction;
import org.gradle.api.xdcl.ReactionScope;

/**
 * Reacts to the {@code nohttp} extension: applies the nohttp plugin and points its
 * {@code checkstyleNohttp} task at the Checkstyle configuration of the project.
 */
public class NohttpReaction implements Reaction<Nohttp, Project> {

	@Override
	public void on(Nohttp data, Project project, ReactionScope scope) {
		project.getPluginManager().apply("io.spring.nohttp");

		// nohttp scans the whole project directory, which contains the build-logic included build; its
		// Gradle caches and outputs hold locked files that cannot be read as task inputs. The task takes
		// its source from the extension by convention mapping, so the excludes must go here.
		NoHttpExtension nohttp = project.getExtensions().getByType(NoHttpExtension.class);
		nohttp.getSource().exclude("**/.gradle/**", "**/build/**");

		project.getTasks().withType(org.gradle.api.plugins.quality.Checkstyle.class).configureEach(task -> {
			if (task.getName().equals("checkstyleNohttp")) {
				task.getConfigDirectory().set(project.file(data.configDirectory().get()));
				task.setConfigFile(project.file(data.configFile().get()));
			}
		});
	}

}
