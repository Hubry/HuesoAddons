package hubry.huesoaddons;

/**
 * A base interface for addon modules. Each method is called during the specified initialization phase.
 * <p>
 * Addons are registered in {@link HuesoAddons#addModule}.
 * Only register a module if it is safe to interact with the mod it is made for!
 */
public interface IModule {

	/**
	 * Set up configs in this method.
	 */
	default void configure() {
	}

	/**
	 * Add things to Hueso in here, using {@link hubry.huesoaddons.common.util.HuesoHooks}
	 */
	default void postInit() {
	}

}
