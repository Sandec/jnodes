/**
 * Module descriptor for jnodes.
 */
module de.sandec.jnodes {
    requires transitive javafx.controls;
    requires transitive simplefx.core;

    exports de.sandec.jnodes.context;
    exports de.sandec.jnodes.css;
    exports de.sandec.jnodes.elements;
    exports de.sandec.jnodes.fork;
}