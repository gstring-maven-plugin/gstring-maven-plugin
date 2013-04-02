# GString-Maven-plugin

## What does it does?

This plugin is meant to allow you to use Groovy GString template mechanism during your Maven build simply by executing a plugin. Templating with GString is one of the simplest and more powerful way to do templating for Java developers. See
* http://groovy.codehaus.org/Groovy+Templates
* http://groovy.codehaus.org/Strings+and+GString#StringsandGString-Strings-GStrings

## How does it work?

Here is an example usage of how to use the gstring-maven-plugin

```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.jboss.tools.maven</groupId>
			<artifactId>gstring-maven-plugin</artifactId>
			<version>0.0.1-SNAPSHOT</version>
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
							<update_site_description>${update.site.description}</update_site_description>
							<TARGET_PLATFORM_VERSION>${TARGET_PLATFORM_VERSION}</TARGET_PLATFORM_VERSION>
							<TARGET_PLATFORM_VERSION_MAXIMUM>${TARGET_PLATFORM_VERSION_MAXIMUM}</TARGET_PLATFORM_VERSION_MAXIMUM>
							<update_site_qualifier>%{buildQualifier}</update_site_qualifier>
							<JOB_NAME>${JOB_NAME}</JOB_NAME>
							<BUILD_NUMBER>${BUILD_NUMBER}</BUILD_NUMBER>
							<PORTLET_1>27842</PORTLET_1>
							<PORTLET_2>23303</PORTLET_2>
						</symbols>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

Currently, you need to build it locally to use it. But you can soon expect to be able to retrieve it from JBoss Nexus.

## License

It's EPL

## Contributing

Fork it, submit issues and pull requests.
