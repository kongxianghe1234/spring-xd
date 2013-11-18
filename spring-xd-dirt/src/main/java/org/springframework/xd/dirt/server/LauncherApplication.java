
package org.springframework.xd.dirt.server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.xd.dirt.container.ContainerStartedEvent;
import org.springframework.xd.dirt.container.XDContainer;
import org.springframework.xd.dirt.util.BannerUtils;

@Configuration
@EnableAutoConfiguration
@ImportResource({
	"classpath:" + XDContainer.XD_INTERNAL_CONFIG_ROOT + "launcher.xml",
	"classpath:" + XDContainer.XD_INTERNAL_CONFIG_ROOT + "container.xml",
	"classpath*:" + XDContainer.XD_CONFIG_ROOT + "plugins/*.xml" })
public class LauncherApplication {

	public static final String NODE_PROFILE = "node";

	private ConfigurableApplicationContext context;

	public static void main(String[] args) {
		new LauncherApplication().run(args);
	}

	public ConfigurableApplicationContext getContext() {
		return this.context;
	}

	public LauncherApplication run(String... args) {
		System.out.println(BannerUtils.displayBanner(getClass().getSimpleName(), null));
		this.context = new SpringApplicationBuilder(ParentConfiguration.class)
				.profiles(NODE_PROFILE)
				.child(LauncherApplication.class).run(args);
		publishContainerStarted(context);
		return this;
	}

	public static void publishContainerStarted(ConfigurableApplicationContext context) {
		XDContainer container = new XDContainer();
		container.setContext(context);
		context.publishEvent(new ContainerStartedEvent(container));
	}

}
