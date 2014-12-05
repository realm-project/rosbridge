package net.realmproject.rosbridge.client.datatypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RosType {
	public String value() default "";
}