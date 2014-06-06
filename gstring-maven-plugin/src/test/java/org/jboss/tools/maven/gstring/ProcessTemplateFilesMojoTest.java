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

import java.io.File;
import java.io.FileInputStream;

import junit.framework.Assert;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

public class ProcessTemplateFilesMojoTest extends AbstractMojoTestCase {

	@Test
	public void testOk() throws Exception {
		File pom = getTestFile( "src/test/resources/pom-testOk.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        ProcessTemplateFilesMojo myMojo = (ProcessTemplateFilesMojo) lookupMojo( "process-templates", pom );
        assertNotNull( myMojo );
        myMojo.execute();

        File outputFile = getTestFile("target/testOutput/template.txt");
        FileInputStream stream = new FileInputStream(outputFile);
        byte[] bytes = new byte[(int)outputFile.length()];
        stream.read(bytes);
        String outputText = new String(bytes);
        Assert.assertEquals(
        	"Hello world !\n" +
        	"Hello WORLD !\n\n" +
        	"Hello WORLD (from a closure) !",
        	outputText);
        stream.close();
	}

    @Test
    public void testTemplateFromResource() throws Exception {
        File pom = getTestFile( "src/test/resources/pom-testResourceTemplate.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        ProcessTemplateFilesMojo myMojo = (ProcessTemplateFilesMojo) lookupMojo( "process-templates", pom );
        assertNotNull( myMojo );
        myMojo.execute();

        File outputFile = getTestFile("target/testOutput/template.txt");
        FileInputStream stream = new FileInputStream(outputFile);
        byte[] bytes = new byte[(int)outputFile.length()];
        stream.read(bytes);
        String outputText = new String(bytes);
        Assert.assertEquals(
            "Hello world !\n" +
                "Hello WORLD !\n\n" +
                "Hello WORLD (from a closure) !",
            outputText);
        stream.close();
    }

	@Test
	public void testBadTemplatePath() throws Exception {
		File pom = getTestFile( "src/test/resources/pom-testBadTemplatePath.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        ProcessTemplateFilesMojo myMojo = (ProcessTemplateFilesMojo) lookupMojo( "process-templates", pom );
        assertNotNull( myMojo );
        try {
        	myMojo.execute();
        	Assert.fail();
        } catch (MojoExecutionException ex) {
        	Assert.assertTrue(ex.getMessage().contains("does not exist"));
        }
	}

	@Test
	public void testBadSymbol() throws Exception {
		File pom = getTestFile( "src/test/resources/pom-testBadSymbol.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        ProcessTemplateFilesMojo myMojo = (ProcessTemplateFilesMojo) lookupMojo( "process-templates", pom );
        assertNotNull( myMojo );
        try {
        	myMojo.execute();
        	Assert.fail();
        } catch (MojoExecutionException ex) {
        	Assert.assertTrue(ex.getMessage().contains("p.lace"));
        }
	}
}
