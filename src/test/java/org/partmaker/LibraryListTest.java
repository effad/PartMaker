package org.partmaker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.partmaker.LibraryList.ListLoaderService;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
public class LibraryListTest extends TestBase {
	
	@SuppressWarnings("unchecked")
	@Test
	public void testListLoaderService() throws InterruptedException, ExecutionException, IOException, TimeoutException {
		prepareDirectory();
		ListLoaderService service = new LibraryList.ListLoaderService();
		CompletableFuture<List<PartDescriptor>> partsFuture = new CompletableFuture<>();
		service.setOnSucceeded(event -> {
			partsFuture.complete((List<PartDescriptor>) event.getSource().getValue());
		});
		service.load(getTestDir());
		List<PartDescriptor> parts = partsFuture.get(60, TimeUnit.SECONDS);
		Assertions.assertEquals(3, parts.size());
		
		PartDescriptor p1 = parts.stream().filter(pd -> "part1".equals(pd.getName())).findAny().get();
		Assertions.assertEquals("part1", p1.getName());
		Assertions.assertEquals("description1", p1.getDescription());
		Assertions.assertEquals(true, p1.isValid());
		
		PartDescriptor p2 = parts.stream().filter(pd -> "part2".equals(pd.getName())).findAny().get();
		Assertions.assertEquals(false, p2.isValid());
		
		PartDescriptor p3 = parts.stream().filter(pd -> "part3".equals(pd.getName())).findAny().get();
		Assertions.assertEquals(false, p3.isValid());
	}

	private void prepareDirectory() throws IOException {
		File p1 = createPartDir("part1", "{ \"name\": \"part1\", \"description\": \"description1\", \"scriptFile\": \"part1.groovy\" }");
		FileUtils.writeStringToFile(new File(p1, "part1.groovy"), "return null;", StandardCharsets.UTF_8);
		createPartDir("part2", "{ \"name\": \"part2\", \"description\": \"description2\", \"scriptFile\": \"part2.groovy\" }");
		createPartDir("part3", null);
	}
	
	private File createPartDir(String subdir, String json) throws IOException {
		File dir = new File(getTestDir(), subdir);
		if (json != null) {
			FileUtils.writeStringToFile(new File(dir, "partmaker.json"), json, StandardCharsets.UTF_8);
		} else {
			dir.mkdirs();
		}
		return dir;
	}
	
	
}
