package org.springframework.samples.petclinic.gradle;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.xdcl.Reaction;
import org.gradle.api.xdcl.ReactionScope;
import org.gradle.jvm.toolchain.JavaLanguageVersion;

/**
 * Reacts to the {@code springBootApplication} template: applies the Java, Spring Boot and
 * dependency-management plugins, sets the coordinates and the Java toolchain, declares the
 * dependencies and runs tests on the JUnit platform.
 */
public class SpringBootApplicationReaction implements Reaction<SpringBootApplication, Project> {

	@Override
	public void on(SpringBootApplication data, Project project, ReactionScope scope) {
		PluginManager plugins = project.getPluginManager();
		plugins.apply("java");
		plugins.apply("org.springframework.boot");
		plugins.apply("io.spring.dependency-management");

		project.setGroup(data.group().get());
		project.setVersion(data.version().get());

		JavaPluginExtension java = project.getExtensions().getByType(JavaPluginExtension.class);
		java.getToolchain().getLanguageVersion().set(JavaLanguageVersion.of(data.javaVersion().get()));

		data.dependencies().ifPresent(dependencies -> {
			ProjectSupport.addDependencies(project, "implementation", dependencies.implementation());
			ProjectSupport.addDependencies(project, "compileOnly", dependencies.compileOnly());
			ProjectSupport.addDependencies(project, "runtimeOnly", dependencies.runtimeOnly());
			ProjectSupport.addDependencies(project, "developmentOnly", dependencies.developmentOnly());
			ProjectSupport.addDependencies(project, "testImplementation", dependencies.testImplementation());
			ProjectSupport.addDependencies(project, "testRuntimeOnly", dependencies.testRuntimeOnly());
		});

		project.getTasks().withType(Test.class).configureEach(Test::useJUnitPlatform);
	}

}
