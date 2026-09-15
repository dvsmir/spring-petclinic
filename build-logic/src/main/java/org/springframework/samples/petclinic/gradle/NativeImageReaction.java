package org.springframework.samples.petclinic.gradle;

import org.gradle.api.Project;
import org.gradle.api.xdcl.Reaction;
import org.gradle.api.xdcl.ReactionScope;

/** Reacts to the {@code nativeImage} extension: applies the GraalVM Native Build Tools plugin. */
public class NativeImageReaction implements Reaction<NativeImage, Project> {

	@Override
	public void on(NativeImage data, Project project, ReactionScope scope) {
		project.getPluginManager().apply("org.graalvm.buildtools.native");
	}

}
