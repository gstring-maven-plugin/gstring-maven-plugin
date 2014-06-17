# GString-Maven-plugin

## What does it does?

This plugin is meant to allow you to use Groovy GString template mechanism during your Maven build simply by executing a plugin. Templating with GString is one of the simplest and more powerful way to do templating for Java developers. See
* http://groovy.codehaus.org/Groovy+Templates
* http://groovy.codehaus.org/Strings+and+GString#StringsandGString-Strings-GStrings

Big advantage of GStrings over simple pattern replacement/filtering (like the one provided by maven-resource-plugin), is that GString allow you to make branching, conditional values, and everthing else Groovy allows you to do.

## How does it work?

Here is an example usage of how to use the gstring-maven-plugin

Given a template named 'results.html' with the content:

```xml
Hello ${place} !
Hello ${place.toUpperCase()} !
<% /* define closures */ upper = { arg -> arg.toUpperCase() + " (from a closure)" } %>
Hello ${upper place} !
```

In your pom.xml:

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.jboss.tools.maven</groupId>
			<artifactId>gstring-maven-plugin</artifactId>
			<version>1.1.0</version>
			<executions>
				<execution>
					<id>create-results-html</id>
					<phase>package</phase>
					<goals>
						<goal>process-templates</goal>
					</goals>
					<configuration>
						<files>
							<file>results.html</file>
						</files>
						<symbols>
							<place>world</place>
						</symbols>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

This will create `${project.build.directory}/results.html` that contains

```
Hello world !
Hello WORLD !

Hello WORLD (from a closure) !
```

You can either re-build it locally, or consume it directly from JBoss Nexus:
```xml
	<repositories>
		<repository>
			<id>jboss-releases</id>
			<name>JBoss Releases Maven Repository</name>
			<url>https://repository.jboss.org/nexus/content/repositories/releases/</url>
			<releases>
				<enabled>true</enabled>
			</releases>
		</repository>
		<repository>
			<id>jboss-snapshots-repository</id>
			<name>JBoss Snapshots Repository</name>
			<url>https://repository.jboss.org/nexus/content/repositories/snapshots/</url>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</repository>
	</repositories>
```

## Community

A Google Group is used as a mailing-list: https://groups.google.com/forum/#!forum/gstring-maven-plugin

## License

It's EPL

## Contributing

Fork it, submit issues and pull requests.
