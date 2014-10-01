package com.voodie;

import org.gradle.api.*
import org.gradle.api.tasks.Exec
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.tasks.wrapper.Wrapper

class GruntTask extends Exec {
    private String gruntExecutable = Os.isFamily(Os.FAMILY_WINDOWS) ? "grunt.cmd" : "grunt"
    def gruntArgs = ""
    public GruntTask() {
        super()
        this.setExecutable(gruntExecutable)
    }
    public void setGruntArgs(String gruntArgs) {
        this.args = "$gruntArgs".trim().split(" ") as List
    }
}

class VoodieProject implements Plugin<Project> {
    def void apply(Project project){
        project.apply(plugin:"idea")
        project.task('wrapper', type: Wrapper)
    }
}