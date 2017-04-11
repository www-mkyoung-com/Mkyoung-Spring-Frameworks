In Spring Boot, you can’t configure the embedded Tomcat `maxSwallowSize` via the [common application properties](http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#common-application-properties), there is no option like `server.tomcat.*.maxSwallowSize`

## Solution

To fix it, you need to declare a `TomcatEmbeddedServletContainerFactory` bean and configure the `maxSwallowSize` like this :

    //...
    import org.apache.coyote.http11.AbstractHttp11Protocol;
    import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
    import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

        private int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB

        @Bean
        public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {

            TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();

            tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {

                // connector other settings...

                // configure maxSwallowSize
                if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                    // -1 means unlimited, accept bytes
                    ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
                }

            });

            return tomcat;

        }

[http://www.mkyong.com/spring-boot/spring-boot-configure-maxswallowsize-in-embedded-tomcat/](http://www.mkyong.com/spring-boot/spring-boot-configure-maxswallowsize-in-embedded-tomcat/)
