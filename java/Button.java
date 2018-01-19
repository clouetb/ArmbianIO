import com.sun.jna.Native;
import java.util.concurrent.TimeUnit;
import armbianio.ArmbianIoLib;

/**
 * Simple callback using built in button.
 * 
 * Copyright (c) 2018 Steven P. Goldsmith
 * See LICENSE.md for details.
 */

public class Button {

	public static void main(String argv[]) throws InterruptedException {
		// Use to debug if JNA cannot find shared library
		System.setProperty("jna.debug_load", "false");
		System.setProperty("jna.debug_load.jna", "false");
		// Load shared library
		final ArmbianIoLib armbianIoLib = (ArmbianIoLib) Native.loadLibrary("/usr/local/lib/libarmbianio.so",
				ArmbianIoLib.class);
		final int rc = armbianIoLib.AIOInit();
		if (rc == 1) {
			System.out.println(String.format("Running on a %s", armbianIoLib.AIOGetBoardName()));
			if (armbianIoLib.AIOHasButton() == 1) {
				ArmbianIoLib.AIOCALLBACK func = (int iPin, int iEdge) -> {
                    System.out.println(String.format("Button state: pin = %d, value = %d", iPin, iEdge));
                };
				System.out.println("Press/release button a few times");
				armbianIoLib.AIOAddGPIOCallback(0, ArmbianIoLib.EDGE_BOTH, func);
				TimeUnit.SECONDS.sleep(10);
				// Remove callback
				armbianIoLib.AIORemoveGPIO(0);
			} else {
				System.out.println(String.format("%s does not have a button", armbianIoLib.AIOGetBoardName()));
			}
			armbianIoLib.AIOShutdown();
		}
	}
}
