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
        	Assert.assertTrue(ex.getMessage().contains("k.ey"));
        }
	}
}
