package org.partmaker;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.testfx.util.WaitForAsyncUtils;

/** TestBase is a base class for unit tests.
 * @author Robert Lichtenberger
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TestBase {

	private File testDir = new File(System.getProperty("java.io.tmpdir"), getClass().getName());
	
	@AfterAll
	@BeforeAll
	private void cleanup() throws IOException {
		if (testDir.exists()) {
			FileUtils.deleteDirectory(testDir);
		}
	}
	
	protected File getTestDir() {
		if (!testDir.exists()) {
			testDir.mkdir();
		}
		return testDir;
	}
	
	public void fx(Runnable runnable) {
		WaitForAsyncUtils.waitForAsyncFx(20000, runnable);
	}
	
	public <T> T fx(Callable<T> callable) {
		return WaitForAsyncUtils.waitForAsyncFx(20000, callable);
	}
	
}
