/*
 * Copyright 2018 Devsoap Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.devsoap.vaadinflow.actions

import com.devsoap.vaadinflow.extensions.VaadinClientDependenciesExtension
import com.moowork.gradle.node.NodeExtension
import com.moowork.gradle.node.NodePlugin
import groovy.util.logging.Log
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Action taken when the Node plugin is applied to a project
 *
 * @author John Ahlroos
 * @since 1.0
 */
@Log('LOGGER')
class NodePluginAction extends PluginAction {

    String pluginId = 'com.moowork.node'

    @Override
    void apply(Project project) {
        super.apply(project)
        project.with {
            pluginManager.apply(NodePlugin)

            repositories.maven { repository ->
                repository.name = 'Gradle Plugin Portal'
                repository.url = 'https://plugins.gradle.org/m2/'
            }

            DependencyHandler projectDependencies = dependencies
            configurations.getByName('runtime').defaultDependencies {
                Dependency nodeGradlePlugin = projectDependencies.create("com.moowork.gradle:gradle-node-plugin:1.2.0")
                it.add(nodeGradlePlugin)
            }
        }
    }

    @Override
    protected void execute(Project project) {
        super.execute(project)

        LOGGER.info('Configuring node extension for vaadin project')
        NodeExtension nodeExtension = project.extensions.getByType(NodeExtension)
        nodeExtension.download = true
        nodeExtension.nodeModulesDir = project.file(VaadinClientDependenciesExtension.FRONTEND_DIR)
    }
}
