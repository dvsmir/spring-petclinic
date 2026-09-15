package org.springframework.samples.petclinic.gradle;

import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.plugins.quality.CheckstyleExtension;
import org.gradle.api.xdcl.Reaction;
import org.gradle.api.xdcl.ReactionScope;

/**
 * Reacts to the {@code checkstyle} extension: applies the Checkstyle plugin and puts the Spring
 * Java Format rule set on its tool classpath.
 */
public class CheckstyleReaction implements Reaction<Checkstyle, Project> {

	@Override
	public void on(Checkstyle data, Project project, ReactionScope scope) {
		project.getPluginManager().apply("checkstyle");

		CheckstyleExtension checkstyle = project.getExtensions().getByType(CheckstyleExtension.class);
		checkstyle.getConfigDirectory().set(project.file(data.configDirectory().get()));
		checkstyle.setConfigFile(project.file(data.configFile().get()));

		DependencyHandler dependencies = project.getDependencies();
		dependencies.add("checkstyle",
				"io.spring.javaformat:spring-javaformat-checkstyle:" + data.springJavaformatVersion().get());
		dependencies.add("checkstyle", "com.puppycrawl.tools:checkstyle:" + data.toolVersion().get());

		// The Spring AOT source sets hold generated code and are never checked.
		ProjectSupport.disableTasks(project, "checkstyleAot", "checkstyleAotTest");
	}

}
