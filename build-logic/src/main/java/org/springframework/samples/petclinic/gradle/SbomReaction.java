package org.springframework.samples.petclinic.gradle;

import java.util.List;

import org.cyclonedx.gradle.CyclonedxDirectTask;
import org.gradle.api.Project;
import org.gradle.api.xdcl.Reaction;
import org.gradle.api.xdcl.ReactionScope;

/**
 * Reacts to the {@code sbom} extension: applies the CycloneDX BOM plugin and configures the direct
 * BOM of the application.
 */
public class SbomReaction implements Reaction<Sbom, Project> {

	@Override
	public void on(Sbom data, Project project, ReactionScope scope) {
		project.getPluginManager().apply("org.cyclonedx.bom");

		project.getTasks().withType(CyclonedxDirectTask.class).configureEach(task -> {
			// The BOM describes the compiled application.
			task.dependsOn("compileJava");
			List<String> skipConfigurations = data.skipConfigurations().getOrNull();
			if (skipConfigurations != null) {
				task.getSkipConfigs().set(skipConfigurations);
			}
		});
	}

}
