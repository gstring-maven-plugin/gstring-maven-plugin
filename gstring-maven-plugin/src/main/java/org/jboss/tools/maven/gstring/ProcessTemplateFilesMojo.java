/**
 * Copyright (c) 2013, Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 * Mickael Istria (Red Hat, Inc.) - initial API and implementation
 */
package org.jboss.tools.maven.gstring;

import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import groovy.text.Template;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.groovy.control.CompilationFailedException;

/**
 * Takes several template files as input, loads them as Groovy GString
 * and evaluate
 *
 * @goal process-templates
 *
 * @phase generate-resource
 */
public class ProcessTemplateFilesMojo extends AbstractMojo {

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	private MavenProject project;

	/**
	 * Location of the file.
	 *
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * @parameter
	 */
	private List<String> files;

    /**
     * @parameter
     */
    private List<String> resources;

	/**
	 * @parameter
	 */
	private Map<String, Object> symbols;

	public void execute() throws MojoExecutionException {
		if ((this.files == null || this.files.isEmpty()) && (this.resources == null || this.resources.isEmpty())) {
			return;
		}

		if (this.symbols == null) {
			this.symbols = new HashMap<String, Object>();
		}
		if (this.symbols.containsKey("project")) {
			throw new MojoExecutionException("'project' is a protected symbol (set to ${project}). Please use something else.");
		}
		this.symbols.put("project", this.project);

		List<String> badSymbols = new ArrayList<String>();
		for (String symbol : this.symbols.keySet()) {
			if (!Character.isJavaIdentifierStart(symbol.charAt(0))) {
				badSymbols.add(symbol);
				break;
			}
			for (int i = 1; i < symbol.length(); i++) {
				if (!Character.isJavaIdentifierPart(symbol.charAt(i))) {
					badSymbols.add(symbol);
					break;
				}
			}
		}
		if (!badSymbols.isEmpty()) {
			throw new MojoExecutionException("symbols have to be valid Java qualifiers " + badSymbols);
		}

		if (!this.outputDirectory.exists()) {
			this.outputDirectory.mkdirs();
		}

		GStringTemplateEngine templateEngine = new GStringTemplateEngine();
        if (this.files != null) {
            for (String path : this.files) {
                File file = new File(path);
                if (!file.exists()) {
                    throw new MojoExecutionException("File " + file + " does not exist.");
                }

                if (!file.isFile()) {
                    throw new MojoExecutionException("File " + file + " is not a valid template file.");
                }

                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                    processInput(templateEngine, path, file.getName(), inputStream);
                }
                catch (FileNotFoundException e) {
                    throw new MojoExecutionException("Can't read file " + file, e);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }

        if (this.resources != null) {
            for (String resourcePath : this.resources) {
                String fileName = new File(resourcePath).getName();
                final InputStream inputStream = getClass().getClassLoader().
                    getResourceAsStream(resourcePath);
                if (inputStream == null) {
                    throw new MojoExecutionException(resourcePath + " does not exist.");
                }

                try {
                    processInput(templateEngine, resourcePath, fileName, inputStream);
                }
                finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }
    }

    private void processInput(GStringTemplateEngine templateEngine, String path, String fileName, InputStream resourceAsStream) throws MojoExecutionException {
        try {
            File outputFile = new File(this.outputDirectory, fileName);
            Template template = templateEngine.createTemplate(new InputStreamReader(resourceAsStream));
            Writable writable = template.make(this.symbols);
            FileWriter outputWriter = new FileWriter(outputFile);
            writable.writeTo(outputWriter);
            outputWriter.close();
        } catch (CompilationFailedException ex) {
            throw new MojoExecutionException("Compilation error in " + path, ex);
        } catch (ClassNotFoundException ex1) {
            throw new MojoExecutionException("Error processing " + path, ex1);
        } catch (IOException ex2) {
            throw new MojoExecutionException("Error while reading/writing " + fileName, ex2);
        }
    }
}
