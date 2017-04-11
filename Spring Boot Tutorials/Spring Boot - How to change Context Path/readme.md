<p>In Spring Boot, to change the context path, update&nbsp;<code>server.contextPath</code>&nbsp;properties. The following examples update the context path from&nbsp;<code>/</code>&nbsp;to&nbsp;<code>/mkyong</code>&nbsp;or&nbsp;<code>http://localhost:8080/mkyong</code></p>

<p><strong>Note</strong><br />
By default, the context path is &ldquo;/&rdquo;.</p>

<p><em>P.S Tested with Spring Boot 1.4.2.RELEASE</em></p>

<h2>1. Properties &amp; Yaml</h2>

<p>1.1 Update via a properties file.</p>

<p>/src/main/resources/application.properties</p>

<pre>
<code>server.port=8080
server.contextPath=/mkyong</code></pre>

<p>1.2 Update via a yaml file.</p>

<p>/src/main/resources/application.yml</p>

<pre>
<code>server:
  port: 8080
  contextPath: /mkyong</code></pre>

<p>&nbsp;</p>

<h2>2. EmbeddedServletContainerCustomizer</h2>

<p>Update via code, this overrides properties and yaml settings.</p>

<p>CustomContainer.java</p>

<pre>
<code>package com.mkyong;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Component;

@Component
public class CustomContainer implements EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {

		container.setPort(8080);
		container.setContextPath("/mkyong");

	}

}</code></pre>

<p>&nbsp;</p>

<h2>3. Command Line</h2>

<p>Update the context path by passing the system properties directly.</p>

<p>Terminal</p>

<pre>
<code>java -jar -Dserver.contextPath=/mkyong spring-boot-example-1.0.jar</code></pre>
