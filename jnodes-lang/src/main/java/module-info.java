/**
 * Module descriptor for jnodes-lang.
 */
module de.sandec.jnodes.lang {
    requires transitive de.sandec.jnodes;
    requires typesafe.config;

    opens de.sandec.jnodes.lang;
    exports de.sandec.jnodes.lang;
}