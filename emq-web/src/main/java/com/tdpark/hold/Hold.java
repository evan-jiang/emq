package com.tdpark.hold;

import java.util.concurrent.atomic.AtomicBoolean;

public class Hold {

	public static volatile AtomicBoolean WAITING = new AtomicBoolean(false);
}
