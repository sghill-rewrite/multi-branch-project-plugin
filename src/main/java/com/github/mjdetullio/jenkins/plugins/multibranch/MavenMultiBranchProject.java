/*
 * The MIT License
 *
 * Copyright (c) 2014-2015, Matthew DeTullio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.mjdetullio.jenkins.plugins.multibranch;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.maven.MavenModuleSet;
import hudson.maven.MavenModuleSetBuild;
import hudson.model.ItemGroup;
import hudson.model.Items;
import hudson.model.TopLevelItem;
import hudson.util.FormValidation;
import jenkins.branch.BranchProjectFactory;
import jenkins.branch.MultiBranchProjectDescriptor;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.QueryParameter;

/**
 * @author Matthew DeTullio
 */
public final class MavenMultiBranchProject
        extends TemplateDrivenMultiBranchProject<MavenModuleSet, MavenModuleSetBuild> {

    private static final String UNUSED = "unused";

    /**
     * Constructor that specifies the {@link hudson.model.ItemGroup} for this project and the project name.
     *
     * @param parent the project's parent {@link hudson.model.ItemGroup}
     * @param name   the project's name
     */
    public MavenMultiBranchProject(ItemGroup parent, String name) {
        super(parent, name);
    }

    @Override
    protected MavenModuleSet newTemplate() {
        return new MavenModuleSet(this, TemplateDrivenMultiBranchProject.TEMPLATE);
    }

    @NonNull
    @Override
    protected BranchProjectFactory<MavenModuleSet, MavenModuleSetBuild> newProjectFactory() {
        return new MavenBranchProjectFactory();
    }

    @NonNull
    @Override
    public MultiBranchProjectDescriptor getDescriptor() {
        return (DescriptorImpl) Jenkins.getActiveInstance().getDescriptorOrDie(MavenMultiBranchProject.class);
    }

    /**
     * Stapler URL binding used by the configure page to check the location of the POM, alternate settings file, etc -
     * any file.
     *
     * @param value file to check
     * @return validation of file
     */
    @SuppressWarnings(UNUSED)
    public FormValidation doCheckFileInWorkspace(@QueryParameter String value) {
        // Probably not great
        return FormValidation.ok();
    }

    /**
     * {@link MavenMultiBranchProject}'s descriptor.
     */
    @Extension(optional = true)
    public static class DescriptorImpl extends MultiBranchProjectDescriptor {
        @NonNull
        @Override
        public String getDisplayName() {
            return Messages.MavenMultiBranchProject_DisplayName();
        }

        /**
         * Sets the description of this item type.
         *
         * TODO: Override when the baseline is upgraded to 2.x
         *
         * @return A string with the Item description.
         */
        public String getDescription() {
            return Messages.MavenMultiBranchProject_Description();
        }

        /**
         * Needed to define image for new item in Jenkins 2.x.
         *
         * TODO: Override when the baseline is upgraded to 2.x
         *
         * @return A string that represents a URL pattern to get the Item icon in different sizes.
         */
        public String getIconFilePathPattern() {
            return "plugin/multi-branch-project-plugin/images/:size/mavenmultibranchproject.png";
        }

        @Override
        public TopLevelItem newInstance(ItemGroup parent, String name) {
            return new MavenMultiBranchProject(parent, name);
        }
    }

    /**
     * Gives this class an alias for configuration XML.
     */
    @Initializer(before = InitMilestone.PLUGINS_STARTED)
    @SuppressWarnings(UNUSED)
    public static void registerXStream() {
        Items.XSTREAM.alias("maven-multi-branch-project", MavenMultiBranchProject.class);
    }
}
